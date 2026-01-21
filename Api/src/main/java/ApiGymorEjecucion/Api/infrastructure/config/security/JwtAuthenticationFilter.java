package ApiGymorEjecucion.Api.infrastructure.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Filtro de autenticación JWT REAL para producción
 *
 * Valida:
 * - Firma del token
 * - Expiración
 * - Estructura correcta
 *
 * Extrae:
 * - userId (subject)
 * - roles (claim "roles")
 */
@Profile("prod")
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final Key signingKey;

    public JwtAuthenticationFilter(
            @Value("${security.jwt.secret}") String jwtSecret) {
        this.signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        // Si es ruta pública, continuar sin validar token
        if (esRutaPublica(path, method)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Rutas protegidas requieren token
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            escribirError(response, request, HttpStatus.UNAUTHORIZED,
                    "Token no proporcionado");
            return;
        }

        String token = authHeader.substring(BEARER_PREFIX.length());

        try {
            // Parsear y validar JWT
            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token);

            Claims claims = jws.getBody();

            // Extraer userId del subject
            String userId = claims.getSubject();

            // Extraer roles
            @SuppressWarnings("unchecked")
            List<String> roles = claims.get("roles", List.class);

            List<SimpleGrantedAuthority> authorities =
                    roles == null
                            ? List.of()
                            : roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

            // Setear contexto de seguridad
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userId,
                            null,
                            authorities
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Continuar flujo
            filterChain.doFilter(request, response);

        } catch (JwtException ex) {
            // Token inválido, expirado o mal formado
            escribirError(response, request, HttpStatus.UNAUTHORIZED,
                    "Token inválido o expirado: " + ex.getMessage());
        }
    }

    /**
     * Define rutas públicas (sin JWT)
     *
     * IMPORTANTE: Mantener sincronizado con SecurityConfig
     */
    private boolean esRutaPublica(String path, String method) {
        return
                // ===== SWAGGER / OPENAPI =====
                path.startsWith("/v3/api-docs") ||
                        path.startsWith("/swagger-ui") ||
                        path.startsWith("/swagger-ui.html") ||

                        // ===== WEBHOOKS =====
                        path.startsWith("/api/webhooks") ||

                        // ===== PRODUCTOS (GET públicos) =====
                        (path.equals("/api/productos") && method.equals("GET")) ||
                        (path.startsWith("/api/productos/") && method.equals("GET")) ||

                        // ===== REGISTRO CLIENTE (POST público) =====
                        (path.equals("/api/v1/clientes") && method.equals("POST"));
    }

    /**
     * Escritura de error JSON
     */
    private void escribirError(
            HttpServletResponse response,
            HttpServletRequest request,
            HttpStatus status,
            String mensaje) throws IOException {

        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String body = """
                {
                  "status": %d,
                  "error": "%s",
                  "message": "%s",
                  "timestamp": "%s",
                  "path": "%s"
                }
                """.formatted(
                status.value(),
                status.getReasonPhrase(),
                mensaje,
                LocalDateTime.now(),
                request.getRequestURI()
        );

        response.getWriter().write(body);
    }
}
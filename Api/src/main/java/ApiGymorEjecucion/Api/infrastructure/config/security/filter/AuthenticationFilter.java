package ApiGymorEjecucion.Api.infrastructure.config.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Filtro de autenticación JWT (MOCK para desarrollo)
 *
 * ⚠️ SOLO PARA LOCAL - En producción usar JwtAuthenticationFilter
 *
 * Este filtro acepta cualquier token que no esté vacío.
 * No valida firma ni expiración.
 */
@Profile("local")
@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        // Log para debug
        logger.debug("🔍 Filtrando: " + method + " " + path);

        // Si es ruta pública, continuar sin validar token
        if (esRutaPublica(path, method)) {
            logger.debug("✅ Ruta pública, permitiendo acceso sin token");
            filterChain.doFilter(request, response);
            return;
        }

        // Rutas protegidas requieren token
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            logger.warn("❌ Token no proporcionado para: " + path);
            escribirError(
                    response,
                    request,
                    HttpStatus.UNAUTHORIZED,
                    "Token no proporcionado. Usa: Authorization: Bearer <token>"
            );
            return;
        }

        String token = authHeader.substring(BEARER_PREFIX.length());

        // Validación MOCK (solo verifica que no esté vacío)
        if (!validarToken(token)) {
            logger.warn("❌ Token inválido para: " + path);
            escribirError(
                    response,
                    request,
                    HttpStatus.UNAUTHORIZED,
                    "Token inválido o vacío"
            );
            return;
        }

        // Setear contexto de seguridad MOCK con authorities
        // IMPORTANTE: Spring Security necesita al menos un authority
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        "mock-user-id",  // En JWT real: userId extraído del token
                        null,
                        List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_USER"))  // ← AGREGADO
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        logger.debug("✅ Token válido, usuario autenticado: mock-user-id");

        // Continuar flujo
        filterChain.doFilter(request, response);
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
     * Validación JWT MOCK
     *
     * En local: solo verifica que el token no esté vacío
     * En prod: se usa JwtAuthenticationFilter con validación real
     */
    private boolean validarToken(String token) {
        return token != null && !token.isBlank();
    }

    /**
     * Escritura de error JSON consistente con GlobalExceptionHandler
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
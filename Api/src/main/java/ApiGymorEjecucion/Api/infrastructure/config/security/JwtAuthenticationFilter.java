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

        if (esRutaPublica(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            escribirError(response, request, HttpStatus.UNAUTHORIZED, "Token no proporcionado");
            return;
        }

        String token = authHeader.substring(BEARER_PREFIX.length());

        try {
            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token);

            Claims claims = jws.getBody();

            String userId = claims.getSubject();

            @SuppressWarnings("unchecked")
            List<String> roles = claims.get("roles", List.class);

            List<SimpleGrantedAuthority> authorities =
                    roles == null
                            ? List.of()
                            : roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userId,
                            null,
                            authorities
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);

        } catch (JwtException ex) {
            escribirError(response, request, HttpStatus.UNAUTHORIZED, "Token inválido o expirado");
        }
    }

    private boolean esRutaPublica(String path) {
        return
                path.startsWith("/v3/api-docs") ||
                        path.startsWith("/swagger-ui") ||
                        path.startsWith("/swagger-ui.html") ||

                        path.startsWith("/api/webhooks") ||

                        path.equals("/api/productos") ||
                        path.startsWith("/api/productos/") ||

                        path.equals("/api/clientes");
    }

    private void escribirError(
            HttpServletResponse response,
            HttpServletRequest request,
            HttpStatus status,
            String mensaje) throws IOException {

        response.setStatus(status.value());
        response.setContentType("application/json");

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
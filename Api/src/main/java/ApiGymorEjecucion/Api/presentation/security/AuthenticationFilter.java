package ApiGymorEjecucion.Api.presentation.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
 * ⚠️ En producción:
 * - Usar Spring Security con JWT real
 * - Validar firma, expiración y roles
 */
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

        // Rutas públicas
        if (esRutaPublica(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Header Authorization
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            escribirError(
                    response,
                    request,
                    HttpStatus.UNAUTHORIZED,
                    "Token no proporcionado"
            );
            return;
        }

        String token = authHeader.substring(BEARER_PREFIX.length());

        // Validación MOCK
        if (!validarToken(token)) {
            escribirError(
                    response,
                    request,
                    HttpStatus.UNAUTHORIZED,
                    "Token inválido"
            );
            return;
        }

        //  MOCK: setear contexto de seguridad
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        "mock-user-id", // en JWT real: userId
                        null,
                        List.of() // en JWT real: roles
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Continuar flujo
        filterChain.doFilter(request, response);
    }

    /**
     * Define rutas públicas (sin JWT)
     */
    private boolean esRutaPublica(String path) {
        return path.startsWith("/api/webhooks") ||      // Webhooks externos
                path.equals("/api/productos") ||          // Catálogo
                path.startsWith("/api/productos/") ||     // Detalle producto
                path.equals("/api/clientes");             // Registro cliente
    }

    /**
     * Validación JWT MOCK
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

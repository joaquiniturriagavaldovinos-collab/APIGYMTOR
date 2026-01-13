package ApiGymorEjecucion.Api.presentation.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

/**
 * Filtro de autenticación JWT (MOCK para desarrollo)
 *
 * NOTA: Esta es una implementación básica.
 * En producción deberías usar Spring Security con JWT real.
 */
@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // Rutas públicas (sin autenticación)
        String path = request.getRequestURI();
        if (esRutaPublica(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Obtener token del header
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token no proporcionado");
            return;
        }

        String token = authHeader.substring(BEARER_PREFIX.length());

        // Validar token (MOCK - en producción usa JWT real)
        if (!validarToken(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
            return;
        }

        // Token válido - continuar con la petición
        filterChain.doFilter(request, response);
    }

    /**
     * Define rutas que no requieren autenticación
     */
    private boolean esRutaPublica(String path) {
        return path.startsWith("/api/webhooks") ||      // Webhooks externos
                path.equals("/api/productos") ||          // Catálogo
                path.startsWith("/api/productos/") ||     // Detalle producto
                path.equals("/api/clientes");             // Registro cliente
    }


    /**
     * Valida el token JWT (MOCK)
     *
     * TODO: Implementar validación real con:
     * - Verificación de firma
     * - Verificación de expiración
     * - Extracción de claims (userId, roles, etc.)
     */
    private boolean validarToken(String token) {
        // MOCK: Acepta cualquier token no vacío
        return token != null && !token.isBlank();

        // En producción sería algo como:
        // try {
        //     Claims claims = Jwts.parser()
        //         .setSigningKey(SECRET_KEY)
        //         .parseClaimsJws(token)
        //         .getBody();
        //
        //     // Verificar expiración
        //     if (claims.getExpiration().before(new Date())) {
        //         return false;
        //     }
        //
        //     // Extraer información del usuario
        //     String userId = claims.getSubject();
        //     // ... guardar en contexto de seguridad
        //
        //     return true;
        // } catch (JwtException e) {
        //     return false;
        // }
    }
}
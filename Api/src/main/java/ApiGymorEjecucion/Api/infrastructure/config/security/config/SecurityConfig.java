package ApiGymorEjecucion.Api.infrastructure.config.security.config;

import ApiGymorEjecucion.Api.infrastructure.config.security.filter.AuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de seguridad para Spring Security
 *
 * RUTAS PÚBLICAS (sin autenticación):
 * - Swagger/OpenAPI
 * - Webhooks externos (pasarelas de pago)
 * - Catálogo de productos (GET)
 * - Registro de clientes (POST)
 *
 * RUTAS PROTEGIDAS (requieren JWT):
 * - Todas las demás operaciones de la API
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // =========================================================================
    // CONFIGURACIÓN PARA LOCAL (DESARROLLO)
    // =========================================================================
    @Profile("local")
    @Configuration
    public static class LocalSecurityConfig {

        private final AuthenticationFilter authenticationFilter;

        public LocalSecurityConfig(AuthenticationFilter authenticationFilter) {
            this.authenticationFilter = authenticationFilter;
        }

        @Bean
        public SecurityFilterChain localFilterChain(HttpSecurity http) throws Exception {
            http
                    // API REST sin estado (stateless)
                    .sessionManagement(session ->
                            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                    // Desactivar CSRF (no necesario en API REST)
                    .csrf(csrf -> csrf.disable())

                    // Desactivar formulario de login
                    .formLogin(form -> form.disable())

                    // Desactivar HTTP Basic (evita popup de login)
                    .httpBasic(basic -> basic.disable())

                    // Configurar autorizaciones
                    .authorizeHttpRequests(auth -> auth
                            // ===== SWAGGER / OPENAPI =====
                            .requestMatchers(
                                    "/v3/api-docs/**",
                                    "/swagger-ui/**",
                                    "/swagger-ui.html"
                            ).permitAll()

                            // ===== WEBHOOKS (PASARELAS DE PAGO) =====
                            .requestMatchers("/api/webhooks/**").permitAll()

                            // ===== CATÁLOGO PÚBLICO DE PRODUCTOS =====
                            .requestMatchers(
                                    "/api/productos",           // Listar todos
                                    "/api/productos/**"         // Ver detalle, buscar por tipo, etc.
                            ).permitAll()

                            // ===== REGISTRO DE CLIENTE (PÚBLICO) =====
                            .requestMatchers(
                                    "/api/v1/clientes"          // POST crear cliente (público)
                            ).permitAll()

                            // ===== TODAS LAS DEMÁS RUTAS REQUIEREN AUTENTICACIÓN =====
                            // IMPORTANTE: authenticated() permite cualquier usuario autenticado
                            // sin importar sus roles/authorities
                            .anyRequest().authenticated()
                    )

                    // Agregar filtro JWT MOCK antes del filtro de autenticación
                    .addFilterBefore(
                            authenticationFilter,
                            UsernamePasswordAuthenticationFilter.class
                    );

            return http.build();
        }
    }
}
package ApiGymorEjecucion.Api.infrastructure.config.security.config;

import ApiGymorEjecucion.Api.infrastructure.config.security.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Profile("prod")
@Configuration
public class ProdSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public ProdSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain prodFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                .authorizeHttpRequests(auth -> auth
                        // ===== SWAGGER / OPENAPI =====
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // ===== WEBHOOKS =====
                        .requestMatchers("/api/webhooks/**").permitAll()

                        // ===== PRODUCTOS PÚBLICOS =====
                        .requestMatchers(
                                "/api/productos",
                                "/api/productos/**"
                        ).permitAll()

                        // ===== REGISTRO CLIENTE =====
                        .requestMatchers("/api/v1/clientes").permitAll()

                        // ===== RUTAS PROTEGIDAS =====
                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}

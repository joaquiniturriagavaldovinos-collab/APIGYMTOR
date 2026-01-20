package ApiGymorEjecucion.Api.infrastructure.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("local")
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                //  Desactivar CSRF (API REST)
                .csrf(csrf -> csrf.disable())

                //  Desactivar login por formulario
                .formLogin(form -> form.disable())

                //  Desactivar Basic Auth (esto quita el popup)
                .httpBasic(basic -> basic.disable())

                //  Permitir Swagger y OpenAPI
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}

package ApiGymorEjecucion.Api.infrastructure.config.security.config;

import ApiGymorEjecucion.Api.infrastructure.config.security.SecurityConstants;
import ApiGymorEjecucion.Api.infrastructure.config.security.filter.LocalAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Profile("local")
@Configuration
@EnableWebSecurity
public class LocalSecurityConfig {

    private final LocalAuthenticationFilter localAuthenticationFilter;

    public LocalSecurityConfig(LocalAuthenticationFilter localAuthenticationFilter) {
        this.localAuthenticationFilter = localAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain localFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(cors ->{})
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(SecurityConstants.SWAGGER_WHITELIST).permitAll()
                        .requestMatchers("/api/webhooks/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/clientes").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/clientes/rut/**").permitAll()

                        // TODO protegido
                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        localAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*"); // ✅ LOCAL
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}

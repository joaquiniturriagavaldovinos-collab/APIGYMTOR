package ApiGymorEjecucion.Api.infrastructure.config.security;


public final class SecurityConstants {

    private SecurityConstants() {}

    // Headers
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    // Swagger
    public static final String[] SWAGGER_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    // Public APIs
    public static final String[] PUBLIC_ENDPOINTS = {
            "/api/webhooks/**",
            "/api/productos/**",
            "/api/v1/clientes"
    };
}

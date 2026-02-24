package ApiGymorEjecucion.Api.application.dto.response.auth;

public record LoginResponse(
    String token,
    String email,
    String nombre,
    java.util.List<String> roles
) {}
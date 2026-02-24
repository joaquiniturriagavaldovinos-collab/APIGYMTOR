// application/usecase/auth/LoginUseCase.java
package ApiGymorEjecucion.Api.application.usecase.auth;

import ApiGymorEjecucion.Api.application.dto.request.auth.LoginRequest;
import ApiGymorEjecucion.Api.application.dto.response.auth.LoginResponse;
import ApiGymorEjecucion.Api.domain.model.usuario.Usuario;
import ApiGymorEjecucion.Api.domain.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoginUseCase {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Key signingKey;

    public LoginUseCase(
            UsuarioRepository usuarioRepository,
            @Value("${security.jwt.secret}") String jwtSecret) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public LoginResponse ejecutar(LoginRequest request) {
        // 1. Buscar usuario
        Usuario usuario = usuarioRepository.buscarPorEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        // 2. Verificar activo
        if (!usuario.isActivo()) {
            throw new RuntimeException("Usuario inactivo");
        }

        // 3. Verificar password
        if (!passwordEncoder.matches(request.password(), usuario.getPasswordHash())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        // 4. Registrar acceso
        usuario.registrarAcceso();

        // 5. Extraer roles
        List<String> roles = usuario.getRoles().stream()
                .map(rol -> rol.getNombre())
                .collect(Collectors.toList());

        // 6. Generar JWT
        String token = Jwts.builder()
                .setSubject(usuario.getId())
                .claim("roles", roles)
                .claim("email", usuario.getEmail())
                .claim("nombre", usuario.getNombreCompleto())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24hs
                .signWith(signingKey)
                .compact();

        return new LoginResponse(token, usuario.getEmail(), usuario.getNombreCompleto(), roles);
    }
}
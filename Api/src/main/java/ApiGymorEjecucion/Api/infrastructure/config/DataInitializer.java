// infrastructure/config/DataInitializer.java
package ApiGymorEjecucion.Api.infrastructure.config;

import ApiGymorEjecucion.Api.domain.model.usuario.Rol;
import ApiGymorEjecucion.Api.domain.model.usuario.Usuario;
import ApiGymorEjecucion.Api.domain.repository.UsuarioRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DataInitializer implements ApplicationRunner {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public DataInitializer(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!usuarioRepository.existePorEmail("admin@gymor.cl")) {
            Usuario admin = Usuario.crear(
                    UUID.randomUUID().toString(),
                    "admin@gymor.cl",
                    "Admin",
                    "Gymor",
                    passwordEncoder.encode("admin123")
            );
            Rol rolAdmin = Rol.crear("ROLE_ADMIN", "ROLE_ADMIN", "Administrador");
            admin.agregarRol(rolAdmin);
            usuarioRepository.guardar(admin);
            System.out.println("✅ Admin creado: admin@gymor.cl / admin123");
        }
    }
}
// infrastructure/config/DataInitializer.java
package ApiGymorEjecucion.Api.infrastructure.config;

import ApiGymorEjecucion.Api.domain.model.usuario.Rol;
import ApiGymorEjecucion.Api.domain.model.usuario.Usuario;
import ApiGymorEjecucion.Api.domain.repository.UsuarioRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ApiGymorEjecucion.Api.domain.repository.RolRepository;


import java.util.UUID;

@Component
public class DataInitializer implements ApplicationRunner {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public DataInitializer(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

@Override
public void run(ApplicationArguments args) {

    Rol rolAdmin = rolRepository.buscarPorNombre("ROLE_ADMIN")
            .orElseGet(() -> rolRepository.guardar(
                    Rol.crear(
                            UUID.randomUUID().toString(),
                            "ROLE_ADMIN",
                            "Administrador del sistema"
                    )
            ));

    Rol rolCliente = rolRepository.buscarPorNombre("ROLE_CLIENTE")
            .orElseGet(() -> rolRepository.guardar(
                    Rol.crear(
                            UUID.randomUUID().toString(),
                            "ROLE_CLIENTE",
                            "Cliente del sistema"
                    )
            ));

    if (!usuarioRepository.existePorEmail("admin@gymor.cl")) {

        Usuario admin = Usuario.crear(
                UUID.randomUUID().toString(),
                "admin@gymor.cl",
                "Admin",
                "Gymor",
                passwordEncoder.encode("admin123")
        );

        admin.agregarRol(rolAdmin);
        usuarioRepository.guardar(admin);

        System.out.println("✅ Admin creado: admin@gymor.cl / admin123");
    }
}
}
package ApiGymorEjecucion.Api.infrastructure.repository.memory;
import ApiGymorEjecucion.Api.domain.model.usuario.Rol;
import ApiGymorEjecucion.Api.domain.repository.RolRepository;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class RolRepositoryInMemory implements RolRepository {

    private final Map<String, Rol> roles = new HashMap<>();

    public RolRepositoryInMemory() {
        // Se crean al iniciar la app
        Rol cliente = Rol.crear("1", "CLIENTE", "Rol para clientes");
        Rol admin = Rol.crear("2", "ADMIN", "Rol administrador");

        roles.put(cliente.getNombre(), cliente);
        roles.put(admin.getNombre(), admin);
    }

@Override
public Optional<Rol> buscarPorNombre(String nombre) {
    return roles.values().stream()
            .filter(r -> r.getNombre().equals(nombre))
            .findFirst();
}

    @Override
    public Rol guardar(Rol rol) {
        roles.put(rol.getNombre(), rol);
        return rol;
    }
}
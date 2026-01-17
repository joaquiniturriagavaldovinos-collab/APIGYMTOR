package ApiGymorEjecucion.Api.infrastructure.repository.memory;

import ApiGymorEjecucion.Api.domain.model.servicio.ModalidadClase;
import ApiGymorEjecucion.Api.domain.model.servicio.Servicio;
import ApiGymorEjecucion.Api.domain.repository.ServicioRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Implementación en memoria del repositorio de Servicios
 *
 * Para desarrollo y testing local (profile: local, dev, test)
 */
@Repository
@Profile({"local", "dev", "test"})
public class ServicioRepositoryInMemory implements ServicioRepository {

    private final Map<String, Servicio> servicios = new ConcurrentHashMap<>();

    @Override
    public Servicio guardar(Servicio servicio) {
        if (servicio == null) {
            throw new IllegalArgumentException("El servicio no puede ser nulo");
        }
        servicios.put(servicio.getId(), servicio);
        return servicio;
    }

    @Override
    public Optional<Servicio> buscarPorId(String id) {
        return Optional.ofNullable(servicios.get(id));
    }

    @Override
    public List<Servicio> buscarTodos() {
        return new ArrayList<>(servicios.values());
    }

    @Override
    public List<Servicio> buscarActivos() {
        return servicios.values().stream()
                .filter(Servicio::isActivo)
                .collect(Collectors.toList());
    }

    @Override
    public List<Servicio> buscarPorModalidad(ModalidadClase modalidad) {
        return servicios.values().stream()
                .filter(servicio -> servicio.getModalidad() == modalidad)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Servicio> buscarPorNombre(String nombre) {
        return servicios.values().stream()
                .filter(servicio -> servicio.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
    }

    @Override
    public boolean existePorNombre(String nombre) {
        return servicios.values().stream()
                .anyMatch(servicio -> servicio.getNombre().equalsIgnoreCase(nombre));
    }

    @Override
    public boolean eliminar(String id) {
        return servicios.remove(id) != null;
    }

    @Override
    public long contar() {
        return servicios.size();
    }

    // ===== MÉTODOS AUXILIARES PARA TESTING =====

    public void limpiar() {
        servicios.clear();
    }

    public boolean estaVacio() {
        return servicios.isEmpty();
    }
}
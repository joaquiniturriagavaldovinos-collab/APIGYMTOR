package ApiGymorEjecucion.Api.infrastructure.repository.memory;

import ApiGymorEjecucion.Api.domain.model.servicio.Plan;
import ApiGymorEjecucion.Api.domain.repository.PlanRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Implementación en memoria del repositorio de Planes
 *
 * Para desarrollo y testing local (profile: local, dev, test)
 * Los datos se pierden al reiniciar la aplicación
 */
@Repository
@Profile("test")  // ← SOLO TESTS
public class PlanRepositoryInMemory implements PlanRepository {

    private final Map<String, Plan> planes = new ConcurrentHashMap<>();

    @Override
    public Plan guardar(Plan plan) {
        if (plan == null) {
            throw new IllegalArgumentException("El plan no puede ser nulo");
        }
        planes.put(plan.getId(), plan);
        return plan;
    }

    @Override
    public Optional<Plan> buscarPorId(String id) {
        return Optional.ofNullable(planes.get(id));
    }

    @Override
    public List<Plan> buscarTodos() {
        return new ArrayList<>(planes.values());
    }

    @Override
    public List<Plan> buscarActivos() {
        return planes.values().stream()
                .filter(Plan::isActivo)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Plan> buscarPorNombre(String nombre) {
        return planes.values().stream()
                .filter(plan -> plan.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
    }

    @Override
    public boolean existePorNombre(String nombre) {
        return planes.values().stream()
                .anyMatch(plan -> plan.getNombre().equalsIgnoreCase(nombre));
    }

    @Override
    public boolean eliminar(String id) {
        return planes.remove(id) != null;
    }

    @Override
    public long contar() {
        return planes.size();
    }

    // ===== MÉTODOS AUXILIARES PARA TESTING =====

    public void limpiar() {
        planes.clear();
    }

    public boolean estaVacio() {
        return planes.isEmpty();
    }
}
package ApiGymorEjecucion.Api.domain.repository;

import ApiGymorEjecucion.Api.domain.model.servicio.Plan;

import java.util.List;
import java.util.Optional;

public interface PlanRepository {

    Plan guardar(Plan plan);

    Optional<Plan> buscarPorId(String id);

    List<Plan> buscarTodos();

    List<Plan> buscarActivos();

    Optional<Plan> buscarPorNombre(String nombre);

    boolean existePorNombre(String nombre);

    boolean eliminar(String id);

    long contar();
}

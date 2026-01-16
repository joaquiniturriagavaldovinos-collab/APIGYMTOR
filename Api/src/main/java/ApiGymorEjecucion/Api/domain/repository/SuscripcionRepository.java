package ApiGymorEjecucion.Api.domain.repository;

import ApiGymorEjecucion.Api.domain.model.servicio.Suscripcion;

import java.util.List;
import java.util.Optional;

public interface SuscripcionRepository {

    // ===== CRUD =====

    Suscripcion guardar(Suscripcion suscripcion);

    Optional<Suscripcion> buscarPorId(String id);

    boolean eliminar(String id);

    // ===== Queries =====

    List<Suscripcion> buscarPorClienteId(String clienteId);

    Optional<Suscripcion> buscarActivaPorCliente(String clienteId);

    List<Suscripcion> buscarActivas();

    List<Suscripcion> buscarVencidas();

    boolean existeActivaPorCliente(String clienteId);
}

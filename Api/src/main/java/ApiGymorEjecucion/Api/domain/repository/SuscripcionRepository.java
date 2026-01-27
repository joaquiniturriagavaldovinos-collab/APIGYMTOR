package ApiGymorEjecucion.Api.domain.repository;

import ApiGymorEjecucion.Api.domain.model.servicio.Suscripcion;

import java.util.List;
import java.util.Optional;

public interface SuscripcionRepository {

    Suscripcion guardar(Suscripcion suscripcion);

    Optional<Suscripcion> buscarPorId(String id);

    List<Suscripcion> buscarPorCliente(String clienteId);  // ← Este método existe

    List<Suscripcion> buscarActivas();

    List<Suscripcion> buscarTodas();

    boolean eliminar(String id);

    long contar();
}
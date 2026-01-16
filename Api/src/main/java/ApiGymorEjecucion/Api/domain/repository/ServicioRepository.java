package ApiGymorEjecucion.Api.domain.repository;

import ApiGymorEjecucion.Api.domain.model.servicio.ModalidadClase;
import ApiGymorEjecucion.Api.domain.model.servicio.Servicio;

import java.util.List;
import java.util.Optional;

public interface ServicioRepository {

    Servicio guardar(Servicio servicio);

    List<Servicio> buscarTodos();

    List<Servicio> buscarPorModalidad(ModalidadClase modalidad);

    Optional<Servicio> buscarPorId(String id);
}
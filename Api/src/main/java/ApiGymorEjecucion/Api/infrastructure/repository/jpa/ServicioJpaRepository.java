package ApiGymorEjecucion.Api.infrastructure.repository.jpa;

import ApiGymorEjecucion.Api.domain.model.servicio.ModalidadClase;
import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.ServicioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServicioJpaRepository
        extends JpaRepository<ServicioEntity, String> {

    Optional<ServicioEntity> findByNombre(String nombre);

    List<ServicioEntity> findByActivoTrue();

    List<ServicioEntity> findByModalidad(ModalidadClase modalidad);

    boolean existsByNombre(String nombre);

}

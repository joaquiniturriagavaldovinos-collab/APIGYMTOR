package ApiGymorEjecucion.Api.infrastructure.repository.jpa;

import ApiGymorEjecucion.Api.domain.model.servicio.ModalidadClase;
import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.ServicioJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServicioJpaRepository
        extends JpaRepository<ServicioJpaEntity, String> {

    Optional<ServicioJpaEntity> findByNombre(String nombre);

    List<ServicioJpaEntity> findByActivoTrue();

    List<ServicioJpaEntity> findByModalidad(ModalidadClase modalidad);
}

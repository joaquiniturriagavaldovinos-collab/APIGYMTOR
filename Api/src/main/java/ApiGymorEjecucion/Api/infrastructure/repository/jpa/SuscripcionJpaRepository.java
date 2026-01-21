package ApiGymorEjecucion.Api.infrastructure.repository.jpa;

import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.suscripcion.SuscripcionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public interface SuscripcionJpaRepository
        extends JpaRepository<SuscripcionEntity, String> {

    List<SuscripcionEntity> findByClienteId(String clienteId);

    Optional<SuscripcionEntity> findByClienteIdAndActivaTrue(String clienteId);

    List<SuscripcionEntity> findByActivaTrue();

    List<SuscripcionEntity> findByFechaVencimientoBefore(LocalDateTime fecha);

    boolean existsByClienteIdAndActivaTrue(String clienteId);
}

package ApiGymorEjecucion.Api.infrastructure.repository.jpa;

import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.cliente.ClienteEntity;
import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.cliente.TipoClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteJpaRepository extends JpaRepository<ClienteEntity, String> {

    Optional<ClienteEntity> findByRut(String rut);

    Optional<ClienteEntity> findByEmail(String email);

    boolean existsByRut(String rut);

    boolean existsByEmail(String email);

    List<ClienteEntity> findByTipo(TipoClienteEntity tipo);

    List<ClienteEntity> findByActivoTrue();
}

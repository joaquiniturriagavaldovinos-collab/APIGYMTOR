package ApiGymorEjecucion.Api.infrastructure.repository.jpa;

import ApiGymorEjecucion.Api.domain.model.Despacho.EstadoDespacho;
import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.despacho.DespachoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DespachoJpaRepository
        extends JpaRepository<DespachoEntity, String> {

    Optional<DespachoEntity> findByPedidoId(String pedidoId);

    Optional<DespachoEntity> findByGuiaDespacho_Numero(String numero);

    List<DespachoEntity> findByEstado(EstadoDespacho estado);

    boolean existsByPedidoId(String pedidoId);
}

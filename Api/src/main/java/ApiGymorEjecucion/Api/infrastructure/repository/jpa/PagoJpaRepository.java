package ApiGymorEjecucion.Api.infrastructure.repository.jpa;

import ApiGymorEjecucion.Api.domain.model.Pago.EstadoPago;
import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.PagoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PagoJpaRepository extends JpaRepository<PagoEntity, String> {

    List<PagoEntity> findByPedidoId(String pedidoId);

    Optional<PagoEntity> findByReferenciaPasarela(String referenciaPasarela);

    List<PagoEntity> findByEstado(EstadoPago estado);

    boolean existsByPedidoId(String pedidoId);
}

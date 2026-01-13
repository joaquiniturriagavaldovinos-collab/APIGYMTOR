package ApiGymorEjecucion.Api.infrastructure.repository.jpa;

import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository (auto-implementado por Spring)
 * Este es el repositorio "puro" de Spring Data
 */
@Repository
public interface PedidoJpaRepository extends JpaRepository<PedidoEntity, String> {
    List<PedidoEntity> findByClienteId(String clienteId);
    List<PedidoEntity> findByEstado(PedidoEntity.EstadoPedidoEntity estado);
}
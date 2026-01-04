package ApiGymorEjecucion.Api.infrastructure.repository.jpa;

import ApiGymorEjecucion.Api.domain.model.pedido.EstadoPedido;
import ApiGymorEjecucion.Api.domain.model.pedido.ItemPedido;
import ApiGymorEjecucion.Api.domain.model.pedido.Pedido;
import ApiGymorEjecucion.Api.domain.model.pedido.TipoItem;
import ApiGymorEjecucion.Api.domain.repository.PedidoRepository;
import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Implementación JPA del repositorio de Pedidos.
 *
 * ARQUITECTURA:
 * 1. PedidoJpaRepository (Spring Data) → Acceso directo a BD
 * 2. Esta clase (PedidoRepositoryJpa) → Adaptador que convierte Entity ↔ Domain
 *
 * NOTA: Descomenta @Primary cuando quieras usar BD en lugar de memoria
 */
@Repository
// @Primary  // ← Descomentar para usar BD en lugar de memoria
public class PedidoRepositoryJpa implements PedidoRepository {

    private final PedidoJpaRepository jpaRepository;

    public PedidoRepositoryJpa(PedidoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Pedido guardar(Pedido pedido) {
        PedidoEntity entity = mapearAEntity(pedido);
        PedidoEntity guardado = jpaRepository.save(entity);
        return mapearADominio(guardado);
    }

    @Override
    public Optional<Pedido> buscarPorId(String id) {
        return jpaRepository.findById(id)
                .map(this::mapearADominio);
    }

    @Override
    public List<Pedido> buscarPorCliente(String clienteId) {
        return jpaRepository.findByClienteId(clienteId).stream()
                .map(this::mapearADominio)
                .collect(Collectors.toList());
    }

    @Override
    public List<Pedido> buscarPorEstado(EstadoPedido estado) {
        PedidoEntity.EstadoPedidoEntity estadoEntity =
                PedidoEntity.EstadoPedidoEntity.valueOf(estado.name());

        return jpaRepository.findByEstado(estadoEntity).stream()
                .map(this::mapearADominio)
                .collect(Collectors.toList());
    }

    @Override
    public List<Pedido> buscarTodos() {
        return jpaRepository.findAll().stream()
                .map(this::mapearADominio)
                .collect(Collectors.toList());
    }

    @Override
    public boolean eliminar(String id) {
        if (jpaRepository.existsById(id)) {
            jpaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean existe(String id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public long contar() {
        return jpaRepository.count();
    }

    // ===== MAPPERS: Entity ↔ Domain =====

    private PedidoEntity mapearAEntity(Pedido pedido) {
        PedidoEntity entity = new PedidoEntity(
                pedido.getId(),
                pedido.getClienteId(),
                PedidoEntity.EstadoPedidoEntity.valueOf(pedido.getEstado().name()),
                pedido.calcularTotal(),
                pedido.getFechaCreacion()
        );

        entity.setFechaActualizacion(pedido.getFechaActualizacion());
        entity.setReferenciaPago(pedido.getReferenciaPago());
        entity.setGuiaDespacho(pedido.getGuiaDespacho());

        // Mapear items
        List<PedidoEntity.ItemPedidoEntity> itemsEntity = pedido.getItems().stream()
                .map(this::mapearItemAEntity)
                .collect(Collectors.toList());
        entity.setItems(itemsEntity);

        return entity;
    }

    private Pedido mapearADominio(PedidoEntity entity) {
        // Mapear items
        List<ItemPedido> items = entity.getItems().stream()
                .map(this::mapearItemADominio)
                .collect(Collectors.toList());

        // Crear pedido de dominio
        Pedido pedido = Pedido.crear(
                entity.getId(),
                entity.getClienteId(),
                items
        );

        // Restaurar estado y otros campos mediante reflection o métodos package-private
        // NOTA: Aquí necesitarías métodos especiales en Pedido para reconstruir desde BD
        // Por simplicidad, este es un esquema básico

        return pedido;
    }

    private PedidoEntity.ItemPedidoEntity mapearItemAEntity(ItemPedido item) {
        return new PedidoEntity.ItemPedidoEntity(
                item.getProductoId(),
                item.getNombre(),
                item.getTipo().name(),
                item.getCantidad(),
                item.getPrecioUnitario(),
                item.getSubtotal()
        );
    }

    private ItemPedido mapearItemADominio(PedidoEntity.ItemPedidoEntity entity) {
        return ItemPedido.crear(
                entity.getProductoId(),
                entity.getNombre(),
                TipoItem.valueOf(entity.getTipo()),
                entity.getCantidad(),
                entity.getPrecioUnitario()
        );
    }
}

/**
 * Spring Data JPA Repository (auto-implementado por Spring)
 */
interface PedidoJpaRepository extends JpaRepository<PedidoEntity, String> {
    List<PedidoEntity> findByClienteId(String clienteId);
    List<PedidoEntity> findByEstado(PedidoEntity.EstadoPedidoEntity estado);
}
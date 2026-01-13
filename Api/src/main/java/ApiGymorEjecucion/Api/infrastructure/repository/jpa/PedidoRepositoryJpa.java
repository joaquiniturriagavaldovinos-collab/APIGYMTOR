package ApiGymorEjecucion.Api.infrastructure.repository.jpa;

import ApiGymorEjecucion.Api.domain.model.pedido.*;
import ApiGymorEjecucion.Api.domain.repository.PedidoRepository;
import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.PedidoEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador JPA para Pedido (Arquitectura Hexagonal + DDD)
 */
@Repository
@Profile("prod")
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

        // Mapear historial de transiciones
        List<PedidoEntity.TransicionEstadoEntity> historialEntity =
                pedido.getHistorialEstados().stream()
                        .map(this::mapearTransicionAEntity)
                        .collect(Collectors.toList());
        entity.setHistorialEstados(historialEntity);

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

        // Restaurar estado y campos mediante reflection (necesario porque el dominio es inmutable)
        try {
            // Restaurar estado
            Field estadoField = Pedido.class.getDeclaredField("estado");
            estadoField.setAccessible(true);
            estadoField.set(pedido, EstadoPedido.valueOf(entity.getEstado().name()));

            // Restaurar fecha de actualización
            Field fechaActField = Pedido.class.getDeclaredField("fechaActualizacion");
            fechaActField.setAccessible(true);
            fechaActField.set(pedido, entity.getFechaActualizacion());

            // Restaurar referencia de pago
            if (entity.getReferenciaPago() != null) {
                Field refPagoField = Pedido.class.getDeclaredField("referenciaPago");
                refPagoField.setAccessible(true);
                refPagoField.set(pedido, entity.getReferenciaPago());
            }

            // Restaurar guía de despacho
            if (entity.getGuiaDespacho() != null) {
                Field guiaField = Pedido.class.getDeclaredField("guiaDespacho");
                guiaField.setAccessible(true);
                guiaField.set(pedido, entity.getGuiaDespacho());
            }

            // Restaurar historial de estados
            Field historialField = Pedido.class.getDeclaredField("historialEstados");
            historialField.setAccessible(true);
            List<TransicionEstado> historial = entity.getHistorialEstados().stream()
                    .map(this::mapearTransicionADominio)
                    .collect(Collectors.toList());
            historialField.set(pedido, historial);

        } catch (Exception e) {
            throw new RuntimeException("Error al reconstruir Pedido desde BD", e);
        }

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

    private PedidoEntity.TransicionEstadoEntity mapearTransicionAEntity(TransicionEstado trans) {
        PedidoEntity.EstadoPedidoEntity estadoAnterior = trans.getEstadoAnterior() != null
                ? PedidoEntity.EstadoPedidoEntity.valueOf(trans.getEstadoAnterior().name())
                : null;

        return new PedidoEntity.TransicionEstadoEntity(
                estadoAnterior,
                PedidoEntity.EstadoPedidoEntity.valueOf(trans.getEstadoNuevo().name()),
                trans.getFechaTransicion(),
                trans.getObservacion()
        );
    }

    private TransicionEstado mapearTransicionADominio(PedidoEntity.TransicionEstadoEntity entity) {
        EstadoPedido estadoAnterior = entity.getEstadoAnterior() != null
                ? EstadoPedido.valueOf(entity.getEstadoAnterior().name())
                : null;

        return TransicionEstado.crear(
                estadoAnterior,
                EstadoPedido.valueOf(entity.getEstadoNuevo().name()),
                entity.getObservacion()
        );
    }
}

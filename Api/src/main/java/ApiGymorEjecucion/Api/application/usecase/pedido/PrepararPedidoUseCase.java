package ApiGymorEjecucion.Api.application.usecase.pedido;

import ApiGymorEjecucion.Api.application.dto.response.PedidoResponse;
import ApiGymorEjecucion.Api.application.mapper.PedidoMapper;
import ApiGymorEjecucion.Api.domain.exception.PedidoNoEncontradoException;
import ApiGymorEjecucion.Api.domain.model.pedido.Pedido;
import ApiGymorEjecucion.Api.domain.repository.PedidoRepository;
import org.springframework.stereotype.Service;

/**
 * CU4: Preparar Pedido
 *
 * Transición: PAID -> PREPARING
 * Asigna el pedido a logística/bodega para preparación.
 */
@Service
public class PrepararPedidoUseCase {

    private final PedidoRepository pedidoRepository;

    public PrepararPedidoUseCase(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    /**
     * Marca un pedido como en preparación
     *
     * @param pedidoId ID del pedido
     * @return Pedido actualizado en estado PREPARING
     */
    public PedidoResponse ejecutar(String pedidoId) {
        // Validar input
        if (pedidoId == null || pedidoId.isBlank()) {
            throw new IllegalArgumentException("El ID del pedido es requerido");
        }

        // Buscar pedido
        Pedido pedido = pedidoRepository.buscarPorId(pedidoId)
                .orElseThrow(() -> new PedidoNoEncontradoException(pedidoId));

        // Validar que pueda prepararse
        if (!pedido.puedePrepararse()) {
            throw new IllegalStateException(
                    String.format("El pedido en estado %s no puede prepararse",
                            pedido.getEstado().getDescripcion())
            );
        }

        // El dominio valida la transición
        pedido.preparar();

        // Persistir cambio
        Pedido pedidoActualizado = pedidoRepository.guardar(pedido);

        // Aquí se integraría con sistema de bodega/WMS
        // - Reservar stock definitivo
        // - Asignar a operador
        // - Generar orden de picking

        return PedidoMapper.toResponse(pedidoActualizado);
    }
}
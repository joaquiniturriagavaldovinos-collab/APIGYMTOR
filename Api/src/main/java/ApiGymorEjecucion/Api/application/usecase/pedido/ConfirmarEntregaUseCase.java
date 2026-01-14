package ApiGymorEjecucion.Api.application.usecase.pedido;

import ApiGymorEjecucion.Api.application.dto.response.pedido.PedidoResponse;
import ApiGymorEjecucion.Api.application.mapper.PedidoMapper;
import ApiGymorEjecucion.Api.domain.exception.PedidoNoEncontradoException;
import ApiGymorEjecucion.Api.domain.model.pedido.Pedido;
import ApiGymorEjecucion.Api.domain.repository.PedidoRepository;
import org.springframework.stereotype.Service;

/**
 * CU6: Confirmar Entrega
 *
 * Transición: DISPATCHED -> DELIVERED (ESTADO FINAL)
 * Confirma que el pedido fue entregado al cliente.
 */
@Service
public class ConfirmarEntregaUseCase {

    private final PedidoRepository pedidoRepository;

    public ConfirmarEntregaUseCase(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    /**
     * Confirma la entrega de un pedido despachado
     *
     * @param pedidoId ID del pedido
     * @return Pedido en estado final DELIVERED
     */
    public PedidoResponse ejecutar(String pedidoId) {
        // Validar input
        if (pedidoId == null || pedidoId.isBlank()) {
            throw new IllegalArgumentException("El ID del pedido es requerido");
        }

        // Buscar pedido
        Pedido pedido = pedidoRepository.buscarPorId(pedidoId)
                .orElseThrow(() -> new PedidoNoEncontradoException(pedidoId));

        // El dominio valida la transición (debe estar en DISPATCHED)
        pedido.confirmarEntrega();

        // Persistir cambio
        Pedido pedidoActualizado = pedidoRepository.guardar(pedido);

        // Aquí se integraría con:
        // - Sistema de satisfacción (enviar encuesta)
        // - Sistema contable (registrar venta completada)
        // - CRM (actualizar historial cliente)

        return PedidoMapper.toResponse(pedidoActualizado);
    }
}
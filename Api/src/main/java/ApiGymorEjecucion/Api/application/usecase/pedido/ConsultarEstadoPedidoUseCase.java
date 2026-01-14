package ApiGymorEjecucion.Api.application.usecase.pedido;

import ApiGymorEjecucion.Api.application.dto.response.pedido.PedidoResponse;
import ApiGymorEjecucion.Api.application.mapper.PedidoMapper;
import ApiGymorEjecucion.Api.domain.exception.PedidoNoEncontradoException;
import ApiGymorEjecucion.Api.domain.model.pedido.Pedido;
import ApiGymorEjecucion.Api.domain.repository.PedidoRepository;
import org.springframework.stereotype.Service;

/**
 * Caso de uso: Consultar Estado de Pedido
 *
 * Permite al cliente o al sistema consultar el estado actual de un pedido.
 */
@Service
public class ConsultarEstadoPedidoUseCase {

    private final PedidoRepository pedidoRepository;

    public ConsultarEstadoPedidoUseCase(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    /**
     * Consulta el estado actual de un pedido
     *
     * @param pedidoId ID del pedido a consultar
     * @return Información completa del pedido
     */
    public PedidoResponse ejecutar(String pedidoId) {
        // Validar input
        if (pedidoId == null || pedidoId.isBlank()) {
            throw new IllegalArgumentException("El ID del pedido es requerido");
        }

        // Buscar pedido
        Pedido pedido = pedidoRepository.buscarPorId(pedidoId)
                .orElseThrow(() -> new PedidoNoEncontradoException(pedidoId));

        // Retornar información
        return PedidoMapper.toResponse(pedido);
    }
}
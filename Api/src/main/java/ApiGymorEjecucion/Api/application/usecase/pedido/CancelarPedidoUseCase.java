package ApiGymorEjecucion.Api.application.usecase.pedido;



import ApiGymorEjecucion.Api.application.dto.response.pedido.PedidoResponse;
import ApiGymorEjecucion.Api.application.mapper.PedidoMapper;
import ApiGymorEjecucion.Api.domain.exception.PedidoNoEncontradoException;
import ApiGymorEjecucion.Api.domain.model.pedido.Pedido;
import ApiGymorEjecucion.Api.domain.repository.PedidoRepository;
import org.springframework.stereotype.Service;

/**
 * Caso de uso: Cancelar Pedido
 *
 * Permite cancelar un pedido en estados permitidos:
 * - CREATED
 * - PAYMENT_PENDING
 * - PAID (ventana de tiempo limitada)
 * - PREPARING (casos excepcionales)
 */
@Service
public class CancelarPedidoUseCase {

    private final PedidoRepository pedidoRepository;

    public CancelarPedidoUseCase(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    /**
     * Cancela un pedido
     *
     * @param pedidoId ID del pedido a cancelar
     * @param motivo Razón de la cancelación
     * @return Pedido cancelado
     */
    public PedidoResponse ejecutar(String pedidoId, String motivo) {
        // Validar inputs
        if (pedidoId == null || pedidoId.isBlank()) {
            throw new IllegalArgumentException("El ID del pedido es requerido");
        }
        if (motivo == null || motivo.isBlank()) {
            throw new IllegalArgumentException("El motivo de cancelación es requerido");
        }

        // Buscar pedido
        Pedido pedido = pedidoRepository.buscarPorId(pedidoId)
                .orElseThrow(() -> new PedidoNoEncontradoException(pedidoId));

        // El dominio valida si puede cancelarse
        pedido.cancelar(motivo);

        // Persistir cambio
        Pedido pedidoActualizado = pedidoRepository.guardar(pedido);

        // Aquí se integraría con:
        // - Sistema de pagos (procesar reembolso si aplica)
        // - Sistema de inventario (liberar reservas)
        // - Notificaciones (avisar al cliente)

        return PedidoMapper.toResponse(pedidoActualizado);
    }
}

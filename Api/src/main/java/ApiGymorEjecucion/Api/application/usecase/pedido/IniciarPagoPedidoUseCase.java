package ApiGymorEjecucion.Api.application.usecase.pedido;

import ApiGymorEjecucion.Api.application.dto.response.pedido.PedidoResponse;
import ApiGymorEjecucion.Api.application.mapper.IniciarPagoMapper;
import ApiGymorEjecucion.Api.application.mapper.PedidoMapper;
import ApiGymorEjecucion.Api.domain.exception.PedidoNoEncontradoException;
import ApiGymorEjecucion.Api.domain.model.Pago.MetodoPago;
import ApiGymorEjecucion.Api.domain.model.Pago.Pago;
import ApiGymorEjecucion.Api.domain.model.pedido.Pedido;
import ApiGymorEjecucion.Api.domain.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CU2: Iniciar Pago de Pedido
 *
 * Transición: CREATED -> PAYMENT_PENDING
 * Bloquea el pedido para cambios e inicia el proceso de pago.
 */
@Service
public class IniciarPagoPedidoUseCase {

    private final PedidoRepository pedidoRepository;

    public IniciarPagoPedidoUseCase(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    /**
     * Inicia el proceso de pago para un pedido
     *
     * @param pedidoId ID del pedido
     * @return Pedido actualizado en estado PAYMENT_PENDING
     */

    @Transactional
    public PedidoResponse ejecutar(String pedidoId) {
        // Validar input
        if (pedidoId == null || pedidoId.isBlank()) {
            throw new IllegalArgumentException("El ID del pedido es requerido");
        }

        // Buscar pedido
        Pedido pedido = pedidoRepository.buscarPorId(pedidoId)
                .orElseThrow(() -> new PedidoNoEncontradoException(pedidoId));

        // El dominio valida la transición (debe estar en CREATED)
        pedido.iniciarPago();

        // Persistir cambio
        Pedido pedidoActualizado = pedidoRepository.guardar(pedido);

        // Crear el registro de pago
        MetodoPago metodoPago = MetodoPago.fromString(metodoPagoStr);
        Pago pago = iniciarPagoUseCase.ejecutarYRetornarDominio( // Nuevo método
                pedidoId,
                pedido.getTotal(),
                metodoPago
        );

        // Retornar response combinado
        return IniciarPagoMapper.toResponse(pedidoActualizado, pago);
    }
}
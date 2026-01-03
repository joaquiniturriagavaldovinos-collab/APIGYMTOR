package ApiGymorEjecucion.Api.application.usecase.pedido;


import ApiGymorEjecucion.Api.application.dto.request.ConfirmarPagoRequest;
import ApiGymorEjecucion.Api.application.dto.response.PedidoResponse;
import ApiGymorEjecucion.Api.application.mapper.PedidoMapper;
import ApiGymorEjecucion.Api.domain.exception.PedidoNoEncontradoException;
import ApiGymorEjecucion.Api.domain.model.pedido.Pedido;
import ApiGymorEjecucion.Api.domain.repository.PedidoRepository;
import org.springframework.stereotype.Service;

/**
 * CU3: Confirmar Resultado de Pago (Webhook)
 *
 * Transiciones:
 * - PAYMENT_PENDING -> PAID (pago exitoso)
 * - PAYMENT_PENDING -> FAILED (pago fallido)
 *
 * Implementa idempotencia para manejar reintentos del webhook.
 */
@Service
public class ConfirmarResultadoPagoUseCase {

    private final PedidoRepository pedidoRepository;

    public ConfirmarResultadoPagoUseCase(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    /**
     * Procesa el resultado del pago recibido desde la pasarela
     *
     * @param request Datos de confirmación del pago
     * @return Pedido actualizado (PAID o FAILED)
     */
    public PedidoResponse ejecutar(ConfirmarPagoRequest request) {
        // Validar request
        validarRequest(request);

        // Buscar pedido
        Pedido pedido = pedidoRepository.buscarPorId(request.getPedidoId())
                .orElseThrow(() -> new PedidoNoEncontradoException(request.getPedidoId()));

        // Implementar idempotencia: si ya está en PAID o FAILED, no reprocesar
        if (pedido.estaPagado() || pedido.estaFinalizado()) {
            // Log: "Pago ya procesado previamente"
            return PedidoMapper.toResponse(pedido);
        }

        // Procesar según resultado del pago
        if (request.isExitoso()) {
            pedido.confirmarPago(request.getReferenciaPago());
        } else {
            String motivo = request.getMotivoFallo() != null
                    ? request.getMotivoFallo()
                    : "Pago rechazado por la pasarela";
            pedido.marcarPagoFallido(motivo);
        }

        // Persistir cambio
        Pedido pedidoActualizado = pedidoRepository.guardar(pedido);

        // Aquí se emitirían eventos de dominio:
        // - PedidoPagado -> notificar al cliente, reservar stock
        // - PagoFallido -> notificar al cliente, liberar carrito

        return PedidoMapper.toResponse(pedidoActualizado);
    }

    private void validarRequest(ConfirmarPagoRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }
        if (request.getPedidoId() == null || request.getPedidoId().isBlank()) {
            throw new IllegalArgumentException("El ID del pedido es requerido");
        }
        if (request.isExitoso() &&
                (request.getReferenciaPago() == null || request.getReferenciaPago().isBlank())) {
            throw new IllegalArgumentException(
                    "La referencia deContinuar28 dic 2025pago es requerida para pagos exitosos"
            );
        }
    }
}
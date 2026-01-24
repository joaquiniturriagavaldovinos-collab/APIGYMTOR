package ApiGymorEjecucion.Api.application.usecase.pago;

import ApiGymorEjecucion.Api.application.dto.request.pago.ConfirmarPagoRequest;
import ApiGymorEjecucion.Api.domain.model.Pago.Pago;
import ApiGymorEjecucion.Api.domain.model.pedido.Pedido;
import ApiGymorEjecucion.Api.domain.repository.PagoRepository;
import ApiGymorEjecucion.Api.domain.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfirmarResultadoPagoUseCase {

    private final PagoRepository pagoRepository;
    private final PedidoRepository pedidoRepository;

    public ConfirmarResultadoPagoUseCase(
            PagoRepository pagoRepository,
            PedidoRepository pedidoRepository) {
        this.pagoRepository = pagoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional
    public void ejecutar(ConfirmarPagoRequest request) {

        // 1. Validar request
        validarRequest(request);

        // 2. Buscar pago por referencia de pasarela
        Pago pago = pagoRepository.buscarPorReferenciaPasarela(request.getReferenciaPago())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el pago con referencia: " + request.getReferenciaPago()
                ));

        // 3. Idempotencia: Si ya está procesado, no hacer nada
        if (pago.estaFinalizado()) {
            System.out.println("⚠️ Pago ya procesado (webhook duplicado). Ignorando...");
            return;
        }

        // 4. Buscar pedido asociado
        Pedido pedido = pedidoRepository.buscarPorId(pago.getPedidoId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el pedido: " + pago.getPedidoId()
                ));

        // 5. Procesar según resultado
        if (request.isExitoso()) {
            // ✅ PAGO EXITOSO

            // Generar código de autorización si no viene
            String codigoAuth = request.getCodigoAutorizacion();
            if (codigoAuth == null || codigoAuth.isBlank() || "null".equals(codigoAuth)) {
                codigoAuth = "AUTH-" + System.currentTimeMillis();
            }

            // ⚠️ IMPORTANTE: Este método debe guardar el código
            pago.confirmarExitoso(codigoAuth);
            pedido.confirmarPago(request.getReferenciaPago());

            System.out.println("✅ Pago confirmado: " + pago.getId());
            System.out.println("   Código Autorización: " + codigoAuth);

        } else {
            // ❌ PAGO RECHAZADO
            String motivo = request.getMotivoFallo() != null
                    ? request.getMotivoFallo()
                    : "Pago rechazado por la pasarela";

            pago.marcarRechazado(motivo);
            pedido.marcarPagoFallido(motivo);

            System.out.println("❌ Pago rechazado: " + motivo);
        }

        // 6. ⚠️ CRÍTICO: Persistir cambios
        Pago pagoGuardado = pagoRepository.guardar(pago);
        pedidoRepository.guardar(pedido);

        // 7. Verificar que se guardó
        System.out.println("💾 Pago guardado en BD:");
        System.out.println("   ID: " + pagoGuardado.getId());
        System.out.println("   Estado: " + pagoGuardado.getEstado());
        System.out.println("   Código Auth: " + pagoGuardado.getCodigoAutorizacion());
    }

    private void validarRequest(ConfirmarPagoRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }
        if (request.getReferenciaPago() == null || request.getReferenciaPago().isBlank()) {
            throw new IllegalArgumentException("La referencia de pago es requerida");
        }
        if (request.getEstadoPago() == null || request.getEstadoPago().isBlank()) {
            throw new IllegalArgumentException("El estado del pago es requerido");
        }
    }
}
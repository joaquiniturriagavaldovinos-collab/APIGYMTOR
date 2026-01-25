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

        System.out.println("\n========================================");
        System.out.println("🚀 INICIANDO CONFIRMACIÓN DE PAGO");
        System.out.println("========================================");

        // 1. Validar request
        validarRequest(request);

        // 2. Buscar pago por referencia de pasarela
        System.out.println("🔍 Buscando pago con referencia: " + request.getReferenciaPago());
        Pago pago = pagoRepository.buscarPorReferenciaPasarela(request.getReferenciaPago())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el pago con referencia: " + request.getReferenciaPago()
                ));

        System.out.println("✅ Pago encontrado:");
        System.out.println("   ID: " + pago.getId());
        System.out.println("   Estado actual: " + pago.getEstado());
        System.out.println("   Código actual: " + pago.getCodigoAutorizacion());

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
            System.out.println("\n✅ PROCESANDO PAGO EXITOSO");

            // Generar código de autorización si no viene
            String codigoAuth = request.getCodigoAutorizacion();
            if (codigoAuth == null || codigoAuth.isBlank() || "null".equals(codigoAuth)) {
                codigoAuth = "AUTH-" + System.currentTimeMillis();
                System.out.println("🔧 Código generado: " + codigoAuth);
            } else {
                System.out.println("📥 Código recibido: " + codigoAuth);
            }

            System.out.println("\n📝 ANTES de confirmarExitoso():");
            System.out.println("   pago.getEstado() = " + pago.getEstado());
            System.out.println("   pago.getCodigoAutorizacion() = " + pago.getCodigoAutorizacion());

            // ⚠️ ESTE ES EL MÉTODO CRÍTICO
            pago.confirmarExitoso(codigoAuth);

            System.out.println("\n📝 DESPUÉS de confirmarExitoso():");
            System.out.println("   pago.getEstado() = " + pago.getEstado());
            System.out.println("   pago.getCodigoAutorizacion() = " + pago.getCodigoAutorizacion());

            pedido.confirmarPago(request.getReferenciaPago());

        } else {
            // ❌ PAGO RECHAZADO
            System.out.println("\n❌ PROCESANDO PAGO RECHAZADO");
            String motivo = request.getMotivoFallo() != null
                    ? request.getMotivoFallo()
                    : "Pago rechazado por la pasarela";

            pago.marcarRechazado(motivo);
            pedido.marcarPagoFallido(motivo);

            System.out.println("   Motivo: " + motivo);
        }

        // 6. ⚠️ CRÍTICO: Persistir cambios
        System.out.println("\n💾 GUARDANDO CAMBIOS EN BD...");
        System.out.println("   Código ANTES de guardar: " + pago.getCodigoAutorizacion());

        Pago pagoGuardado = pagoRepository.guardar(pago);

        System.out.println("   Código DESPUÉS de guardar (retornado): " + pagoGuardado.getCodigoAutorizacion());

        // Verificación adicional: consultar de nuevo
        System.out.println("\n🔍 VERIFICACIÓN: Consultando pago recién guardado...");
        pagoRepository.buscarPorId(pagoGuardado.getId()).ifPresent(pagoVerificado -> {
            System.out.println("   ID verificado: " + pagoVerificado.getId());
            System.out.println("   Estado verificado: " + pagoVerificado.getEstado());
            System.out.println("   Código verificado: " + pagoVerificado.getCodigoAutorizacion());
        });

        pedidoRepository.guardar(pedido);

        System.out.println("\n========================================");
        System.out.println("✅ CONFIRMACIÓN COMPLETADA");
        System.out.println("========================================\n");
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
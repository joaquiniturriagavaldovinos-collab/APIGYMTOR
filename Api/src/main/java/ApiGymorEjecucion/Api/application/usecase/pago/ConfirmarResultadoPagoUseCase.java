package ApiGymorEjecucion.Api.application.usecase.pago;

import ApiGymorEjecucion.Api.application.dto.request.pago.ConfirmarPagoRequest;
import ApiGymorEjecucion.Api.application.usecase.despacho.CrearDespachoUseCase;
import ApiGymorEjecucion.Api.domain.model.Despacho.Despacho;
import ApiGymorEjecucion.Api.domain.model.Pago.Pago;
import ApiGymorEjecucion.Api.domain.model.pedido.Pedido;
import ApiGymorEjecucion.Api.domain.repository.DespachoRepository;
import ApiGymorEjecucion.Api.domain.repository.PagoRepository;
import ApiGymorEjecucion.Api.domain.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfirmarResultadoPagoUseCase {

    private final PagoRepository pagoRepository;
    private final PedidoRepository pedidoRepository;
    private final DespachoRepository despachoRepository;
    private final CrearDespachoUseCase crearDespachoUseCase;

    public ConfirmarResultadoPagoUseCase(
            PagoRepository pagoRepository,
            PedidoRepository pedidoRepository,
            DespachoRepository despachoRepository,
            CrearDespachoUseCase crearDespachoUseCase
    ) {
        this.pagoRepository = pagoRepository;
        this.pedidoRepository = pedidoRepository;
        this.despachoRepository = despachoRepository;
        this.crearDespachoUseCase = crearDespachoUseCase;
    }

    @Transactional
    public void ejecutar(ConfirmarPagoRequest request) {

        System.out.println("\n========================================");
        System.out.println("🚀 INICIANDO CONFIRMACIÓN DE PAGO");
        System.out.println("========================================");

        validarRequest(request);

        // 1. Buscar pago
        System.out.println("🔍 Buscando pago con referencia: " + request.getReferenciaPago());
        Pago pago = pagoRepository.buscarPorReferenciaPasarela(request.getReferenciaPago())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el pago con referencia: " + request.getReferenciaPago()
                ));
        System.out.println("✅ Pago encontrado: " + pago.getId());

        // 2. Idempotencia
        if (pago.estaFinalizado()) {
            System.out.println("⚠️ Pago ya procesado (webhook duplicado). Ignorando...");
            return;
        }

        // 3. Buscar pedido
        Pedido pedido = pedidoRepository.buscarPorId(pago.getPedidoId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el pedido: " + pago.getPedidoId()
                ));

        // 4. Procesar según resultado
        if (request.isExitoso()) {
            System.out.println("\n✅ PROCESANDO PAGO EXITOSO");

            // Generar código de autorización
            String codigoAuth = request.getCodigoAutorizacion();
            if (codigoAuth == null || codigoAuth.isBlank() || "null".equals(codigoAuth)) {
                codigoAuth = "AUTH-" + System.currentTimeMillis();
                System.out.println("🔧 Código generado: " + codigoAuth);
            } else {
                System.out.println("📥 Código recibido: " + codigoAuth);
            }

            // Confirmar pago y pedido
            pago.confirmarExitoso(codigoAuth);
            pedido.confirmarPago(request.getReferenciaPago());

            // ✅ CREAR DESPACHO
            System.out.println("\n========================================");
            System.out.println("📦 INICIANDO CREACIÓN DE DESPACHO");
            System.out.println("========================================");

            try {
                String direccionCompleta = obtenerDireccionCliente(pedido.getClienteId());

                Despacho despacho = crearDespachoUseCase.ejecutar(
                        pedido.getId(),
                        direccionCompleta
                );

                System.out.println("✅ Despacho creado: " + despacho.getId());
                System.out.println("✅ Guía generada: " + despacho.getGuiaDespacho().getNumero());

                // ⚠️ CRÍTICO: Asignar despacho ANTES de guardar
                if (despacho.getGuiaDespacho() != null) {
                    pedido.asignarDespacho(
                            despacho.getId(),
                            despacho.getGuiaDespacho().getNumero()
                    );
                    System.out.println("✅ Despacho asignado al pedido:");
                    System.out.println("   Despacho ID: " + despacho.getId());
                    System.out.println("   Guía: " + despacho.getGuiaDespacho().getNumero());
                }

            } catch (Exception e) {
                System.out.println("❌ ERROR CRÍTICO al crear despacho:");
                System.out.println("   Mensaje: " + e.getMessage());
                e.printStackTrace();
                // NO fallar el pago si hay error en despacho
            }

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

        // ⚠️ CRÍTICO: Guardar TODO al final
        System.out.println("\n💾 GUARDANDO CAMBIOS EN BD...");

        Pago pagoGuardado = pagoRepository.guardar(pago);
        System.out.println("✅ Pago guardado:");
        System.out.println("   Estado: " + pagoGuardado.getEstado());
        System.out.println("   Código: " + pagoGuardado.getCodigoAutorizacion());

        Pedido pedidoGuardado = pedidoRepository.guardar(pedido);
        System.out.println("✅ Pedido guardado:");
        System.out.println("   Estado: " + pedidoGuardado.getEstado());
        System.out.println("   Despacho ID: " + pedidoGuardado.getDespachoId());  // ← AGREGAR
        System.out.println("   Guía: " + pedidoGuardado.getGuiaDespacho());

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

    private String obtenerDireccionCliente(String clienteId) {
        // TODO: Implementar obtención de dirección desde repositorio de Cliente
        // Por ahora retornamos un placeholder
        return "Av. Providencia 1234, Providencia, Santiago, Chile";
    }
}
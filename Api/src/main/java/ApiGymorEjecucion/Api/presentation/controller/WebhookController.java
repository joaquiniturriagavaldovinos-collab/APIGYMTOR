package ApiGymorEjecucion.Api.presentation.controller;

import ApiGymorEjecucion.Api.application.dto.request.pago.ConfirmarPagoRequest;
import ApiGymorEjecucion.Api.application.usecase.pago.ConfirmarResultadoPagoUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para recibir webhooks/callbacks de pasarelas de pago
 *
 * IMPORTANTE: Este endpoint es público (sin autenticación)
 * porque es llamado por servicios externos (Webpay, MercadoPago, etc.)
 */
@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {

    private final ConfirmarResultadoPagoUseCase confirmarResultadoPagoUseCase;

    public WebhookController(ConfirmarResultadoPagoUseCase confirmarResultadoPagoUseCase) {
        this.confirmarResultadoPagoUseCase = confirmarResultadoPagoUseCase;
    }

    /**
     * Webhook para confirmación de pago desde pasarelas externas
     *
     * Este endpoint es llamado automáticamente por:
     * - Webpay (Transbank)
     * - MercadoPago
     * - Otras pasarelas
     *
     * Request esperado:
     * {
     *   "referencia": "REF-1769256000000-abc12345",
     *   "estadoPago": "APROBADO",  // o "RECHAZADO"
     *   "codigoAutorizacion": "AUTH-123456" // solo si APROBADO
     * }
     */
    @PostMapping("/pago")
    public ResponseEntity<String> confirmarPago(
            @Valid @RequestBody ConfirmarPagoRequest request) {

        try {
            confirmarResultadoPagoUseCase.ejecutar(request);
            return ResponseEntity.ok("Pago procesado correctamente");

        } catch (IllegalArgumentException e) {
            // Pago no encontrado o datos inválidos
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {
            // Error interno
            return ResponseEntity.status(500).body("Error al procesar el pago");
        }
    }
}
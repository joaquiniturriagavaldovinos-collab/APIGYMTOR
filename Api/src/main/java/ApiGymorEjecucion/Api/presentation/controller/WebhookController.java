package ApiGymorEjecucion.Api.presentation.controller;

import ApiGymorEjecucion.Api.application.dto.request.pago.ConfirmarPagoRequest;
import ApiGymorEjecucion.Api.application.usecase.pago.ConfirmarResultadoPagoUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para recibir webhooks/callbacks de pasarelas de pago
 *
 * ⚠️ IMPORTANTE: Este endpoint es PÚBLICO (sin autenticación)
 * porque es llamado por servicios externos (Webpay, MercadoPago, etc.)
 *
 * En producción, validar la firma del webhook para seguridad.
 */
@Tag(name = "Webhooks", description = "Endpoints públicos para callbacks de servicios externos")
@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {

    private static final Logger log = LoggerFactory.getLogger(WebhookController.class);

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
     * - Otras pasarelas de pago
     *
     * Request esperado:
     * {
     *   "referenciaPago": "REF-1769256000000-abc12345",
     *   "estadoPago": "APROBADO",  // o "RECHAZADO"
     *   "codigoAutorizacion": "AUTH-123456" // solo si APROBADO
     * }
     */
    @Operation(
            summary = "Webhook de confirmación de pago",
            description = "Recibe la confirmación de pago desde la pasarela externa (Webpay, MercadoPago, etc.)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pago procesado correctamente", content = @Content),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en el webhook", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error al procesar el webhook", content = @Content)
    })
    @PostMapping("/pago")
    public ResponseEntity<String> confirmarPago(
            @Valid @RequestBody ConfirmarPagoRequest request) {

        log.info("🌐 WEBHOOK RECIBIDO - Referencia: {}, Estado: {}, Exitoso: {}",
                request.getReferenciaPago(),
                request.getEstadoPago(),
                request.isExitoso());

        try {
            confirmarResultadoPagoUseCase.ejecutar(request);

            log.info("✅ Webhook procesado correctamente - Referencia: {}",
                    request.getReferenciaPago());

            return ResponseEntity.ok("Pago procesado correctamente");

        } catch (IllegalArgumentException e) {
            log.warn("⚠️ Error de validación en webhook - Referencia: {}, Error: {}",
                    request.getReferenciaPago(), e.getMessage());

            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {
            log.error("❌ Error interno en webhook - Referencia: {}, Error: {}",
                    request.getReferenciaPago(), e.getMessage(), e);

            return ResponseEntity.status(500).body("Error al procesar el pago");
        }
    }
}
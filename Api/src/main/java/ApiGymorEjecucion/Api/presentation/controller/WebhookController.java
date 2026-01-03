package ApiGymorEjecucion.Api.presentation.controller;


import ApiGymorEjecucion.Api.application.dto.request.ConfirmarPagoRequest;
import ApiGymorEjecucion.Api.application.dto.response.PedidoResponse;
import ApiGymorEjecucion.Api.application.usecase.pedido.ConfirmarResultadoPagoUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para recibir webhooks de sistemas externos.
 *
 * Endpoints especiales que no requieren autenticación del usuario
 * pero deben validar la firma/token del proveedor externo.
 */
@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {

    private final ConfirmarResultadoPagoUseCase confirmarResultadoPagoUseCase;

    public WebhookController(ConfirmarResultadoPagoUseCase confirmarResultadoPagoUseCase) {
        this.confirmarResultadoPagoUseCase = confirmarResultadoPagoUseCase;
    }

    /**
     * CU3: Confirmar Resultado de Pago (Webhook de Pasarela)
     * POST /api/webhooks/pago
     *
     * Este endpoint es llamado por la pasarela de pago externa
     * cuando se completa una transacción (exitosa o fallida).
     *
     * @param request Datos de confirmación del pago
     * @return Confirmación de procesamiento
     */
    @PostMapping("/pago")
    public ResponseEntity<WebhookResponse> confirmarPago(
            @RequestBody ConfirmarPagoRequest request,
            @RequestHeader(value = "X-Webhook-Signature", required = false) String signature) {

        // TODO: Validar firma del webhook para seguridad
        // if (!validarFirmaWebhook(request, signature)) {
        //     return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        // }

        // Procesar el resultado del pago
        PedidoResponse pedido = confirmarResultadoPagoUseCase.ejecutar(request);

        // Responder al webhook (la pasarela espera confirmación)
        WebhookResponse response = new WebhookResponse(
                "PROCESSED",
                "Pago procesado correctamente",
                pedido.getId()
        );

        return ResponseEntity.ok(response);
    }

    /**
     * DTO de respuesta para webhooks
     */
    public static class WebhookResponse {
        private String status;
        private String message;
        private String pedidoId;

        public WebhookResponse(String status, String message, String pedidoId) {
            this.status = status;
            this.message = message;
            this.pedidoId = pedidoId;
        }

        // Getters
        public String getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public String getPedidoId() {
            return pedidoId;
        }
    }
}
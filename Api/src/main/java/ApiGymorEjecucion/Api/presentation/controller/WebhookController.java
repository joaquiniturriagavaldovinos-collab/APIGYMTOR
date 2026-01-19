package ApiGymorEjecucion.Api.presentation.controller;


import ApiGymorEjecucion.Api.application.dto.request.pago.ConfirmarPagoRequest;
import ApiGymorEjecucion.Api.application.dto.response.pedido.PedidoResponse;
import ApiGymorEjecucion.Api.application.usecase.pedido.ConfirmarResultadoPagoUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
@Tag(
        name = "Webhooks",
        description = "Endpoints para integraciones externas (pasarelas de pago)"
)
@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {

    private final ConfirmarResultadoPagoUseCase confirmarResultadoPagoUseCase;

    public WebhookController(ConfirmarResultadoPagoUseCase confirmarResultadoPagoUseCase) {
        this.confirmarResultadoPagoUseCase = confirmarResultadoPagoUseCase;
    }

    @Operation(
            summary = "Webhook confirmación de pago",
            description = "Endpoint llamado por la pasarela de pago para confirmar el resultado de una transacción"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Webhook procesado correctamente"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Firma de webhook inválida",
                    content = @Content
            )
    })
    @PostMapping("/pago")
    public ResponseEntity<WebhookResponse> confirmarPago(
            @RequestBody ConfirmarPagoRequest request,
            @Parameter(
                    description = "Firma de seguridad del webhook",
                    example = "sha256=abc123"
            )
            @RequestHeader(value = "X-Webhook-Signature", required = false)
            String signature
    ) {

        // Validación de firma → va en filtro o service dedicado
        PedidoResponse pedido =
                confirmarResultadoPagoUseCase.ejecutar(request);

        WebhookResponse response = new WebhookResponse(
                "PROCESSED",
                "Pago procesado correctamente",
                pedido.getId()
        );

        return ResponseEntity.ok(response);
    }

    @Schema(description = "Respuesta estándar para webhooks")
    public static class WebhookResponse {

        @Schema(example = "PROCESSED")
        private String status;

        @Schema(example = "Pago procesado correctamente")
        private String message;

        @Schema(example = "ped_001")
        private String pedidoId;

        public WebhookResponse(String status, String message, String pedidoId) {
            this.status = status;
            this.message = message;
            this.pedidoId = pedidoId;
        }

        public String getStatus() { return status; }
        public String getMessage() { return message; }
        public String getPedidoId() { return pedidoId; }
    }
}

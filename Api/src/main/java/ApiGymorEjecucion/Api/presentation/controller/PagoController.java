package ApiGymorEjecucion.Api.presentation.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controller REST para operaciones de Pagos
 */
@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    /**
     * Consultar pagos de un pedido
     * GET /api/pagos/pedido/{pedidoId}
     */
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<PagoResponse>> buscarPagosPorPedido(
            @PathVariable String pedidoId) {

        // TODO: Implementar ConsultarPagosPorPedidoUseCase
        return ResponseEntity.ok().build();
    }

    /**
     * Consultar pago por ID
     * GET /api/pagos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<PagoResponse> buscarPorId(@PathVariable String id) {
        // TODO: Implementar ConsultarPagoUseCase
        return ResponseEntity.ok().build();
    }

    /**
     * Consultar pago por referencia de pasarela
     * GET /api/pagos/referencia/{referencia}
     */
    @GetMapping("/referencia/{referencia}")
    public ResponseEntity<PagoResponse> buscarPorReferencia(
            @PathVariable String referencia) {

        // TODO: Implementar BuscarPagoPorReferenciaUseCase
        return ResponseEntity.ok().build();
    }

    /**
     * Procesar reembolso
     * POST /api/pagos/{id}/reembolsar
     */
    @PostMapping("/{id}/reembolsar")
    public ResponseEntity<PagoResponse> procesarReembolso(
            @PathVariable String id,
            @RequestBody ReembolsoRequest request) {

        // TODO: Implementar ProcesarReembolsoUseCase
        return ResponseEntity.ok().build();
    }

    /**
     * Cancelar pago pendiente
     * POST /api/pagos/{id}/cancelar
     */
    @PostMapping("/{id}/cancelar")
    public ResponseEntity<PagoResponse> cancelarPago(
            @PathVariable String id,
            @RequestParam String motivo) {

        // TODO: Implementar CancelarPagoUseCase
        return ResponseEntity.ok().build();
    }

    // DTOs
    public static class PagoResponse {
        private String id;
        private String pedidoId;
        private BigDecimal monto;
        private String metodoPago;
        private String estado;
        private String referenciaPasarela;
        private String codigoAutorizacion;
        private String motivoRechazo;
        // getters/setters
    }

    public static class ReembolsoRequest {
        private BigDecimal monto;
        private String motivo;
        // getters/setters
    }
}
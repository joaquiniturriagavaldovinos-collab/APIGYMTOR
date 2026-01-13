package ApiGymorEjecucion.Api.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller REST para operaciones de Despachos
 */
@RestController
@RequestMapping("/api/despachos")
public class DespachoController {

    /**
     * Consultar despacho de un pedido
     * GET /api/despachos/pedido/{pedidoId}
     */
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<DespachoResponse> buscarPorPedido(
            @PathVariable String pedidoId) {

        // TODO: Implementar ConsultarDespachoPorPedidoUseCase
        return ResponseEntity.ok().build();
    }

    /**
     * Consultar despacho por guía
     * GET /api/despachos/guia/{numeroGuia}
     */
    @GetMapping("/guia/{numeroGuia}")
    public ResponseEntity<DespachoResponse> buscarPorGuia(
            @PathVariable String numeroGuia) {

        // TODO: Implementar ConsultarDespachoPorGuiaUseCase
        return ResponseEntity.ok().build();
    }

    /**
     * Listar despachos pendientes
     * GET /api/despachos/pendientes
     */
    @GetMapping("/pendientes")
    public ResponseEntity<List<DespachoResponse>> listarPendientes() {
        // TODO: Implementar ListarDespachosPendientesUseCase
        return ResponseEntity.ok().build();
    }

    /**
     * Listar despachos en tránsito
     * GET /api/despachos/en-transito
     */
    @GetMapping("/en-transito")
    public ResponseEntity<List<DespachoResponse>> listarEnTransito() {
        // TODO: Implementar ListarDespachosEnTransitoUseCase
        return ResponseEntity.ok().build();
    }

    /**
     * Actualizar estado de despacho (integración con transportista)
     * POST /api/despachos/{id}/actualizar-estado
     */
    @PostMapping("/{id}/actualizar-estado")
    public ResponseEntity<DespachoResponse> actualizarEstado(
            @PathVariable String id,
            @RequestBody ActualizarEstadoRequest request) {

        // TODO: Implementar ActualizarEstadoDespachoUseCase
        return ResponseEntity.ok().build();
    }

    // DTOs
    public static class DespachoResponse {
        private String id;
        private String pedidoId;
        private String numeroGuia;
        private String urlTracking;
        private String transportista;
        private String direccionCompleta;
        private LocalDateTime fechaDespacho;
        private LocalDateTime fechaEntregaEstimada;
        private LocalDateTime fechaEntregaReal;
        private String estado;
        // getters/setters
    }

    public static class ActualizarEstadoRequest {
        private String estado;
        private String observaciones;
        private LocalDateTime fechaActualizacion;
        // getters/setters
    }
}
package ApiGymorEjecucion.Api.presentation.controller;

import ApiGymorEjecucion.Api.application.dto.request.despacho.ActualizarEstadoRequest;
import ApiGymorEjecucion.Api.application.dto.response.despacho.DespachoResponse;
import ApiGymorEjecucion.Api.application.dto.response.despacho.TrackingResponse;
import ApiGymorEjecucion.Api.application.usecase.despacho.ActualizarEstadoDespachoUseCase;
import ApiGymorEjecucion.Api.application.usecase.despacho.ConsultarDespachoUseCase;
import ApiGymorEjecucion.Api.application.usecase.despacho.ObtenerTrackingUseCase;
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

    private final ConsultarDespachoUseCase consultarDespachoUseCase;
    private final ActualizarEstadoDespachoUseCase actualizarEstadoDespachoUseCase;
    private final ObtenerTrackingUseCase obtenerTrackingUseCase;

    public DespachoController(
            ConsultarDespachoUseCase consultarDespachoUseCase,
            ActualizarEstadoDespachoUseCase actualizarEstadoDespachoUseCase,
            ObtenerTrackingUseCase obtenerTrackingUseCase
    ) {
        this.consultarDespachoUseCase = consultarDespachoUseCase;
        this.actualizarEstadoDespachoUseCase = actualizarEstadoDespachoUseCase;
        this.obtenerTrackingUseCase = obtenerTrackingUseCase;
    }

    // 1️⃣ Consultar despacho por ID
    @GetMapping("/{despachoId}")
    public ResponseEntity<DespachoResponse> consultarPorId(
            @PathVariable String despachoId
    ) {
        DespachoResponse response =
                consultarDespachoUseCase.buscarPorId(despachoId);

        return ResponseEntity.ok(response);
    }

    // 2️⃣ Consultar despacho por pedido
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<DespachoResponse> consultarPorPedido(
            @PathVariable String pedidoId
    ) {
        DespachoResponse response =
                consultarDespachoUseCase.buscarPorPedido(pedidoId);

        return ResponseEntity.ok(response);
    }

    // 3️⃣ Consultar despacho por número de guía
    @GetMapping("/guia/{numeroGuia}")
    public ResponseEntity<DespachoResponse> consultarPorGuia(
            @PathVariable String numeroGuia
    ) {
        DespachoResponse response =
                consultarDespachoUseCase.buscarPorGuia(numeroGuia);

        return ResponseEntity.ok(response);
    }

    // 4️⃣ Actualizar estado del despacho
    @PatchMapping("/{despachoId}")
    public ResponseEntity<DespachoResponse> actualizarEstado(
            @PathVariable String despachoId,
            @RequestBody ActualizarEstadoRequest request
    ) {
        DespachoResponse response =
                actualizarEstadoDespachoUseCase.ejecutar(despachoId, request);

        return ResponseEntity.ok(response);
    }

    // 5️⃣ Obtener tracking del despacho (por pedido)
    @GetMapping("/pedido/{pedidoId}/tracking")
    public ResponseEntity<TrackingResponse> obtenerTracking(
            @PathVariable String pedidoId
    ) {
        TrackingResponse response =
                obtenerTrackingUseCase.ejecutar(pedidoId);

        return ResponseEntity.ok(response);
    }
}

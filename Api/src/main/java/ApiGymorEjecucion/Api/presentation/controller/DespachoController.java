package ApiGymorEjecucion.Api.presentation.controller;

import ApiGymorEjecucion.Api.application.dto.request.despacho.ActualizarEstadoRequest;
import ApiGymorEjecucion.Api.application.dto.response.despacho.DespachoResponse;
import ApiGymorEjecucion.Api.application.dto.response.despacho.TrackingResponse;
import ApiGymorEjecucion.Api.application.usecase.despacho.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Despachos",
        description = "Operaciones relacionadas con la gestión y seguimiento de despachos"
)
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

    // -------------------------------------------------
    // CONSULTAR DESPACHO POR ID
    // -------------------------------------------------
    @Operation(
            summary = "Consultar despacho por ID",
            description = "Obtiene la información completa de un despacho usando su identificador"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Despacho encontrado",
                    content = @Content(schema = @Schema(implementation = DespachoResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Despacho no encontrado", content = @Content)
    })
    @GetMapping("/{despachoId}")
    public ResponseEntity<DespachoResponse> consultarPorId(
            @Parameter(description = "ID del despacho", example = "desp_123")
            @PathVariable String despachoId
    ) {
        return ResponseEntity.ok(
                consultarDespachoUseCase.buscarPorId(despachoId)
        );
    }

    // -------------------------------------------------
    // CONSULTAR DESPACHO POR PEDIDO
    // -------------------------------------------------
    @Operation(
            summary = "Consultar despacho por pedido",
            description = "Obtiene el despacho asociado a un pedido específico"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Despacho encontrado",
                    content = @Content(schema = @Schema(implementation = DespachoResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Despacho no encontrado", content = @Content)
    })
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<DespachoResponse> consultarPorPedido(
            @Parameter(description = "ID del pedido", example = "ped_456")
            @PathVariable String pedidoId
    ) {
        return ResponseEntity.ok(
                consultarDespachoUseCase.buscarPorPedido(pedidoId)
        );
    }

    // -------------------------------------------------
    // CONSULTAR DESPACHO POR NÚMERO DE GUÍA
    // -------------------------------------------------
    @Operation(
            summary = "Consultar despacho por número de guía",
            description = "Obtiene un despacho utilizando el número de guía de transporte"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Despacho encontrado",
                    content = @Content(schema = @Schema(implementation = DespachoResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Despacho no encontrado", content = @Content)
    })
    @GetMapping("/guia/{numeroGuia}")
    public ResponseEntity<DespachoResponse> consultarPorGuia(
            @Parameter(description = "Número de guía", example = "GUIA-789456")
            @PathVariable String numeroGuia
    ) {
        return ResponseEntity.ok(
                consultarDespachoUseCase.buscarPorGuia(numeroGuia)
        );
    }

    // -------------------------------------------------
    // ACTUALIZAR ESTADO DEL DESPACHO
    // -------------------------------------------------
    @Operation(
            summary = "Actualizar estado del despacho",
            description = "Actualiza el estado actual del despacho (ej: EN_PREPARACION, EN_RUTA, ENTREGADO)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Estado del despacho actualizado",
                    content = @Content(schema = @Schema(implementation = DespachoResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Estado inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Despacho no encontrado", content = @Content)
    })
    @PatchMapping("/{despachoId}")
    public ResponseEntity<DespachoResponse> actualizarEstado(
            @Parameter(description = "ID del despacho", example = "desp_123")
            @PathVariable String despachoId,
            @RequestBody ActualizarEstadoRequest request
    ) {
        return ResponseEntity.ok(
                actualizarEstadoDespachoUseCase.ejecutar(despachoId, request)
        );
    }

    // -------------------------------------------------
    // OBTENER TRACKING DEL DESPACHO
    // -------------------------------------------------
    @Operation(
            summary = "Obtener tracking del despacho",
            description = "Obtiene el historial de seguimiento de un despacho asociado a un pedido"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tracking obtenido correctamente",
                    content = @Content(schema = @Schema(implementation = TrackingResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Tracking no encontrado", content = @Content)
    })
    @GetMapping("/pedido/{pedidoId}/tracking")
    public ResponseEntity<TrackingResponse> obtenerTracking(
            @Parameter(description = "ID del pedido", example = "ped_456")
            @PathVariable String pedidoId
    ) {
        return ResponseEntity.ok(
                obtenerTrackingUseCase.ejecutar(pedidoId)
        );
    }
}

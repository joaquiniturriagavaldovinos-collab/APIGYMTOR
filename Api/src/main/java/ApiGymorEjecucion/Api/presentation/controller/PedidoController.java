package ApiGymorEjecucion.Api.presentation.controller;

import ApiGymorEjecucion.Api.application.dto.request.pago.ConfirmarPagoRequest;
import ApiGymorEjecucion.Api.application.dto.request.pedido.*;
import ApiGymorEjecucion.Api.application.dto.response.pedido.IniciarPagoResponse;
import ApiGymorEjecucion.Api.application.dto.response.pedido.PedidoResponse;
import ApiGymorEjecucion.Api.application.usecase.pedido.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Pedidos",
        description = "Gestión del ciclo de vida completo de los pedidos"
)
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final CrearPedidoUseCase crearPedidoUseCase;
    private final IniciarPagoPedidoUseCase iniciarPagoPedidoUseCase;
    private final PrepararPedidoUseCase prepararPedidoUseCase;
    private final DespacharPedidoUseCase despacharPedidoUseCase;
    private final ConfirmarEntregaUseCase confirmarEntregaUseCase;
    private final CancelarPedidoUseCase cancelarPedidoUseCase;
    private final ConsultarEstadoPedidoUseCase consultarEstadoPedidoUseCase;

    public PedidoController(
            CrearPedidoUseCase crearPedidoUseCase,
            IniciarPagoPedidoUseCase iniciarPagoPedidoUseCase,
            PrepararPedidoUseCase prepararPedidoUseCase,
            DespacharPedidoUseCase despacharPedidoUseCase,
            ConfirmarEntregaUseCase confirmarEntregaUseCase,
            CancelarPedidoUseCase cancelarPedidoUseCase,
            ConsultarEstadoPedidoUseCase consultarEstadoPedidoUseCase
    ) {
        this.crearPedidoUseCase = crearPedidoUseCase;
        this.iniciarPagoPedidoUseCase = iniciarPagoPedidoUseCase;
        this.prepararPedidoUseCase = prepararPedidoUseCase;
        this.despacharPedidoUseCase = despacharPedidoUseCase;
        this.confirmarEntregaUseCase = confirmarEntregaUseCase;
        this.cancelarPedidoUseCase = cancelarPedidoUseCase;
        this.consultarEstadoPedidoUseCase = consultarEstadoPedidoUseCase;
    }

    // -------------------------------------------------
    // CREAR PEDIDO
    // -------------------------------------------------
    @Operation(
            summary = "Crear pedido",
            description = "Crea un nuevo pedido en estado inicial"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Pedido creado correctamente",
                    content = @Content(schema = @Schema(implementation = PedidoResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Datos del pedido inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<PedidoResponse> crearPedido(
            @RequestBody CrearPedidoRequest request
    ) {
        PedidoResponse response = crearPedidoUseCase.ejecutar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // -------------------------------------------------
    // CONSULTAR ESTADO DEL PEDIDO
    // -------------------------------------------------
    @Operation(
            summary = "Consultar pedido",
            description = "Obtiene el estado actual y detalle de un pedido"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedido encontrado",
                    content = @Content(schema = @Schema(implementation = PedidoResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content)
    })
    @GetMapping("/{pedidoId}")
    public ResponseEntity<PedidoResponse> consultarPedido(
            @Parameter(description = "ID del pedido", example = "ped_123")
            @PathVariable String pedidoId
    ) {
        PedidoResponse response = consultarEstadoPedidoUseCase.ejecutar(pedidoId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/iniciar-pago")
    public ResponseEntity<IniciarPagoResponse> iniciarPago(
            @PathVariable String id,
            @RequestParam String metodoPago) {

        IniciarPagoResponse response = iniciarPagoPedidoUseCase.ejecutar(id, metodoPago);
        return ResponseEntity.ok(response);
    }


    // -------------------------------------------------
    // PREPARAR PEDIDO
    // -------------------------------------------------
    @Operation(
            summary = "Preparar pedido",
            description = "Marca un pedido como preparado para despacho"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedido preparado",
                    content = @Content(schema = @Schema(implementation = PedidoResponse.class))
            ),
            @ApiResponse(responseCode = "409", description = "Estado inválido", content = @Content)
    })
    @PostMapping("/{pedidoId}/preparacion")
    public ResponseEntity<PedidoResponse> prepararPedido(
            @Parameter(description = "ID del pedido", example = "ped_123")
            @PathVariable String pedidoId
    ) {
        PedidoResponse response = prepararPedidoUseCase.ejecutar(pedidoId);
        return ResponseEntity.ok(response);
    }

    // -------------------------------------------------
    // DESPACHAR PEDIDO
    // -------------------------------------------------
    @Operation(
            summary = "Despachar pedido",
            description = "Despacha un pedido previamente preparado"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedido despachado",
                    content = @Content(schema = @Schema(implementation = PedidoResponse.class))
            ),
            @ApiResponse(responseCode = "409", description = "Estado inválido", content = @Content)
    })
    @PostMapping("/despacho")
    public ResponseEntity<PedidoResponse> despacharPedido(
            @RequestBody DespacharPedidoRequest request
    ) {
        PedidoResponse response = despacharPedidoUseCase.ejecutar(request);
        return ResponseEntity.ok(response);
    }

    // -------------------------------------------------
    // CONFIRMAR ENTREGA
    // -------------------------------------------------
    @Operation(
            summary = "Confirmar entrega de pedido",
            description = "Confirma que el pedido fue entregado al cliente"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Entrega confirmada",
                    content = @Content(schema = @Schema(implementation = PedidoResponse.class))
            ),
            @ApiResponse(responseCode = "409", description = "Estado inválido", content = @Content)
    })
    @PostMapping("/{pedidoId}/entrega")
    public ResponseEntity<PedidoResponse> confirmarEntrega(
            @Parameter(description = "ID del pedido", example = "ped_123")
            @PathVariable String pedidoId
    ) {
        PedidoResponse response = confirmarEntregaUseCase.ejecutar(pedidoId);
        return ResponseEntity.ok(response);
    }

    // -------------------------------------------------
    // CANCELAR PEDIDO
    // -------------------------------------------------
    @Operation(
            summary = "Cancelar pedido",
            description = "Cancela un pedido indicando el motivo"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedido cancelado",
                    content = @Content(schema = @Schema(implementation = PedidoResponse.class))
            ),
            @ApiResponse(responseCode = "409", description = "No se puede cancelar el pedido", content = @Content)
    })
    @PostMapping("/{pedidoId}/cancelacion")
    public ResponseEntity<PedidoResponse> cancelarPedido(
            @Parameter(description = "ID del pedido", example = "ped_123")
            @PathVariable String pedidoId,
            @Parameter(description = "Motivo de cancelación", example = "Cliente solicitó anulación")
            @RequestParam String motivo
    ) {
        PedidoResponse response = cancelarPedidoUseCase.ejecutar(pedidoId, motivo);
        return ResponseEntity.ok(response);
    }
}

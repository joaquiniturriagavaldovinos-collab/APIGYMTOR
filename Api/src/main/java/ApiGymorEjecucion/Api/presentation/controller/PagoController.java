package ApiGymorEjecucion.Api.presentation.controller;

import ApiGymorEjecucion.Api.application.dto.request.pago.*;
import ApiGymorEjecucion.Api.application.dto.response.pago.PagoResponse;
import ApiGymorEjecucion.Api.application.dto.response.pedido.PedidoResponse;
import ApiGymorEjecucion.Api.application.usecase.pago.*;
import ApiGymorEjecucion.Api.application.usecase.pedido.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Pagos",
        description = "Operaciones relacionadas con pagos, confirmaciones y reembolsos"
)
@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final IniciarPagoPedidoUseCase iniciarPagoPedidoUseCase;
    private final ConfirmarResultadoPagoUseCase confirmarResultadoPagoUseCase;
    private final ConsultarPagosPorPedidoUseCase consultarPagosPorPedidoUseCase;
    private final ListarPagosUseCase listarPagosUseCase;
    private final ReembolsarPagoUseCase reembolsarPagoUseCase;

    public PagoController(
            IniciarPagoPedidoUseCase iniciarPagoPedidoUseCase,
            ConfirmarResultadoPagoUseCase confirmarResultadoPagoUseCase,
            ConsultarPagosPorPedidoUseCase consultarPagosPorPedidoUseCase,
            ListarPagosUseCase listarPagosUseCase,
            ReembolsarPagoUseCase reembolsarPagoUseCase
    ) {
        this.iniciarPagoPedidoUseCase = iniciarPagoPedidoUseCase;
        this.confirmarResultadoPagoUseCase = confirmarResultadoPagoUseCase;
        this.consultarPagosPorPedidoUseCase = consultarPagosPorPedidoUseCase;
        this.listarPagosUseCase = listarPagosUseCase;
        this.reembolsarPagoUseCase = reembolsarPagoUseCase;
    }

    // -------------------------------------------------
    // INICIAR PAGO
    // -------------------------------------------------
    @Operation(
            summary = "Iniciar pago de pedido",
            description = "Inicia el proceso de pago asociado a un pedido"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pago iniciado correctamente",
                    content = @Content(schema = @Schema(implementation = PedidoResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content)
    })
    @PostMapping("/iniciar")
    public ResponseEntity<PedidoResponse> iniciarPago(
            @RequestBody IniciarPagoRequest request
    ) {
        PedidoResponse response =
                iniciarPagoPedidoUseCase.ejecutar(request.getPedidoId());

        return ResponseEntity.ok(response);
    }

    // -------------------------------------------------
    // CONFIRMACIÓN DE PAGO (CALLBACK)
    // -------------------------------------------------
    @Operation(
            summary = "Confirmar resultado de pago",
            description = "Endpoint utilizado como callback por la pasarela de pagos"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pago confirmado correctamente",
                    content = @Content(schema = @Schema(implementation = PedidoResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Datos de confirmación inválidos", content = @Content)
    })
    @PostMapping("/confirmacion")
    public ResponseEntity<PedidoResponse> confirmarPago(
            @RequestBody ConfirmarPagoRequest request
    ) {
        PedidoResponse response =
                confirmarResultadoPagoUseCase.ejecutar(request);

        return ResponseEntity.ok(response);
    }

    // -------------------------------------------------
    // LISTAR PAGOS POR PEDIDO
    // -------------------------------------------------
    @Operation(
            summary = "Listar pagos por pedido",
            description = "Obtiene todos los pagos asociados a un pedido específico"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado de pagos",
                    content = @Content(schema = @Schema(implementation = PagoResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content)
    })
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<PagoResponse>> listarPagosPorPedido(
            @Parameter(description = "ID del pedido", example = "ped_123")
            @PathVariable String pedidoId
    ) {
        return ResponseEntity.ok(
                consultarPagosPorPedidoUseCase.ejecutar(pedidoId)
        );
    }

    // -------------------------------------------------
    // LISTAR TODOS LOS PAGOS
    // -------------------------------------------------
    @Operation(
            summary = "Listar pagos",
            description = "Obtiene todos los pagos. Permite filtrar opcionalmente por estado"
    )
    @ApiResponse(responseCode = "200", description = "Listado de pagos")
    @GetMapping
    public ResponseEntity<List<PagoResponse>> listarPagos(
            @Parameter(description = "Estado del pago", example = "EXITOSO")
            @RequestParam(required = false) String estado
    ) {
        List<PagoResponse> response =
                (estado == null)
                        ? listarPagosUseCase.listarTodos()
                        : listarPagosUseCase.listarPorEstado(estado);

        return ResponseEntity.ok(response);
    }

    // -------------------------------------------------
    // PAGOS EXITOSOS
    // -------------------------------------------------
    @Operation(
            summary = "Listar pagos exitosos",
            description = "Obtiene únicamente los pagos con estado exitoso"
    )
    @ApiResponse(responseCode = "200", description = "Listado de pagos exitosos")
    @GetMapping("/exitosos")
    public ResponseEntity<List<PagoResponse>> listarPagosExitosos() {
        return ResponseEntity.ok(
                listarPagosUseCase.listarExitosos()
        );
    }

    // -------------------------------------------------
    // PAGOS RECHAZADOS
    // -------------------------------------------------
    @Operation(
            summary = "Listar pagos rechazados",
            description = "Obtiene únicamente los pagos con estado rechazado"
    )
    @ApiResponse(responseCode = "200", description = "Listado de pagos rechazados")
    @GetMapping("/rechazados")
    public ResponseEntity<List<PagoResponse>> listarPagosRechazados() {
        return ResponseEntity.ok(
                listarPagosUseCase.listarRechazados()
        );
    }

    // -------------------------------------------------
    // REEMBOLSAR PAGO
    // -------------------------------------------------
    @Operation(
            summary = "Reembolsar pago",
            description = "Ejecuta el reembolso total o parcial de un pago"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pago reembolsado correctamente",
                    content = @Content(schema = @Schema(implementation = PagoResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "No se puede reembolsar el pago", content = @Content)
    })
    @PostMapping("/{pagoId}/reembolso")
    public ResponseEntity<PagoResponse> reembolsarPago(
            @Parameter(description = "ID del pago", example = "pag_789")
            @PathVariable String pagoId,
            @RequestBody ReembolsoRequest request
    ) {
        PagoResponse response =
                reembolsarPagoUseCase.ejecutar(pagoId, request);

        return ResponseEntity.ok(response);
    }
}

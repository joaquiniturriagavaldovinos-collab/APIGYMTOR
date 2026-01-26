package ApiGymorEjecucion.Api.presentation.controller;

import ApiGymorEjecucion.Api.application.dto.request.pago.*;
import ApiGymorEjecucion.Api.application.dto.response.pago.EstadoPagoSimpleResponse;
import ApiGymorEjecucion.Api.application.dto.response.pago.PagoResponse;
import ApiGymorEjecucion.Api.application.dto.response.pedido.PedidoResponse;
import ApiGymorEjecucion.Api.application.usecase.pago.*;
import ApiGymorEjecucion.Api.application.usecase.pedido.IniciarPagoPedidoUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

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
    private final ConsultarPagoPorIdUseCase consultarPagoPorIdUseCase;
    private final ReintentarPagoUseCase reintentarPagoUseCase;
    private final ConsultarEstadoPagoUseCase consultarEstadoPagoUseCase;



    public PagoController(
            IniciarPagoPedidoUseCase iniciarPagoPedidoUseCase,
            ConfirmarResultadoPagoUseCase confirmarResultadoPagoUseCase,
            ConsultarPagosPorPedidoUseCase consultarPagosPorPedidoUseCase,
            ListarPagosUseCase listarPagosUseCase,
            ReembolsarPagoUseCase reembolsarPagoUseCase,
            ConsultarPagoPorIdUseCase consultarPagoPorIdUseCase,
            ReintentarPagoUseCase reintentarPagoUseCase,
            ConsultarEstadoPagoUseCase consultarEstadoPagoUseCase
    ) {
        this.iniciarPagoPedidoUseCase = iniciarPagoPedidoUseCase;
        this.confirmarResultadoPagoUseCase = confirmarResultadoPagoUseCase;
        this.consultarPagosPorPedidoUseCase = consultarPagosPorPedidoUseCase;
        this.listarPagosUseCase = listarPagosUseCase;
        this.reembolsarPagoUseCase = reembolsarPagoUseCase;
        this.consultarPagoPorIdUseCase = consultarPagoPorIdUseCase;
        this.reintentarPagoUseCase = reintentarPagoUseCase;
        this.consultarEstadoPagoUseCase = consultarEstadoPagoUseCase;
    }


    @PostMapping("/confirmacion")
    public ResponseEntity<Void> confirmarPago(
            @RequestBody ConfirmarPagoRequest request
    ) {
        confirmarResultadoPagoUseCase.ejecutar(request);
        return ResponseEntity.ok().build();
    }


    // -----------------------------------------
    // LISTAR PAGOS POR PEDIDO
    // -----------------------------------------
    @Operation(summary = "Listar pagos por pedido", description = "Obtiene todos los pagos asociados a un pedido específico")
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<PagoResponse>> listarPagosPorPedido(
            @Parameter(description = "ID del pedido", example = "ped_123")
            @PathVariable String pedidoId
    ) {
        List<PagoResponse> pagos = consultarPagosPorPedidoUseCase.ejecutar(pedidoId);
        return ResponseEntity.ok(pagos);
    }

    // -----------------------------------------
    // LISTAR TODOS LOS PAGOS (opcional por estado)
    // -----------------------------------------
    @Operation(summary = "Listar pagos", description = "Obtiene todos los pagos, opcionalmente filtrando por estado")
    @GetMapping
    public ResponseEntity<List<PagoResponse>> listarPagos(
            @RequestParam(required = false) String estado
    ) {
        List<PagoResponse> pagos = (estado == null)
                ? listarPagosUseCase.listarTodos()
                : listarPagosUseCase.listarPorEstado(estado);
        return ResponseEntity.ok(pagos);
    }

    // -----------------------------------------
    // LISTAR PAGOS EXITOSOS
    // -----------------------------------------
    @Operation(summary = "Listar pagos exitosos", description = "Obtiene únicamente los pagos con estado EXITOSO")
    @GetMapping("/exitosos")
    public ResponseEntity<List<PagoResponse>> listarPagosExitosos() {
        return ResponseEntity.ok(listarPagosUseCase.listarExitosos());
    }

    // -----------------------------------------
    // LISTAR PAGOS RECHAZADOS
    // -----------------------------------------
    @Operation(summary = "Listar pagos rechazados", description = "Obtiene únicamente los pagos con estado RECHAZADO")
    @GetMapping("/rechazados")
    public ResponseEntity<List<PagoResponse>> listarPagosRechazados() {
        return ResponseEntity.ok(listarPagosUseCase.listarRechazados());
    }

    // -----------------------------------------
    // REEMBOLSAR PAGO
    // -----------------------------------------
    @Operation(summary = "Reembolsar pago", description = "Ejecuta el reembolso total o parcial de un pago")
    @PostMapping("/{pagoId}/reembolso")
    public ResponseEntity<PagoResponse> reembolsarPago(
            @PathVariable String pagoId,
            @RequestBody ReembolsoRequest request
    ) {
        PagoResponse response = reembolsarPagoUseCase.ejecutar(pagoId, request);
        return ResponseEntity.ok(response);
    }



    // Consultar un pago específico
    @GetMapping("/{pagoId}")
    public ResponseEntity<PagoResponse> consultarPago(@PathVariable String pagoId) {
        PagoResponse pago = consultarPagoPorIdUseCase.ejecutar(pagoId);
        return ResponseEntity.ok(pago);
    }

    // Reintentar pago fallido
    @PostMapping("/{pagoId}/reintentar")
    public ResponseEntity<PagoResponse> reintentarPago(@PathVariable String pagoId) {
        PagoResponse nuevoPago = reintentarPagoUseCase.ejecutar(pagoId);
        return ResponseEntity.ok(nuevoPago);
    }

    // Consultar solo el estado (para polling)
    @GetMapping("/{pagoId}/estado")
    public ResponseEntity<EstadoPagoSimpleResponse> consultarEstado(@PathVariable String pagoId) {
        return ResponseEntity.ok(consultarEstadoPagoUseCase.ejecutar(pagoId));
    }}

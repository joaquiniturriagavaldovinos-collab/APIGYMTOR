package ApiGymorEjecucion.Api.presentation.controller;
import ApiGymorEjecucion.Api.application.dto.request.pago.ConfirmarPagoRequest;
import ApiGymorEjecucion.Api.application.dto.request.pago.IniciarPagoRequest;
import ApiGymorEjecucion.Api.application.dto.request.pago.ReembolsoRequest;
import ApiGymorEjecucion.Api.application.dto.response.pago.PagoResponse;
import ApiGymorEjecucion.Api.application.dto.response.pedido.PedidoResponse;
import ApiGymorEjecucion.Api.application.usecase.pago.ConsultarPagosPorPedidoUseCase;
import ApiGymorEjecucion.Api.application.usecase.pago.ListarPagosUseCase;
import ApiGymorEjecucion.Api.application.usecase.pago.ReembolsarPagoUseCase;
import ApiGymorEjecucion.Api.application.usecase.pedido.ConfirmarResultadoPagoUseCase;
import ApiGymorEjecucion.Api.application.usecase.pedido.IniciarPagoPedidoUseCase;
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

    // 1️⃣ Iniciar pago de un pedido
    @PostMapping("/iniciar")
    public ResponseEntity<PedidoResponse> iniciarPago(
            @RequestBody IniciarPagoRequest request
    ) {
        PedidoResponse response =
                iniciarPagoPedidoUseCase.ejecutar(request.getPedidoId());

        return ResponseEntity.ok(response);
    }

    // 2️⃣ Confirmación de pago (callback pasarela)
    @PostMapping("/confirmacion")
    public ResponseEntity<PedidoResponse> confirmarPago(
            @RequestBody ConfirmarPagoRequest request
    ) {
        PedidoResponse response =
                confirmarResultadoPagoUseCase.ejecutar(request);

        return ResponseEntity.ok(response);
    }

    // 3️⃣ Listar pagos por pedido
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<PagoResponse>> listarPagosPorPedido(
            @PathVariable String pedidoId
    ) {
        List<PagoResponse> response =
                consultarPagosPorPedidoUseCase.ejecutar(pedidoId);

        return ResponseEntity.ok(response);
    }

    // 4️⃣ Listar todos los pagos
    @GetMapping
    public ResponseEntity<List<PagoResponse>> listarPagos(
            @RequestParam(required = false) String estado
    ) {
        List<PagoResponse> response;

        if (estado == null) {
            response = listarPagosUseCase.listarTodos();
        } else {
            response = listarPagosUseCase.listarPorEstado(estado);
        }

        return ResponseEntity.ok(response);
    }

    // 5️⃣ Listar solo pagos exitosos
    @GetMapping("/exitosos")
    public ResponseEntity<List<PagoResponse>> listarPagosExitosos() {
        return ResponseEntity.ok(
                listarPagosUseCase.listarExitosos()
        );
    }

    // 6️⃣ Listar pagos rechazados
    @GetMapping("/rechazados")
    public ResponseEntity<List<PagoResponse>> listarPagosRechazados() {
        return ResponseEntity.ok(
                listarPagosUseCase.listarRechazados()
        );
    }

    // 7️⃣ Reembolsar pago
    @PostMapping("/{pagoId}/reembolso")
    public ResponseEntity<PagoResponse> reembolsarPago(
            @PathVariable String pagoId,
            @RequestBody ReembolsoRequest request
    ) {
        PagoResponse response =
                reembolsarPagoUseCase.ejecutar(pagoId, request);

        return ResponseEntity.ok(response);
    }
}

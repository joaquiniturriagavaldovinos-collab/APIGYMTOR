package ApiGymorEjecucion.Api.presentation.controller;


import ApiGymorEjecucion.Api.application.dto.request.pago.ConfirmarPagoRequest;
import ApiGymorEjecucion.Api.application.dto.request.pedido.CrearPedidoRequest;
import ApiGymorEjecucion.Api.application.dto.request.pedido.DespacharPedidoRequest;
import ApiGymorEjecucion.Api.application.dto.response.pedido.PedidoResponse;
import ApiGymorEjecucion.Api.application.usecase.pedido.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST para operaciones sobre Pedidos.
 *
 * Este controller SOLO maneja HTTP y delega toda la lógica a los casos de uso.
 * NO contiene lógica de negocio.
 */
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final CrearPedidoUseCase crearPedidoUseCase;
    private final IniciarPagoPedidoUseCase iniciarPagoPedidoUseCase;
    private final ConfirmarResultadoPagoUseCase confirmarResultadoPagoUseCase;
    private final PrepararPedidoUseCase prepararPedidoUseCase;
    private final DespacharPedidoUseCase despacharPedidoUseCase;
    private final ConfirmarEntregaUseCase confirmarEntregaUseCase;
    private final CancelarPedidoUseCase cancelarPedidoUseCase;
    private final ConsultarEstadoPedidoUseCase consultarEstadoPedidoUseCase;

    public PedidoController(
            CrearPedidoUseCase crearPedidoUseCase,
            IniciarPagoPedidoUseCase iniciarPagoPedidoUseCase,
            ConfirmarResultadoPagoUseCase confirmarResultadoPagoUseCase,
            PrepararPedidoUseCase prepararPedidoUseCase,
            DespacharPedidoUseCase despacharPedidoUseCase,
            ConfirmarEntregaUseCase confirmarEntregaUseCase,
            CancelarPedidoUseCase cancelarPedidoUseCase,
            ConsultarEstadoPedidoUseCase consultarEstadoPedidoUseCase
    ) {
        this.crearPedidoUseCase = crearPedidoUseCase;
        this.iniciarPagoPedidoUseCase = iniciarPagoPedidoUseCase;
        this.confirmarResultadoPagoUseCase = confirmarResultadoPagoUseCase;
        this.prepararPedidoUseCase = prepararPedidoUseCase;
        this.despacharPedidoUseCase = despacharPedidoUseCase;
        this.confirmarEntregaUseCase = confirmarEntregaUseCase;
        this.cancelarPedidoUseCase = cancelarPedidoUseCase;
        this.consultarEstadoPedidoUseCase = consultarEstadoPedidoUseCase;
    }

    // 1️⃣ Crear pedido
    @PostMapping
    public ResponseEntity<PedidoResponse> crearPedido(
            @RequestBody CrearPedidoRequest request
    ) {
        PedidoResponse response = crearPedidoUseCase.ejecutar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 2️⃣ Consultar estado del pedido
    @GetMapping("/{pedidoId}")
    public ResponseEntity<PedidoResponse> consultarPedido(
            @PathVariable String pedidoId
    ) {
        PedidoResponse response = consultarEstadoPedidoUseCase.ejecutar(pedidoId);
        return ResponseEntity.ok(response);
    }

    // 3️⃣ Iniciar pago
    @PostMapping("/{pedidoId}/pago")
    public ResponseEntity<PedidoResponse> iniciarPago(
            @PathVariable String pedidoId
    ) {
        PedidoResponse response = iniciarPagoPedidoUseCase.ejecutar(pedidoId);
        return ResponseEntity.ok(response);
    }

    // 4️⃣ Confirmar resultado de pago (callback pasarela)
    @PostMapping("/pago/confirmacion")
    public ResponseEntity<PedidoResponse> confirmarPago(
            @RequestBody ConfirmarPagoRequest request
    ) {
        PedidoResponse response = confirmarResultadoPagoUseCase.ejecutar(request);
        return ResponseEntity.ok(response);
    }

    // 5️⃣ Preparar pedido
    @PostMapping("/{pedidoId}/preparacion")
    public ResponseEntity<PedidoResponse> prepararPedido(
            @PathVariable String pedidoId
    ) {
        PedidoResponse response = prepararPedidoUseCase.ejecutar(pedidoId);
        return ResponseEntity.ok(response);
    }

    // 6️⃣ Despachar pedido
    @PostMapping("/despacho")
    public ResponseEntity<PedidoResponse> despacharPedido(
            @RequestBody DespacharPedidoRequest request
    ) {
        PedidoResponse response = despacharPedidoUseCase.ejecutar(request);
        return ResponseEntity.ok(response);
    }

    // 7️⃣ Confirmar entrega
    @PostMapping("/{pedidoId}/entrega")
    public ResponseEntity<PedidoResponse> confirmarEntrega(
            @PathVariable String pedidoId
    ) {
        PedidoResponse response = confirmarEntregaUseCase.ejecutar(pedidoId);
        return ResponseEntity.ok(response);
    }

    // 8️⃣ Cancelar pedido
    @PostMapping("/{pedidoId}/cancelacion")
    public ResponseEntity<PedidoResponse> cancelarPedido(
            @PathVariable String pedidoId,
            @RequestParam String motivo
    ) {
        PedidoResponse response = cancelarPedidoUseCase.ejecutar(pedidoId, motivo);
        return ResponseEntity.ok(response);
    }
}
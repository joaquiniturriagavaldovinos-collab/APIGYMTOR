package ApiGymorEjecucion.Api.presentation.controller;


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
    private final PrepararPedidoUseCase prepararPedidoUseCase;
    private final DespacharPedidoUseCase despacharPedidoUseCase;
    private final ConfirmarEntregaUseCase confirmarEntregaUseCase;
    private final ConsultarEstadoPedidoUseCase consultarEstadoPedidoUseCase;
    private final CancelarPedidoUseCase cancelarPedidoUseCase;

    public PedidoController(
            CrearPedidoUseCase crearPedidoUseCase,
            IniciarPagoPedidoUseCase iniciarPagoPedidoUseCase,
            PrepararPedidoUseCase prepararPedidoUseCase,
            DespacharPedidoUseCase despacharPedidoUseCase,
            ConfirmarEntregaUseCase confirmarEntregaUseCase,
            ConsultarEstadoPedidoUseCase consultarEstadoPedidoUseCase,
            CancelarPedidoUseCase cancelarPedidoUseCase) {
        this.crearPedidoUseCase = crearPedidoUseCase;
        this.iniciarPagoPedidoUseCase = iniciarPagoPedidoUseCase;
        this.prepararPedidoUseCase = prepararPedidoUseCase;
        this.despacharPedidoUseCase = despacharPedidoUseCase;
        this.confirmarEntregaUseCase = confirmarEntregaUseCase;
        this.consultarEstadoPedidoUseCase = consultarEstadoPedidoUseCase;
        this.cancelarPedidoUseCase = cancelarPedidoUseCase;
    }

    /**
     * CU1: Crear Pedido
     * POST /api/pedidos
     *
     * @param request Datos del pedido a crear
     * @return Pedido creado con estado CREATED
     */
    @PostMapping
    public ResponseEntity<PedidoResponse> crearPedido(
            @RequestBody CrearPedidoRequest request) {

        PedidoResponse response = crearPedidoUseCase.ejecutar(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * CU2: Iniciar Pago de Pedido
     * POST /api/pedidos/{id}/iniciar-pago
     *
     * @param id ID del pedido
     * @return Pedido en estado PAYMENT_PENDING
     */
    @PostMapping("/{id}/iniciar-pago")
    public ResponseEntity<PedidoResponse> iniciarPago(
            @PathVariable String id) {

        PedidoResponse response = iniciarPagoPedidoUseCase.ejecutar(id);
        return ResponseEntity.ok(response);
    }

    /**
     * CU4: Preparar Pedido
     * POST /api/pedidos/{id}/preparar
     *
     * @param id ID del pedido
     * @return Pedido en estado PREPARING
     */
    @PostMapping("/{id}/preparar")
    public ResponseEntity<PedidoResponse> prepararPedido(
            @PathVariable String id) {

        PedidoResponse response = prepararPedidoUseCase.ejecutar(id);
        return ResponseEntity.ok(response);
    }

    /**
     * CU5: Despachar Pedido
     * POST /api/pedidos/{id}/despachar
     *
     * @param id ID del pedido
     * @param request Datos de despacho (guía, transportista)
     * @return Pedido en estado DISPATCHED
     */
    @PostMapping("/{id}/despachar")
    public ResponseEntity<PedidoResponse> despacharPedido(
            @PathVariable String id,
            @RequestBody DespacharPedidoRequest request) {

        // Asegurar que el ID coincida
        request.setPedidoId(id);

        PedidoResponse response = despacharPedidoUseCase.ejecutar(request);
        return ResponseEntity.ok(response);
    }

    /**
     * CU6: Confirmar Entrega
     * POST /api/pedidos/{id}/confirmar-entrega
     *
     * @param id ID del pedido
     * @return Pedido en estado DELIVERED (final)
     */
    @PostMapping("/{id}/confirmar-entrega")
    public ResponseEntity<PedidoResponse> confirmarEntrega(
            @PathVariable String id) {

        PedidoResponse response = confirmarEntregaUseCase.ejecutar(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Consultar Estado de Pedido
     * GET /api/pedidos/{id}
     *
     * @param id ID del pedido
     * @return Información completa del pedido
     */
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> consultarPedido(
            @PathVariable String id) {

        PedidoResponse response = consultarEstadoPedidoUseCase.ejecutar(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Cancelar Pedido
     * POST /api/pedidos/{id}/cancelar
     *
     * @param id ID del pedido
     * @param motivo Razón de la cancelación (query param)
     * @return Pedido en estado CANCELLED (final)
     */
    @PostMapping("/{id}/cancelar")
    public ResponseEntity<PedidoResponse> cancelarPedido(
            @PathVariable String id,
            @RequestParam String motivo) {

        PedidoResponse response = cancelarPedidoUseCase.ejecutar(id, motivo);
        return ResponseEntity.ok(response);
    }
}
package ApiGymorEjecucion.Api.presentation.controller;
import ApiGymorEjecucion.Api.application.dto.request.producto.*;
import ApiGymorEjecucion.Api.application.dto.response.producto.ProductoResponse;
import ApiGymorEjecucion.Api.application.usecase.producto.COMMANDS.*;
import ApiGymorEjecucion.Api.application.usecase.producto.QUERIES.*;
import ApiGymorEjecucion.Api.domain.model.producto.Producto;
import ApiGymorEjecucion.Api.domain.model.producto.TipoProducto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller para Producto
 * Sigue arquitectura CQRS Liviana
 */
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    // ===== COMMANDS =====
    private final CrearProductoUseCase crearProductoUC;
    private final ActualizarPrecioProductoUseCase actualizarPrecioUC;
    private final ActualizarInformacionProductoUseCase actualizarInfoUC;
    private final IncrementarStockProductoUseCase incrementarStockUC;
    private final DecrementarStockProductoUseCase decrementarStockUC;
    private final ReservarStockProductoUseCase reservarStockUC;
    private final LiberarReservaStockUseCase liberarReservaUC;
    private final ConfirmarReservaStockUseCase confirmarReservaUC;
    private final AjustarInventarioUseCase ajustarInventarioUC;
    private final ActivarProductoUseCase activarProductoUC;
    private final DesactivarProductoUseCase desactivarProductoUC;
    private final EliminarProductoUseCase eliminarProductoUC;

    // ===== QUERIES =====
    private final BuscarProductoPorIdUseCase buscarPorIdUC;
    private final BuscarProductoPorCodigoUseCase buscarPorCodigoUC;
    private final ListarProductosUseCase listarProductosUC;
    private final ListarProductosActivosUseCase listarActivosUC;
    private final ListarProductosStockBajoUseCase listarStockBajoUC;
    private final ListarProductosAgotadosUseCase listarAgotadosUC;

    public ProductoController(
            CrearProductoUseCase crearProductoUC,
            ActualizarPrecioProductoUseCase actualizarPrecioUC,
            ActualizarInformacionProductoUseCase actualizarInfoUC,
            ActualizarStockUseCase actualizarStockUC,
            IncrementarStockProductoUseCase incrementarStockUC,
            DecrementarStockProductoUseCase decrementarStockUC,
            ReservarStockProductoUseCase reservarStockUC,
            LiberarReservaStockUseCase liberarReservaUC,
            ConfirmarReservaStockUseCase confirmarReservaUC,
            AjustarInventarioUseCase ajustarInventarioUC,
            ActivarProductoUseCase activarProductoUC,
            DesactivarProductoUseCase desactivarProductoUC,
            EliminarProductoUseCase eliminarProductoUC,
            BuscarProductoPorIdUseCase buscarPorIdUC,
            BuscarProductoPorCodigoUseCase buscarPorCodigoUC,
            ListarProductosUseCase listarProductosUC,
            ListarProductosActivosUseCase listarActivosUC,
            ListarProductosStockBajoUseCase listarStockBajoUC,
            ListarProductosAgotadosUseCase listarAgotadosUC) {
        this.crearProductoUC = crearProductoUC;
        this.actualizarPrecioUC = actualizarPrecioUC;
        this.actualizarInfoUC = actualizarInfoUC;
        this.incrementarStockUC = incrementarStockUC;
        this.decrementarStockUC = decrementarStockUC;
        this.reservarStockUC = reservarStockUC;
        this.liberarReservaUC = liberarReservaUC;
        this.confirmarReservaUC = confirmarReservaUC;
        this.ajustarInventarioUC = ajustarInventarioUC;
        this.activarProductoUC = activarProductoUC;
        this.desactivarProductoUC = desactivarProductoUC;
        this.eliminarProductoUC = eliminarProductoUC;
        this.buscarPorIdUC = buscarPorIdUC;
        this.buscarPorCodigoUC = buscarPorCodigoUC;
        this.listarProductosUC = listarProductosUC;
        this.listarActivosUC = listarActivosUC;
        this.listarStockBajoUC = listarStockBajoUC;
        this.listarAgotadosUC = listarAgotadosUC;
    }

    // ========================================
    // COMMANDS - POST/PUT/PATCH/DELETE
    // ========================================

    /**
     * POST /api/productos
     * Crear un nuevo producto
     */
    @PostMapping
    public ResponseEntity<ProductoResponse> crear(
            @Valid @RequestBody CrearProductoRequest request) {

        Producto producto = crearProductoUC.ejecutar(
                request.getCodigo(),
                request.getNombre(),
                request.getDescripcion(),
                TipoProducto.valueOf(request.getTipo()),
                request.getPrecio()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ProductoResponse.fromDomain(producto));
    }

    /**
     * PUT /api/productos/{id}/precio
     * Actualizar precio del producto
     */
    @PutMapping("/{id}/precio")
    public ResponseEntity<ProductoResponse> actualizarPrecio(
            @PathVariable String id,
            @Valid @RequestBody ActualizarPrecioRequest request) {

        Producto producto = actualizarPrecioUC.ejecutar(id, request.getNuevoPrecio());
        return ResponseEntity.ok(ProductoResponse.fromDomain(producto));
    }

    /**
     * PUT /api/productos/{id}/informacion
     * Actualizar nombre y/o descripción
     */
    @PutMapping("/{id}/informacion")
    public ResponseEntity<ProductoResponse> actualizarInformacion(
            @PathVariable String id,
            @Valid @RequestBody ActualizarInformacionRequest request) {

        Producto producto = actualizarInfoUC.ejecutar(
                id,
                request.getNombre(),
                request.getDescripcion()
        );
        return ResponseEntity.ok(ProductoResponse.fromDomain(producto));
    }


    /**
     * PATCH /api/productos/{id}/stock/incrementar
     * Incrementar stock (reposición)
     */
    @PatchMapping("/{id}/stock/incrementar")
    public ResponseEntity<ProductoResponse> incrementarStock(
            @PathVariable String id,
            @Valid @RequestBody CantidadRequest request) {

        Producto producto = incrementarStockUC.ejecutar(id, request.getCantidad());
        return ResponseEntity.ok(ProductoResponse.fromDomain(producto));
    }

    /**
     * PATCH /api/productos/{id}/stock/decrementar
     * Decrementar stock (venta)
     */
    @PatchMapping("/{id}/stock/decrementar")
    public ResponseEntity<ProductoResponse> decrementarStock(
            @PathVariable String id,
            @Valid @RequestBody CantidadRequest request) {

        Producto producto = decrementarStockUC.ejecutar(id, request.getCantidad());
        return ResponseEntity.ok(ProductoResponse.fromDomain(producto));
    }

    /**
     * PATCH /api/productos/{id}/stock/reservar
     * Reservar stock (carrito)
     */
    @PatchMapping("/{id}/stock/reservar")
    public ResponseEntity<ProductoResponse> reservarStock(
            @PathVariable String id,
            @Valid @RequestBody CantidadRequest request) {

        Producto producto = reservarStockUC.ejecutar(id, request.getCantidad());
        return ResponseEntity.ok(ProductoResponse.fromDomain(producto));
    }

    /**
     * PATCH /api/productos/{id}/stock/liberar-reserva
     * Liberar reserva (cancelación)
     */
    @PatchMapping("/{id}/stock/liberar-reserva")
    public ResponseEntity<ProductoResponse> liberarReserva(
            @PathVariable String id,
            @Valid @RequestBody CantidadRequest request) {

        Producto producto = liberarReservaUC.ejecutar(id, request.getCantidad());
        return ResponseEntity.ok(ProductoResponse.fromDomain(producto));
    }

    /**
     * PATCH /api/productos/{id}/stock/confirmar-reserva
     * Confirmar reserva como venta
     */
    @PatchMapping("/{id}/stock/confirmar-reserva")
    public ResponseEntity<ProductoResponse> confirmarReserva(
            @PathVariable String id,
            @Valid @RequestBody CantidadRequest request) {

        Producto producto = confirmarReservaUC.ejecutar(id, request.getCantidad());
        return ResponseEntity.ok(ProductoResponse.fromDomain(producto));
    }

    /**
     * PATCH /api/productos/{id}/stock/ajustar
     * Ajustar inventario (corrección física)
     */
    @PatchMapping("/{id}/stock/ajustar")
    public ResponseEntity<ProductoResponse> ajustarInventario(
            @PathVariable String id,
            @Valid @RequestBody CantidadRequest request) {

        Producto producto = ajustarInventarioUC.ejecutar(id, request.getCantidad());
        return ResponseEntity.ok(ProductoResponse.fromDomain(producto));
    }

    /**
     * PATCH /api/productos/{id}/activar
     * Activar producto
     */
    @PatchMapping("/{id}/activar")
    public ResponseEntity<ProductoResponse> activar(@PathVariable String id) {
        Producto producto = activarProductoUC.ejecutar(id);
        return ResponseEntity.ok(ProductoResponse.fromDomain(producto));
    }

    /**
     * PATCH /api/productos/{id}/desactivar
     * Desactivar producto
     */
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<ProductoResponse> desactivar(@PathVariable String id) {
        Producto producto = desactivarProductoUC.ejecutar(id);
        return ResponseEntity.ok(ProductoResponse.fromDomain(producto));
    }

    /**
     * DELETE /api/productos/{id}
     * Eliminar producto físicamente
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        eliminarProductoUC.ejecutar(id);
        return ResponseEntity.noContent().build();
    }

    // ========================================
    // QUERIES - GET
    // ========================================

    /**
     * GET /api/productos
     * Listar todos los productos
     */
    @GetMapping
    public ResponseEntity<List<ProductoResponse>> listarTodos() {
        List<Producto> productos = listarProductosUC.listarTodos();

        List<ProductoResponse> response = productos.stream()
                .map(ProductoResponse::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }


    /**
     * GET /api/productos/activos
     * Listar solo productos activos
     */
    @GetMapping("/activos")
    public ResponseEntity<List<ProductoResponse>> listarActivos() {
        List<Producto> productos = listarActivosUC.ejecutar();
        List<ProductoResponse> response = productos.stream()
                .map(ProductoResponse::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/productos/stock-bajo
     * Listar productos con stock bajo
     */
    @GetMapping("/stock-bajo")
    public ResponseEntity<List<ProductoResponse>> listarStockBajo() {
        List<Producto> productos = listarStockBajoUC.ejecutar();
        List<ProductoResponse> response = productos.stream()
                .map(ProductoResponse::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/productos/agotados
     * Listar productos agotados
     */
    @GetMapping("/agotados")
    public ResponseEntity<List<ProductoResponse>> listarAgotados() {
        List<Producto> productos = listarAgotadosUC.ejecutar();
        List<ProductoResponse> response = productos.stream()
                .map(ProductoResponse::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/productos/{id}
     * Buscar producto por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> buscarPorId(@PathVariable String id) {
        Producto producto = buscarPorIdUC.ejecutar(id);
        return ResponseEntity.ok(ProductoResponse.fromDomain(producto));
    }

    /**
     * GET /api/productos/codigo/{codigo}
     * Buscar producto por código
     */
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ProductoResponse> buscarPorCodigo(@PathVariable String codigo) {
        Producto producto = buscarPorCodigoUC.ejecutar(codigo);
        return ResponseEntity.ok(ProductoResponse.fromDomain(producto));
    }
}
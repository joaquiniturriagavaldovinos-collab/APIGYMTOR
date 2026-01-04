package ApiGymorEjecucion.Api.presentation.controller;


import ApiGymorEjecucion.Api.application.usecase.producto.ActualizarStockUseCase;
import ApiGymorEjecucion.Api.application.usecase.producto.CrearProductoUseCase;
import ApiGymorEjecucion.Api.application.usecase.producto.ListarProductosUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para operaciones sobre Productos
 */
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final CrearProductoUseCase crearProductoUseCase;
    private final ListarProductosUseCase listarProductosUseCase;
    private final ActualizarStockUseCase actualizarStockUseCase;

    public ProductoController(
            CrearProductoUseCase crearProductoUseCase,
            ListarProductosUseCase listarProductosUseCase,
            ActualizarStockUseCase actualizarStockUseCase) {
        this.crearProductoUseCase = crearProductoUseCase;
        this.listarProductosUseCase = listarProductosUseCase;
        this.actualizarStockUseCase = actualizarStockUseCase;
    }

    /**
     * Crear un nuevo producto
     * POST /api/productos
     */
    @PostMapping
    public ResponseEntity<CrearProductoUseCase.ProductoResponse> crearProducto(
            @RequestBody CrearProductoUseCase.CrearProductoRequest request) {

        CrearProductoUseCase.ProductoResponse response = crearProductoUseCase.ejecutar(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Listar todos los productos
     * GET /api/productos
     */
    @GetMapping
    public ResponseEntity<List<ListarProductosUseCase.ProductoListResponse>> listarProductos() {
        List<ListarProductosUseCase.ProductoListResponse> productos = listarProductosUseCase.listarTodos();
        return ResponseEntity.ok(productos);
    }

    /**
     * Listar productos por tipo
     * GET /api/productos/tipo/{tipo}
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<ListarProductosUseCase.ProductoListResponse>> listarPorTipo(
            @PathVariable String tipo) {

        List<ListarProductosUseCase.ProductoListResponse> productos =
                listarProductosUseCase.listarPorTipo(tipo);
        return ResponseEntity.ok(productos);
    }

    /**
     * Listar productos con stock disponible
     * GET /api/productos/con-stock
     */
    @GetMapping("/con-stock")
    public ResponseEntity<List<ListarProductosUseCase.ProductoListResponse>> listarConStock() {
        List<ListarProductosUseCase.ProductoListResponse> productos =
                listarProductosUseCase.listarConStock();
        return ResponseEntity.ok(productos);
    }

    /**
     * Buscar producto por código
     * GET /api/productos/codigo/{codigo}
     */
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ListarProductosUseCase.ProductoListResponse> buscarPorCodigo(
            @PathVariable String codigo) {

        ListarProductosUseCase.ProductoListResponse producto =
                listarProductosUseCase.buscarPorCodigo(codigo);
        return ResponseEntity.ok(producto);
    }

    /**
     * Buscar producto por ID
     * GET /api/productos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ListarProductosUseCase.ProductoListResponse> buscarPorId(
            @PathVariable String id) {

        ListarProductosUseCase.ProductoListResponse producto =
                listarProductosUseCase.buscarPorId(id);
        return ResponseEntity.ok(producto);
    }

    /**
     * Incrementar stock de un producto
     * POST /api/productos/{id}/stock/incrementar
     */
    @PostMapping("/{id}/stock/incrementar")
    public ResponseEntity<ActualizarStockUseCase.ActualizarStockResponse> incrementarStock(
            @PathVariable String id,
            @RequestParam int cantidad) {

        ActualizarStockUseCase.ActualizarStockResponse response =
                actualizarStockUseCase.incrementarStock(id, cantidad);
        return ResponseEntity.ok(response);
    }

    /**
     * Decrementar stock de un producto
     * POST /api/productos/{id}/stock/decrementar
     */
    @PostMapping("/{id}/stock/decrementar")
    public ResponseEntity<ActualizarStockUseCase.ActualizarStockResponse> decrementarStock(
            @PathVariable String id,
            @RequestParam int cantidad) {

        ActualizarStockUseCase.ActualizarStockResponse response =
                actualizarStockUseCase.decrementarStock(id, cantidad);
        return ResponseEntity.ok(response);
    }

    /**
     * Ajustar stock a un valor específico
     * POST /api/productos/{id}/stock/ajustar
     */
    @PostMapping("/{id}/stock/ajustar")
    public ResponseEntity<ActualizarStockUseCase.ActualizarStockResponse> ajustarStock(
            @PathVariable String id,
            @RequestParam int nuevoStock) {

        ActualizarStockUseCase.ActualizarStockResponse response =
                actualizarStockUseCase.ajustarStock(id, nuevoStock);
        return ResponseEntity.ok(response);
    }
}
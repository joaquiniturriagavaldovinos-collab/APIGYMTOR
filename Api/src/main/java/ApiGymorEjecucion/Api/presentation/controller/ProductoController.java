package ApiGymorEjecucion.Api.presentation.controller;


import ApiGymorEjecucion.Api.application.dto.request.producto.CrearProductoRequest;
import ApiGymorEjecucion.Api.application.dto.response.producto.ActualizarStockResponse;
import ApiGymorEjecucion.Api.application.dto.response.producto.ProductoListResponse;
import ApiGymorEjecucion.Api.application.dto.response.producto.ProductoResponse;
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
            ActualizarStockUseCase actualizarStockUseCase
    ) {
        this.crearProductoUseCase = crearProductoUseCase;
        this.listarProductosUseCase = listarProductosUseCase;
        this.actualizarStockUseCase = actualizarStockUseCase;
    }

    // =========================
    // 📦 PRODUCTOS
    // =========================

    // 1️⃣ Crear producto
    @PostMapping
    public ResponseEntity<ProductoResponse> crearProducto(
            @RequestBody CrearProductoRequest request
    ) {
        ProductoResponse response = crearProductoUseCase.ejecutar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 2️⃣ Listar todos los productos activos
    @GetMapping
    public ResponseEntity<List<ProductoListResponse>> listarProductos() {
        return ResponseEntity.ok(
                listarProductosUseCase.listarTodos()
        );
    }

    // 3️⃣ Listar productos por tipo
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<ProductoListResponse>> listarPorTipo(
            @PathVariable String tipo
    ) {
        return ResponseEntity.ok(
                listarProductosUseCase.listarPorTipo(tipo)
        );
    }

    // 4️⃣ Listar productos con stock disponible
    @GetMapping("/con-stock")
    public ResponseEntity<List<ProductoListResponse>> listarConStock() {
        return ResponseEntity.ok(
                listarProductosUseCase.listarConStock()
        );
    }

    // 5️⃣ Obtener producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductoListResponse> obtenerPorId(
            @PathVariable String id
    ) {
        return ResponseEntity.ok(
                listarProductosUseCase.buscarPorId(id)
        );
    }

    // 6️⃣ Obtener producto por código
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ProductoListResponse> obtenerPorCodigo(
            @PathVariable String codigo
    ) {
        return ResponseEntity.ok(
                listarProductosUseCase.buscarPorCodigo(codigo)
        );
    }



    // 7️⃣ Incrementar stock
    @PostMapping("/{id}/stock/incrementar")
    public ResponseEntity<ActualizarStockResponse> incrementarStock(
            @PathVariable String id,
            @RequestParam int cantidad
    ) {
        return ResponseEntity.ok(
                actualizarStockUseCase.incrementarStock(id, cantidad)
        );
    }

    // 8️⃣ Decrementar stock
    @PostMapping("/{id}/stock/decrementar")
    public ResponseEntity<ActualizarStockResponse> decrementarStock(
            @PathVariable String id,
            @RequestParam int cantidad
    ) {
        return ResponseEntity.ok(
                actualizarStockUseCase.decrementarStock(id, cantidad)
        );
    }

    // 9️⃣ Ajustar stock
    @PutMapping("/{id}/stock")
    public ResponseEntity<ActualizarStockResponse> ajustarStock(
            @PathVariable String id,
            @RequestParam int nuevoStock
    ) {
        return ResponseEntity.ok(
                actualizarStockUseCase.ajustarStock(id, nuevoStock)
        );
    }
}

package ApiGymorEjecucion.Api.presentation.controller;

import ApiGymorEjecucion.Api.application.dto.request.producto.CrearProductoRequest;
import ApiGymorEjecucion.Api.application.dto.response.producto.ActualizarStockResponse;
import ApiGymorEjecucion.Api.application.dto.response.producto.ProductoListResponse;
import ApiGymorEjecucion.Api.application.dto.response.producto.ProductoResponse;
import ApiGymorEjecucion.Api.application.usecase.producto.*;

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

import java.util.List;

@Tag(
        name = "Productos",
        description = "Gestión de productos y control de stock"
)
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

    // -------------------------------------------------
    // CREAR PRODUCTO
    // -------------------------------------------------
    @Operation(
            summary = "Crear producto",
            description = "Crea un nuevo producto en el catálogo"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Producto creado correctamente",
                    content = @Content(schema = @Schema(implementation = ProductoResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ProductoResponse> crearProducto(
            @RequestBody CrearProductoRequest request
    ) {
        ProductoResponse response = crearProductoUseCase.ejecutar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // -------------------------------------------------
    // LISTAR PRODUCTOS
    // -------------------------------------------------
    @Operation(
            summary = "Listar productos",
            description = "Obtiene todos los productos activos"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado de productos",
                    content = @Content(schema = @Schema(implementation = ProductoListResponse.class))
            )
    })
    @GetMapping
    public ResponseEntity<List<ProductoListResponse>> listarProductos() {
        return ResponseEntity.ok(
                listarProductosUseCase.listarTodos()
        );
    }

    @Operation(
            summary = "Listar productos por tipo",
            description = "Obtiene productos filtrados por tipo"
    )
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<ProductoListResponse>> listarPorTipo(
            @Parameter(description = "Tipo de producto", example = "SUPLEMENTO")
            @PathVariable String tipo
    ) {
        return ResponseEntity.ok(
                listarProductosUseCase.listarPorTipo(tipo)
        );
    }

    @Operation(
            summary = "Listar productos con stock",
            description = "Obtiene solo productos con stock disponible"
    )
    @GetMapping("/con-stock")
    public ResponseEntity<List<ProductoListResponse>> listarConStock() {
        return ResponseEntity.ok(
                listarProductosUseCase.listarConStock()
        );
    }

    // -------------------------------------------------
    // CONSULTA PRODUCTO
    // -------------------------------------------------
    @Operation(
            summary = "Obtener producto por ID",
            description = "Obtiene el detalle de un producto por su ID"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProductoListResponse> obtenerPorId(
            @Parameter(description = "ID del producto", example = "prod_001")
            @PathVariable String id
    ) {
        return ResponseEntity.ok(
                listarProductosUseCase.buscarPorId(id)
        );
    }

    @Operation(
            summary = "Obtener producto por código",
            description = "Obtiene un producto por su código único"
    )
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ProductoListResponse> obtenerPorCodigo(
            @Parameter(description = "Código del producto", example = "SKU-12345")
            @PathVariable String codigo
    ) {
        return ResponseEntity.ok(
                listarProductosUseCase.buscarPorCodigo(codigo)
        );
    }

    // -------------------------------------------------
    // CONTROL DE STOCK (ADMIN)
    // -------------------------------------------------
    @Operation(
            summary = "Incrementar stock",
            description = "Incrementa el stock de un producto"
    )
    @PostMapping("/{id}/stock/incrementar")
    public ResponseEntity<ActualizarStockResponse> incrementarStock(
            @PathVariable String id,
            @RequestParam int cantidad
    ) {
        return ResponseEntity.ok(
                actualizarStockUseCase.incrementarStock(id, cantidad)
        );
    }

    @Operation(
            summary = "Decrementar stock",
            description = "Reduce el stock de un producto"
    )
    @PostMapping("/{id}/stock/decrementar")
    public ResponseEntity<ActualizarStockResponse> decrementarStock(
            @PathVariable String id,
            @RequestParam int cantidad
    ) {
        return ResponseEntity.ok(
                actualizarStockUseCase.decrementarStock(id, cantidad)
        );
    }

    @Operation(
            summary = "Ajustar stock",
            description = "Ajusta el stock a un valor exacto"
    )
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

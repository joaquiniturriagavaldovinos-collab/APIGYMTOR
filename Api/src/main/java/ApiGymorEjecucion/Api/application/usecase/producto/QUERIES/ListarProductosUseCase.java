package ApiGymorEjecucion.Api.application.usecase.producto.QUERIES;

import ApiGymorEjecucion.Api.application.dto.response.producto.ProductoListResponse;
import ApiGymorEjecucion.Api.domain.model.producto.Producto;
import ApiGymorEjecucion.Api.domain.model.producto.TipoProducto;
import ApiGymorEjecucion.Api.domain.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Caso de Uso: Listar Productos
 *
 * Consulta el catálogo de productos con filtros opcionales
 */
@Service
public class ListarProductosUseCase {

    private final ProductoRepository productoRepository;

    public ListarProductosUseCase(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // ========================
    // CASOS DE USO
    // ========================

    /**
     * Lista todos los productos activos
     */
    public List<Producto> listarTodos() {
        return productoRepository.buscarTodos().stream()
                .filter(Producto::isActivo)
                .collect(Collectors.toList());
    }


    /**
     * Lista productos por tipo
     */
    public List<ProductoListResponse> listarPorTipo(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("El tipo de producto es requerido");
        }

        TipoProducto tipoProducto;
        try {
            tipoProducto = TipoProducto.valueOf(tipo);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de producto inválido: " + tipo);
        }

        return productoRepository.buscarPorTipo(tipoProducto).stream()
                .filter(Producto::isActivo)
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lista productos con stock disponible
     */
    public List<ProductoListResponse> listarConStock() {
        return productoRepository.buscarTodos().stream()
                .filter(Producto::isActivo)
                .filter(p -> p.getStockDisponible() > 0)
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    /**
     * Busca un producto por su código
     */
    public ProductoListResponse buscarPorCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código del producto es requerido");
        }

        Producto producto = productoRepository.buscarPorCodigo(codigo)
                .orElseThrow(() ->
                        new IllegalArgumentException("No se encontró el producto con código: " + codigo)
                );

        return mapearAResponse(producto);
    }

    /**
     * Busca un producto por su ID
     */
    public ProductoListResponse buscarPorId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID del producto es requerido");
        }

        Producto producto = productoRepository.buscarPorId(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("No se encontró el producto con ID: " + id)
                );

        return mapearAResponse(producto);
    }

    // ========================
    // MAPPER
    // ========================

    private ProductoListResponse mapearAResponse(Producto producto) {
        ProductoListResponse response = new ProductoListResponse();
        response.setId(producto.getId());
        response.setCodigo(producto.getCodigo());
        response.setNombre(producto.getNombre());
        response.setDescripcion(producto.getDescripcion());
        response.setTipo(producto.getTipo().name());
        response.setTipoDescripcion(producto.getTipo().getDescripcion());
        response.setPrecio(producto.getPrecio());
        response.setActivo(producto.isActivo());
        response.setStockDisponible(producto.getStockDisponible());
        response.setDisponibleParaVenta(producto.getStockDisponible() > 0);
        response.setFechaCreacion(producto.getFechaCreacion());
        return response;
    }



}

package ApiGymorEjecucion.Api.application.usecase.producto;


import ApiGymorEjecucion.Api.domain.model.producto.Producto;
import ApiGymorEjecucion.Api.domain.model.producto.TipoProducto;
import ApiGymorEjecucion.Api.domain.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    /**
     * Lista todos los productos activos
     *
     * @return Lista de productos
     */
    public List<ProductoResponse> listarTodos() {
        List<Producto> productos = productoRepository.buscarTodos();
        return productos.stream()
                .filter(Producto::isActivo)
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lista productos por tipo (DISCO, MAQUINA, etc.)
     *
     * @param tipo Tipo de producto
     * @return Lista de productos del tipo especificado
     */
    public List<ProductoResponse> listarPorTipo(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("El tipo de producto es requerido");
        }

        TipoProducto tipoProducto;
        try {
            tipoProducto = TipoProducto.valueOf(tipo);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de producto inválido: " + tipo);
        }

        List<Producto> productos = productoRepository.buscarPorTipo(tipoProducto);
        return productos.stream()
                .filter(Producto::isActivo)
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lista productos con stock disponible
     *
     * @return Lista de productos con stock > 0
     */
    public List<ProductoResponse> listarConStock() {
        List<Producto> productos = productoRepository.buscarTodos();
        return productos.stream()
                .filter(Producto::isActivo)
                .filter(p -> p.getStockDisponible() > 0)
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    /**
     * Busca un producto por su código
     *
     * @param codigo Código del producto
     * @return Producto encontrado
     */
    public ProductoResponse buscarPorCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código del producto es requerido");
        }

        Producto producto = productoRepository.buscarPorCodigo(codigo)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el producto con código: " + codigo
                ));

        return mapearAResponse(producto);
    }

    /**
     * Busca un producto por su ID
     *
     * @param id ID del producto
     * @return Producto encontrado
     */
    public ProductoResponse buscarPorId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID del producto es requerido");
        }

        Producto producto = productoRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el producto con ID: " + id
                ));

        return mapearAResponse(producto);
    }

    private ProductoResponse mapearAResponse(Producto producto) {
        ProductoResponse response = new ProductoResponse();
        response.setId(producto.getId());
        response.setCodigo(producto.getCodigo());
        response.setNombre(producto.getNombre());
        response.setDescripcion(producto.getDescripcion());
        response.setTipo(producto.getTipo().name());
        response.setTipoDescripcion(producto.getTipo().getDescripcion());
        response.setPrecio(producto.getPrecio());
        response.setActivo(producto.isActivo());
        response.setStockDisponible(producto.getStockDisponible());
        response.setFechaCreacion(producto.getFechaCreacion());
        response.setDisponibleParaVenta(producto.getStockDisponible() > 0);
        return response;
    }

    // ===== DTO DE RESPUESTA =====

    public static class ProductoResponse {
        private String id;
        private String codigo;
        private String nombre;
        private String descripcion;
        private String tipo;
        private String tipoDescripcion;
        private BigDecimal precio;
        private boolean activo;
        private int stockDisponible;
        private boolean disponibleParaVenta;
        private LocalDateTime fechaCreacion;

        // Getters y Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCodigo() {
            return codigo;
        }

        public void setCodigo(String codigo) {
            this.codigo = codigo;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        public String getTipoDescripcion() {
            return tipoDescripcion;
        }

        public void setTipoDescripcion(String tipoDescripcion) {
            this.tipoDescripcion = tipoDescripcion;
        }

        public BigDecimal getPrecio() {
            return precio;
        }

        public void setPrecioContinuar(BigDecimal precio) {
            this.precio = precio;
        }
        public boolean isActivo() {
            return activo;
        }

        public void setActivo(boolean activo) {
            this.activo = activo;
        }

        public int getStockDisponible() {
            return stockDisponible;
        }

        public void setStockDisponible(int stockDisponible) {
            this.stockDisponible = stockDisponible;
        }

        public boolean isDisponibleParaVenta() {
            return disponibleParaVenta;
        }

        public void setDisponibleParaVenta(boolean disponibleParaVenta) {
            this.disponibleParaVenta = disponibleParaVenta;
        }

        public LocalDateTime getFechaCreacion() {
            return fechaCreacion;
        }

        public void setFechaCreacion(LocalDateTime fechaCreacion) {
            this.fechaCreacion = fechaCreacion;
        }
    }
}
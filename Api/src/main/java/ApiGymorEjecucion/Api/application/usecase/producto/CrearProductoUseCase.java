package ApiGymorEjecucion.Api.application.usecase.producto;



import ApiGymorEjecucion.Api.domain.model.producto.Producto;
import ApiGymorEjecucion.Api.domain.model.producto.Stock;
import ApiGymorEjecucion.Api.domain.model.producto.TipoProducto;
import ApiGymorEjecucion.Api.domain.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Caso de Uso: Crear Producto
 *
 * Crea un nuevo producto en el catálogo (disco, máquina, etc.)
 */
@Service
public class CrearProductoUseCase {

    private final ProductoRepository productoRepository;

    public CrearProductoUseCase(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * Crea un nuevo producto
     *
     * @param request Datos del producto a crear
     * @return Producto creado
     */
    public ProductoResponse ejecutar(CrearProductoRequest request) {
        // Validar request
        validarRequest(request);

        // Verificar que no exista producto con ese código
        if (productoRepository.existePorCodigo(request.getCodigo())) {
            throw new IllegalArgumentException(
                    "Ya existe un producto con el código: " + request.getCodigo()
            );
        }

        // Crear producto en dominio
        TipoProducto tipo = TipoProducto.valueOf(request.getTipo());

        Producto producto = Producto.crear(
                generarIdProducto(),
                request.getCodigo(),
                request.getNombre(),
                request.getDescripcion(),
                tipo,
                request.getPrecio()
        );

        // Si viene stock inicial, agregarlo
        if (request.getStockInicial() != null && request.getStockInicial() > 0) {
            Stock stockInicial = Stock.crear(request.getStockInicial());
            producto.establecerStock(stockInicial);
        }

        // Persistir
        Producto productoGuardado = productoRepository.guardar(producto);

        // Retornar response
        return mapearAResponse(productoGuardado);
    }

    private void validarRequest(CrearProductoRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }
        if (request.getCodigo() == null || request.getCodigo().isBlank()) {
            throw new IllegalArgumentException("El código del producto es requerido");
        }
        if (request.getNombre() == null || request.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del producto es requerido");
        }
        if (request.getTipo() == null || request.getTipo().isBlank()) {
            throw new IllegalArgumentException("El tipo de producto es requerido");
        }
        if (request.getPrecio() == null || request.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }
    }

    private String generarIdProducto() {
        return "PROD-" + System.currentTimeMillis();
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
        return response;
    }

    // ===== DTOs INTERNOS =====

    public static class CrearProductoRequest {
        private String codigo;
        private String nombre;
        private String descripcion;
        private String tipo; // "DISCO", "MAQUINA", etc.
        private BigDecimal precio;
        private Integer stockInicial;

        // Getters y Setters
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

        public BigDecimal getPrecio() {
            return precio;
        }

        public void setPrecio(BigDecimal precio) {
            this.precio = precio;
        }

        public Integer getStockInicial() {
            return stockInicial;
        }

        public void setStockInicial(Integer stockInicial) {
            this.stockInicial = stockInicial;
        }
    }

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
        private java.time.LocalDateTime fechaCreacion;

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

        public void setPrecio(BigDecimal precio) {
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

        public java.time.LocalDateTime getFechaCreacion() {
            return fechaCreacion;
        }

        public void setFechaCreacion(java.time.LocalDateTime fechaCreacion) {
            this.fechaCreacion = fechaCreacion;
        }
    }
}
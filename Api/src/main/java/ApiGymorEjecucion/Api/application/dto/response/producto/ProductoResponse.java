package ApiGymorEjecucion.Api.application.dto.response.producto;

import ApiGymorEjecucion.Api.domain.model.producto.Producto;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductoResponse {
    private String id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private String tipo;
    private String tipoDescripcion;
    private BigDecimal precio;
    private StockResponse stock;
    private boolean activo;
    private MetadataResponse metadata;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaCreacion;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaActualizacion;

    // Factory method desde Dominio
    public static ProductoResponse fromDomain(Producto producto) {
        ProductoResponse response = new ProductoResponse();
        response.id = producto.getId();
        response.codigo = producto.getCodigo();
        response.nombre = producto.getNombre();
        response.descripcion = producto.getDescripcion();
        response.tipo = producto.getTipo().name();
        response.tipoDescripcion = producto.getTipo().getDescripcion();
        response.precio = producto.getPrecio();
        response.stock = StockResponse.fromDomain(producto.getStock());
        response.activo = producto.isActivo();
        response.fechaCreacion = producto.getFechaCreacion();
        response.fechaActualizacion = producto.getFechaActualizacion();
        response.metadata = MetadataResponse.fromDomain(producto);
        return response;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getTipoDescripcion() { return tipoDescripcion; }
    public void setTipoDescripcion(String tipoDescripcion) {
        this.tipoDescripcion = tipoDescripcion;
    }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public StockResponse getStock() { return stock; }
    public void setStock(StockResponse stock) { this.stock = stock; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
    public MetadataResponse getMetadata() { return metadata; }
    public void setMetadata(MetadataResponse metadata) { this.metadata = metadata; }
}

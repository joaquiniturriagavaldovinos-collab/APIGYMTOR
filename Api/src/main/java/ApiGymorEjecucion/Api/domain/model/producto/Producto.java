package ApiGymorEjecucion.Api.domain.model.producto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad: Producto
 * Representa un producto físico del catálogo (disco, máquina, etc.)
 */
public class Producto {
    private final String id;
    private final String codigo;
    private String nombre;
    private String descripcion;
    private final TipoProducto tipo;
    private BigDecimal precio;
    private Stock stock;
    private boolean activo;
    private final LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    private Producto(String id, String codigo, String nombre, String descripcion,
                     TipoProducto tipo, BigDecimal precio) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.precio = precio;
        this.stock = Stock.crear(0); // Stock inicial en 0
        this.activo = true;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    /**
     * Factory method: Crea un nuevo producto
     */
    public static Producto crear(String id, String codigo, String nombre,
                                 String descripcion, TipoProducto tipo, BigDecimal precio) {
        validarDatosCreacion(id, codigo, nombre, tipo, precio);
        return new Producto(id, codigo, nombre, descripcion, tipo, precio);
    }

    private static void validarDatosCreacion(String id, String codigo, String nombre,
                                             TipoProducto tipo, BigDecimal precio) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID del producto es requerido");
        }
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código del producto es requerido");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del producto es requerido");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de producto es requerido");
        }
        if (precio == null || precio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }
    }

    // ===== MÉTODOS DE NEGOCIO - GESTIÓN DE STOCK =====

    public void establecerStock(Stock nuevoStock) {
        if (nuevoStock == null) {
            throw new IllegalArgumentException("El stock no puede ser nulo");
        }
        this.stock = nuevoStock;
        this.fechaActualizacion = LocalDateTime.now();
    }

    public void incrementarStock(int cantidad) {
        this.stock = this.stock.incrementar(cantidad);
        this.fechaActualizacion = LocalDateTime.now();
    }

    public void decrementarStock(int cantidad) {
        this.stock = this.stock.decrementar(cantidad);
        this.fechaActualizacion = LocalDateTime.now();
    }

    public void reservarStock(int cantidad) {
        this.stock = this.stock.reservar(cantidad);
        this.fechaActualizacion = LocalDateTime.now();
    }

    public void liberarReserva(int cantidad) {
        this.stock = this.stock.liberarReserva(cantidad);
        this.fechaActualizacion = LocalDateTime.now();
    }

    // ===== MÉTODOS DE NEGOCIO - GESTIÓN DE PRODUCTO =====

    public void actualizarPrecio(BigDecimal nuevoPrecio) {
        if (nuevoPrecio == null || nuevoPrecio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }
        this.precio = nuevoPrecio;
        this.fechaActualizacion = LocalDateTime.now();
    }

    public void actualizarInformacion(String nuevoNombre, String nuevaDescripcion) {
        if (nuevoNombre != null && !nuevoNombre.isBlank()) {
            this.nombre = nuevoNombre;
        }
        if (nuevaDescripcion != null) {
            this.descripcion = nuevaDescripcion;
        }
        this.fechaActualizacion = LocalDateTime.now();
    }

    public void desactivar() {
        this.activo = false;
        this.fechaActualizacion = LocalDateTime.now();
    }

    public void activar() {
        this.activo = true;
        this.fechaActualizacion = LocalDateTime.now();
    }

    // ===== MÉTODOS DE CONSULTA =====

    public boolean tieneStockDisponible(int cantidadRequerida) {
        return this.stock.tieneStockDisponible(cantidadRequerida);
    }

    public int getStockDisponible() {
        return this.stock.getCantidadDisponible();
    }

    public boolean estaDisponibleParaVenta() {
        return this.activo && this.stock.getCantidadDisponible() > 0;
    }

    // ===== GETTERS =====

    public String getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public TipoProducto getTipo() {
        return tipo;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public Stock getStock() {
        return stock;
    }

    public boolean isActivo() {
        return activo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return Objects.equals(id, producto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Producto{id='%s', codigo='%s', nombre='%s', tipo=%s, precio=%s, stock=%d}",
                id, codigo, nombre, tipo, precio, stock.getCantidadDisponible());
    }
}
package ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "productos")
public class ProductoEntity {

    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "tipo", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoProductoEntity tipo;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    // Stock embebido
    @Embedded
    private StockEntity stock;

    @Column(nullable = false)
    private boolean activo = true;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // Constructores
    public ProductoEntity() {}

    public ProductoEntity(String id, String codigo, String nombre, String descripcion,
                          TipoProductoEntity tipo, BigDecimal precio,
                          LocalDateTime fechaCreacion) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.precio = precio;
        this.fechaCreacion = fechaCreacion;
        this.activo = true;
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

    public TipoProductoEntity getTipo() { return tipo; }
    public void setTipo(TipoProductoEntity tipo) { this.tipo = tipo; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public StockEntity getStock() { return stock; }
    public void setStock(StockEntity stock) { this.stock = stock; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    // Enums
    public enum TipoProductoEntity {
        DISCO, MAQUINA, ACCESORIO, BARRA, RACK, CARDIO
    }

    // Value Object embebido
    @Embeddable
    public static class StockEntity {
        @Column(name = "stock_cantidad", nullable = false)
        private int cantidad;

        @Column(name = "stock_cantidad_reservada", nullable = false)
        private int cantidadReservada;

        @Column(name = "stock_cantidad_disponible", nullable = false)
        private int cantidadDisponible;

        public StockEntity() {}

        public StockEntity(int cantidad, int cantidadReservada, int cantidadDisponible) {
            this.cantidad = cantidad;
            this.cantidadReservada = cantidadReservada;
            this.cantidadDisponible = cantidadDisponible;
        }

        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }

        public int getCantidadReservada() { return cantidadReservada; }
        public void setCantidadReservada(int cantidadReservada) {
            this.cantidadReservada = cantidadReservada;
        }

        public int getCantidadDisponible() { return cantidadDisponible; }
        public void setCantidadDisponible(int cantidadDisponible) {
            this.cantidadDisponible = cantidadDisponible;
        }
    }
}
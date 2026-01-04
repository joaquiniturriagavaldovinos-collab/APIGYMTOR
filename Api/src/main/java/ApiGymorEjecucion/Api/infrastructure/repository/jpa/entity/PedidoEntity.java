package ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * Entidad JPA para mapear Pedido a la base de datos.
 *
 * IMPORTANTE: Esta clase es SOLO para persistencia.
 * La lógica de negocio vive en domain/model/pedido/Pedido.java
 */
@Entity
@Table(name = "pedidos")
public class PedidoEntity {

    @Id
    @Column(name = "id", nullable = false, length = 50)
    private String id;

    @Column(name = "cliente_id", nullable = false, length = 50)
    private String clienteId;

    @Column(name = "estado", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private EstadoPedidoEntity estado;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "pedido_id")
    private List<ItemPedidoEntity> items = new ArrayList<>();

    @Column(name = "total", precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(name = "referencia_pago", length = 100)
    private String referenciaPago;

    @Column(name = "guia_despacho", length = 100)
    private String guiaDespacho;

    // Constructors
    public PedidoEntity() {
    }

    public PedidoEntity(String id, String clienteId, EstadoPedidoEntity estado,
                        BigDecimal total, LocalDateTime fechaCreacion) {
        this.id = id;
        this.clienteId = clienteId;
        this.estado = estado;
        this.total = total;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaCreacion;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public EstadoPedidoEntity getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedidoEntity estado) {
        this.estado = estado;
    }

    public List<ItemPedidoEntity> getItems() {
        return items;
    }

    public void setItems(List<ItemPedidoEntity> items) {
        this.items = items;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getReferenciaPago() {
        return referenciaPago;
    }

    public void setReferenciaPago(String referenciaPago) {
        this.referenciaPago = referenciaPago;
    }

    public String getGuiaDespacho() {
        return guiaDespacho;
    }

    public void setGuiaDespacho(String guiaDespacho) {
        this.guiaDespacho = guiaDespacho;
    }

    // Enum para JPA
    public enum EstadoPedidoEntity {
        CREATED, PAYMENT_PENDING, PAID, PREPARING,
        DISPATCHED, DELIVERED, FAILED, CANCELLED
    }

    // Nested Entity para items
    @Entity
    @Table(name = "pedido_items")
    public static class ItemPedidoEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "producto_id", nullable = false)
        private String productoId;

        @Column(name = "nombre", nullable = false)
        private String nombre;

        @Column(name = "tipo", nullable = false)
        private String tipo;

        @Column(name = "cantidad", nullable = false)
        private int cantidad;

        @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
        private BigDecimal precioUnitario;

        @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
        private BigDecimal subtotal;

        // Constructor
        public ItemPedidoEntity() {
        }

        public ItemPedidoEntity(String productoId, String nombre, String tipo,
                                int cantidad, BigDecimal precioUnitario, BigDecimal subtotal) {
            this.productoId = productoId;
            this.nombre = nombre;
            this.tipo = tipo;
            this.cantidad = cantidad;
            this.precioUnitario = precioUnitario;
            this.subtotal = subtotal;
        }

        // Getters y Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getProductoId() {
            return productoId;
        }

        public void setProductoId(String productoId) {
            this.productoId = productoId;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        public int getCantidad() {
            return cantidad;
        }

        public void setCantidad(int cantidad) {
            this.cantidad = cantidad;
        }

        public BigDecimal getPrecioUnitario() {
            return precioUnitario;
        }

        public void setPrecioUnitario(BigDecimal precioUnitario) {
            this.precioUnitario = precioUnitario;
        }

        public BigDecimal getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(BigDecimal subtotal) {
            this.subtotal = subtotal;
        }
    }
}
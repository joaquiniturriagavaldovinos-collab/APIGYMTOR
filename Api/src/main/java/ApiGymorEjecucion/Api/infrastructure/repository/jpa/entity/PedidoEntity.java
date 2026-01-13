package ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class PedidoEntity {

    @Id
    private String id;

    @Column(name = "cliente_id", nullable = false)
    private String clienteId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoPedidoEntity estado;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(name = "referencia_pago")
    private String referenciaPago;

    @Column(name = "guia_despacho")
    private String guiaDespacho;

    // Items del pedido (OneToMany)
    @ElementCollection
    @CollectionTable(name = "pedido_items",
            joinColumns = @JoinColumn(name = "pedido_id"))
    private List<ItemPedidoEntity> items = new ArrayList<>();

    // Historial de transiciones (OneToMany)
    @ElementCollection
    @CollectionTable(name = "pedido_historial",
            joinColumns = @JoinColumn(name = "pedido_id"))
    @OrderColumn(name = "orden")
    private List<TransicionEstadoEntity> historialEstados = new ArrayList<>();

    // Constructores
    public PedidoEntity() {}

    public PedidoEntity(String id, String clienteId, EstadoPedidoEntity estado,
                        BigDecimal total, LocalDateTime fechaCreacion) {
        this.id = id;
        this.clienteId = clienteId;
        this.estado = estado;
        this.total = total;
        this.fechaCreacion = fechaCreacion;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }

    public EstadoPedidoEntity getEstado() { return estado; }
    public void setEstado(EstadoPedidoEntity estado) { this.estado = estado; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getReferenciaPago() { return referenciaPago; }
    public void setReferenciaPago(String referenciaPago) { this.referenciaPago = referenciaPago; }

    public String getGuiaDespacho() { return guiaDespacho; }
    public void setGuiaDespacho(String guiaDespacho) { this.guiaDespacho = guiaDespacho; }

    public List<ItemPedidoEntity> getItems() { return items; }
    public void setItems(List<ItemPedidoEntity> items) { this.items = items; }

    public List<TransicionEstadoEntity> getHistorialEstados() { return historialEstados; }
    public void setHistorialEstados(List<TransicionEstadoEntity> historialEstados) {
        this.historialEstados = historialEstados;
    }

    // Enums
    public enum EstadoPedidoEntity {
        CREATED, PAYMENT_PENDING, PAID, PREPARING, DISPATCHED, DELIVERED, FAILED, CANCELLED
    }

    // Value Objects embebidos
    @Embeddable
    public static class ItemPedidoEntity {
        @Column(name = "producto_id")
        private String productoId;

        private String nombre;

        private String tipo;

        private int cantidad;

        @Column(name = "precio_unitario", precision = 10, scale = 2)
        private BigDecimal precioUnitario;

        @Column(precision = 10, scale = 2)
        private BigDecimal subtotal;

        public ItemPedidoEntity() {}

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
        public String getProductoId() { return productoId; }
        public void setProductoId(String productoId) { this.productoId = productoId; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }

        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }

        public BigDecimal getPrecioUnitario() { return precioUnitario; }
        public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

        public BigDecimal getSubtotal() { return subtotal; }
        public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    }

    @Embeddable
    public static class TransicionEstadoEntity {
        @Column(name = "estado_anterior")
        @Enumerated(EnumType.STRING)
        private EstadoPedidoEntity estadoAnterior;

        @Column(name = "estado_nuevo")
        @Enumerated(EnumType.STRING)
        private EstadoPedidoEntity estadoNuevo;

        @Column(name = "fecha_transicion")
        private LocalDateTime fechaTransicion;

        private String observacion;

        public TransicionEstadoEntity() {}

        public TransicionEstadoEntity(EstadoPedidoEntity estadoAnterior,
                                      EstadoPedidoEntity estadoNuevo,
                                      LocalDateTime fechaTransicion,
                                      String observacion) {
            this.estadoAnterior = estadoAnterior;
            this.estadoNuevo = estadoNuevo;
            this.fechaTransicion = fechaTransicion;
            this.observacion = observacion;
        }

        // Getters y Setters
        public EstadoPedidoEntity getEstadoAnterior() { return estadoAnterior; }
        public void setEstadoAnterior(EstadoPedidoEntity estadoAnterior) {
            this.estadoAnterior = estadoAnterior;
        }

        public EstadoPedidoEntity getEstadoNuevo() { return estadoNuevo; }
        public void setEstadoNuevo(EstadoPedidoEntity estadoNuevo) {
            this.estadoNuevo = estadoNuevo;
        }

        public LocalDateTime getFechaTransicion() { return fechaTransicion; }
        public void setFechaTransicion(LocalDateTime fechaTransicion) {
            this.fechaTransicion = fechaTransicion;
        }

        public String getObservacion() { return observacion; }
        public void setObservacion(String observacion) { this.observacion = observacion; }
    }
}
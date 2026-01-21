package ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.pedido;

import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.pedido.embeddable.ItemPedidoEntity;
import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.pedido.embeddable.TransicionEstadoEntity;
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

}
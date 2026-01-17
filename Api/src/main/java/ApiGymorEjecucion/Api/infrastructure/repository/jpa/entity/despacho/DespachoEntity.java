package ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.despacho;

import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.despacho.embeddable.DireccionEntregaEmbeddable;
import ApiGymorEjecucion.Api.domain.model.Despacho.EstadoDespacho;
import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.despacho.embeddable.GuiaDespachoEmbeddable;
import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.despacho.embeddable.TransportistaEmbeddable;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "despachos")
public class DespachoEntity {

    @Id
    @NotBlank
    @Column(nullable = false, length = 36)
    private String id;

    @NotBlank
    @Column(nullable = false)
    private String pedidoId;

    @Embedded
    private GuiaDespachoEmbeddable guiaDespacho;

    @Embedded
    private TransportistaEmbeddable transportista;

    @Embedded
    private DireccionEntregaEmbeddable direccionEntrega;

    private LocalDateTime fechaDespacho;
    private LocalDateTime fechaEntregaEstimada;
    private LocalDateTime fechaEntregaReal;

    @Size(max = 255)
    private String observaciones;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoDespacho estado;

    /* ===== getters / setters JPA ===== */

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPedidoId() { return pedidoId; }
    public void setPedidoId(String pedidoId) { this.pedidoId = pedidoId; }

    public GuiaDespachoEmbeddable getGuiaDespacho() { return guiaDespacho; }
    public void setGuiaDespacho(GuiaDespachoEmbeddable guiaDespacho) {
        this.guiaDespacho = guiaDespacho;
    }

    public TransportistaEmbeddable getTransportista() { return transportista; }
    public void setTransportista(TransportistaEmbeddable transportista) {
        this.transportista = transportista;
    }

    public DireccionEntregaEmbeddable getDireccionEntrega() { return direccionEntrega; }
    public void setDireccionEntrega(DireccionEntregaEmbeddable direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public LocalDateTime getFechaDespacho() { return fechaDespacho; }
    public void setFechaDespacho(LocalDateTime fechaDespacho) {
        this.fechaDespacho = fechaDespacho;
    }

    public LocalDateTime getFechaEntregaEstimada() { return fechaEntregaEstimada; }
    public void setFechaEntregaEstimada(LocalDateTime fechaEntregaEstimada) {
        this.fechaEntregaEstimada = fechaEntregaEstimada;
    }

    public LocalDateTime getFechaEntregaReal() { return fechaEntregaReal; }
    public void setFechaEntregaReal(LocalDateTime fechaEntregaReal) {
        this.fechaEntregaReal = fechaEntregaReal;
    }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public EstadoDespacho getEstado() { return estado; }
    public void setEstado(EstadoDespacho estado) { this.estado = estado; }
}

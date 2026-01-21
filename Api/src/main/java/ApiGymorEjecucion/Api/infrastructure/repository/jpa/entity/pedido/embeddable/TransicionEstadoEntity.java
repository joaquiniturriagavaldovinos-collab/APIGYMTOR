package ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.pedido.embeddable;

import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.pedido.EstadoPedidoEntity;
import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.pedido.PedidoEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;

@Embeddable
public class TransicionEstadoEntity {
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
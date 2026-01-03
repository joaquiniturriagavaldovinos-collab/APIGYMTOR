package ApiGymorEjecucion.Api.domain.model.Despacho;


import ApiGymorEjecucion.Api.domain.model.Cliente.DireccionEntrega;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad: Despacho
 * Representa el envío físico de un pedido
 */
public class Despacho {
    private final String id;
    private final String pedidoId;
    private GuiaDespacho guiaDespacho;
    private Transportista transportista;
    private DireccionEntrega direccionEntrega;
    private LocalDateTime fechaDespacho;
    private LocalDateTime fechaEntregaEstimada;
    private LocalDateTime fechaEntregaReal;
    private String observaciones;

    private Despacho(String id, String pedidoId, DireccionEntrega direccionEntrega) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.direccionEntrega = direccionEntrega;
    }

    public static Despacho crear(String id, String pedidoId, DireccionEntrega direccionEntrega) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID del despacho es requerido");
        }
        if (pedidoId == null || pedidoId.isBlank()) {
            throw new IllegalArgumentException("El ID del pedido es requerido");
        }
        if (direccionEntrega == null) {
            throw new IllegalArgumentException("La dirección de entrega es requerida");
        }
        return new Despacho(id, pedidoId, direccionEntrega);
    }

    // Métodos de negocio
    public void asignarTransportista(Transportista transportista, GuiaDespacho guiaDespacho) {
        if (transportista == null) {
            throw new IllegalArgumentException("El transportista no puede ser nulo");
        }
        if (guiaDespacho == null) {
            throw new IllegalArgumentException("La guía de despacho no puede ser nula");
        }
        this.transportista = transportista;
        this.guiaDespacho = guiaDespacho;
        this.fechaDespacho = LocalDateTime.now();
    }

    public void establecerFechaEntregaEstimada(LocalDateTime fechaEntregaEstimada) {
        if (fechaEntregaEstimada == null) {
            throw new IllegalArgumentException("La fecha de entrega estimada no puede ser nula");
        }
        if (fechaEntregaEstimada.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de entrega estimada no puede ser en el pasado");
        }
        this.fechaEntregaEstimada = fechaEntregaEstimada;
    }

    public void confirmarEntrega() {
        this.fechaEntregaReal = LocalDateTime.now();
    }

    public boolean estaEntregado() {
        return fechaEntregaReal != null;
    }

    public boolean estaDespachado() {
        return fechaDespacho != null && guiaDespacho != null;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getPedidoId() {
        return pedidoId;
    }

    public GuiaDespacho getGuiaDespacho() {
        return guiaDespacho;
    }

    public Transportista getTransportista() {
        return transportista;
    }

    public DireccionEntrega getDireccionEntrega() {
        return direccionEntrega;
    }

    public LocalDateTime getFechaDespacho() {
        return fechaDespacho;
    }

    public LocalDateTime getFechaEntregaEstimada() {
        return fechaEntregaEstimada;
    }

    public LocalDateTime getFechaEntregaReal() {
        return fechaEntregaReal;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Despacho despacho = (Despacho) o;
        return Objects.equals(id, despacho.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Despacho{id='%s', pedidoId='%s', despachado=%s}",
                id, pedidoId, estaDespachado());
    }
}
package ApiGymorEjecucion.Api.domain.model.Despacho;

import ApiGymorEjecucion.Api.domain.model.Despacho.DireccionEntrega;

import java.time.LocalDateTime;

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
    private EstadoDespacho estado;

    private Despacho(String id, String pedidoId, EstadoDespacho estado) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.estado = estado;
    }

    public static Despacho crear(String id, String pedidoId) {
        return new Despacho(id, pedidoId, EstadoDespacho.PENDIENTE);
    }

    public static Despacho reconstruir(
            String id,
            String pedidoId,
            GuiaDespacho guiaDespacho,
            Transportista transportista,
            DireccionEntrega direccionEntrega,
            LocalDateTime fechaDespacho,
            LocalDateTime fechaEntregaEstimada,
            LocalDateTime fechaEntregaReal,
            String observaciones,
            EstadoDespacho estado
    ) {
        Despacho d = new Despacho(id, pedidoId, estado);
        d.guiaDespacho = guiaDespacho;
        d.transportista = transportista;
        d.direccionEntrega = direccionEntrega;
        d.fechaDespacho = fechaDespacho;
        d.fechaEntregaEstimada = fechaEntregaEstimada;
        d.fechaEntregaReal = fechaEntregaReal;
        d.observaciones = observaciones;
        return d;
    }

    /* === COMPORTAMIENTO === */

    public void despachar(GuiaDespacho guia, Transportista transportista) {
        if (estado != EstadoDespacho.PENDIENTE) {
            throw new IllegalStateException("El despacho no está pendiente");
        }
        this.guiaDespacho = guia;
        this.transportista = transportista;
        this.fechaDespacho = LocalDateTime.now();
        this.estado = EstadoDespacho.EN_TRANSITO;
    }

    public void confirmarEntrega() {
        if (estado != EstadoDespacho.EN_TRANSITO) {
            throw new IllegalStateException("No está en tránsito");
        }
        this.fechaEntregaReal = LocalDateTime.now();
        this.estado = EstadoDespacho.ENTREGADO;
    }

    public void actualizarObservaciones(String observaciones) {
        if (observaciones == null || observaciones.isBlank()) {
            throw new IllegalArgumentException("Observaciones inválidas");
        }
        this.observaciones = observaciones;
    }

    /* === GETTERS (LECTURA) === */

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

    public EstadoDespacho getEstado() {
        return estado;
    }
}

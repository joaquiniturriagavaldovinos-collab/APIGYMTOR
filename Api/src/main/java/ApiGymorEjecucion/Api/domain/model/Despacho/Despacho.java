package ApiGymorEjecucion.Api.domain.model.Despacho;

import ApiGymorEjecucion.Api.domain.model.Cliente.DireccionEntrega;

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

    private Despacho(
            String id,
            String pedidoId,
            EstadoDespacho estado
    ) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.estado = estado;
    }

    /* ===================== FACTORY ===================== */

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
        Despacho despacho = new Despacho(id, pedidoId, estado);
        despacho.guiaDespacho = guiaDespacho;
        despacho.transportista = transportista;
        despacho.direccionEntrega = direccionEntrega;
        despacho.fechaDespacho = fechaDespacho;
        despacho.fechaEntregaEstimada = fechaEntregaEstimada;
        despacho.fechaEntregaReal = fechaEntregaReal;
        despacho.observaciones = observaciones;
        return despacho;
    }

    /* ===================== COMPORTAMIENTO ===================== */

    public void despachar(
            GuiaDespacho guiaDespacho,
            Transportista transportista
    ) {
        if (estado != EstadoDespacho.PENDIENTE) {
            throw new IllegalStateException("El despacho no está pendiente");
        }

        this.guiaDespacho = guiaDespacho;
        this.transportista = transportista;
        this.fechaDespacho = LocalDateTime.now();
        this.estado = EstadoDespacho.EN_TRANSITO;
    }

    public void confirmarEntrega() {
        if (estado != EstadoDespacho.EN_TRANSITO) {
            throw new IllegalStateException(
                    "No se puede entregar un despacho que no está en tránsito"
            );
        }
        this.fechaEntregaReal = LocalDateTime.now();
        this.estado = EstadoDespacho.ENTREGADO;
    }

    public void establecerFechaEntregaEstimada(LocalDateTime fecha) {
        this.fechaEntregaEstimada = fecha;
    }

    /* ===================== ESTADOS ===================== */

    public boolean estaPendiente() {
        return estado == EstadoDespacho.PENDIENTE;
    }

    public boolean estaDespachado() {
        return estado == EstadoDespacho.EN_TRANSITO
                || estado == EstadoDespacho.ENTREGADO;
    }

    public boolean estaEnTransito() {
        return estado == EstadoDespacho.EN_TRANSITO;
    }

    public boolean estaEntregado() {
        return estado == EstadoDespacho.ENTREGADO;
    }

    public void actualizarObservaciones(String observaciones) {
        if (observaciones == null || observaciones.isBlank()) {
            throw new IllegalArgumentException("Las observaciones no pueden estar vacías");
        }
        this.observaciones = observaciones;
    }


    /* ===================== GETTERS ===================== */

    public String getId() { return id; }
    public String getPedidoId() { return pedidoId; }
    public GuiaDespacho getGuiaDespacho() { return guiaDespacho; }
    public Transportista getTransportista() { return transportista; }
    public DireccionEntrega getDireccionEntrega() { return direccionEntrega; }
    public LocalDateTime getFechaDespacho() { return fechaDespacho; }
    public LocalDateTime getFechaEntregaEstimada() { return fechaEntregaEstimada; }
    public LocalDateTime getFechaEntregaReal() { return fechaEntregaReal; }
    public String getObservaciones() { return observaciones; }
    public EstadoDespacho getEstado() { return estado; }
}

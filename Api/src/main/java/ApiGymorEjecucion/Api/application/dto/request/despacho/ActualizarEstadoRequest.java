package ApiGymorEjecucion.Api.application.dto.request.despacho;

import java.time.LocalDateTime;

public  class ActualizarEstadoRequest {
    private String tipoActualizacion; // ENTREGA_CONFIRMADA, FECHA_ESTIMADA, OBSERVACIONES
    private LocalDateTime fechaEntregaEstimada;
    private String observaciones;

    public String getTipoActualizacion() { return tipoActualizacion; }
    public void setTipoActualizacion(String tipoActualizacion) {
        this.tipoActualizacion = tipoActualizacion;
    }

    public LocalDateTime getFechaEntregaEstimada() { return fechaEntregaEstimada; }
    public void setFechaEntregaEstimada(LocalDateTime fechaEntregaEstimada) {
        this.fechaEntregaEstimada = fechaEntregaEstimada;
    }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
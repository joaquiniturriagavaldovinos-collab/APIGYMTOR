package ApiGymorEjecucion.Api.application.dto.request.despacho;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class ActualizarEstadoRequest {

    @NotBlank(message = "El tipo de actualización es obligatorio")
    private String tipoActualizacion; // ENTREGA_CONFIRMADA, FECHA_ESTIMADA, OBSERVACIONES

    private LocalDateTime fechaEntregaEstimada;
    private String observaciones;

    // Constructors
    public ActualizarEstadoRequest() {
    }

    public ActualizarEstadoRequest(String tipoActualizacion, LocalDateTime fechaEntregaEstimada, String observaciones) {
        this.tipoActualizacion = tipoActualizacion;
        this.fechaEntregaEstimada = fechaEntregaEstimada;
        this.observaciones = observaciones;
    }

    // Getters y Setters
    public String getTipoActualizacion() {
        return tipoActualizacion;
    }

    public void setTipoActualizacion(String tipoActualizacion) {
        this.tipoActualizacion = tipoActualizacion;
    }

    public LocalDateTime getFechaEntregaEstimada() {
        return fechaEntregaEstimada;
    }

    public void setFechaEntregaEstimada(LocalDateTime fechaEntregaEstimada) {
        this.fechaEntregaEstimada = fechaEntregaEstimada;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
package ApiGymorEjecucion.Api.application.dto.request.despacho;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Future;

import java.time.LocalDateTime;

public class ActualizarEstadoRequest {

    /**
     * Tipo de actualización permitida:
     * - ENTREGA_CONFIRMADA
     * - FECHA_ESTIMADA
     * - OBSERVACIONES
     */
    @NotBlank(message = "El tipo de actualización es obligatorio")
    @Pattern(
            regexp = "ENTREGA_CONFIRMADA|FECHA_ESTIMADA|OBSERVACIONES",
            message = "El tipo de actualización debe ser ENTREGA_CONFIRMADA, FECHA_ESTIMADA u OBSERVACIONES"
    )
    private String tipoActualizacion;

    /**
     * Fecha estimada de entrega.
     * Obligatoria solo cuando tipoActualizacion = FECHA_ESTIMADA
     */
    @Future(message = "La fecha estimada de entrega debe ser futura")
    private LocalDateTime fechaEntregaEstimada;

    /**
     * Observaciones del despacho
     */
    @Size(
            max = 500,
            message = "Las observaciones no pueden superar los 500 caracteres"
    )
    private String observaciones;

    // Getters & Setters

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

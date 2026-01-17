package ApiGymorEjecucion.Api.application.dto.request.servicio;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * DTO para actualización de precio de servicio
 */
public class ActualizarServicioRequest {

    @NotNull(message = "El nuevo precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El nuevo precio debe ser mayor a cero")
    private BigDecimal nuevoPrecio;

    // Getters y Setters

    public BigDecimal getNuevoPrecio() {
        return nuevoPrecio;
    }

    public void setNuevoPrecio(BigDecimal nuevoPrecio) {
        this.nuevoPrecio = nuevoPrecio;
    }
}

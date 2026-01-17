package ApiGymorEjecucion.Api.application.dto.request.plan;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ActualizarPlanRequest {
    @NotNull(message = "El nuevo precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El nuevo precio debe ser mayor a 0")
    @Digits(integer = 10, fraction = 2, message = "El precio debe tener como máximo 2 decimales")
    private BigDecimal nuevoPrecio;

    public BigDecimal getNuevoPrecio() {
        return nuevoPrecio;
    }

    public void setNuevoPrecio(BigDecimal nuevoPrecio) {
        this.nuevoPrecio = nuevoPrecio;
    }}
package ApiGymorEjecucion.Api.application.dto.request.producto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ActualizarPrecioRequest {
    @NotNull(message = "El nuevo precio es requerido")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a cero")
    private BigDecimal nuevoPrecio;

    public BigDecimal getNuevoPrecio() { return nuevoPrecio; }
    public void setNuevoPrecio(BigDecimal nuevoPrecio) { this.nuevoPrecio = nuevoPrecio; }
}
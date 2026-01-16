package ApiGymorEjecucion.Api.application.dto.request.servicio;

import java.math.BigDecimal;

public class ActualizarServicioRequest {
    private BigDecimal nuevoPrecio;

    public BigDecimal getNuevoPrecio() { return nuevoPrecio; }
    public void setNuevoPrecio(BigDecimal nuevoPrecio) { this.nuevoPrecio = nuevoPrecio; }
}
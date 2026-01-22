package ApiGymorEjecucion.Api.application.dto.request.producto;

import jakarta.validation.constraints.Min;

public class CantidadRequest {
    @Min(value = 1, message = "La cantidad debe ser mayor a cero")
    private int cantidad;

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}

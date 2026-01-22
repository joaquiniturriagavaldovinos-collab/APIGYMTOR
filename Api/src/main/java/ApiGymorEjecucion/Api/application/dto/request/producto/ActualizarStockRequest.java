package ApiGymorEjecucion.Api.application.dto.request.producto;

import jakarta.validation.constraints.Min;

public class ActualizarStockRequest {
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private int cantidad;

    @Min(value = 0, message = "La cantidad reservada no puede ser negativa")
    private int cantidadReservada;

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public int getCantidadReservada() { return cantidadReservada; }
    public void setCantidadReservada(int cantidadReservada) {
        this.cantidadReservada = cantidadReservada;
    }
}

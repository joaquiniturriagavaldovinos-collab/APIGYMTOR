package ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.producto.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class StockEntity {
    @Column(name = "stock_cantidad", nullable = false)
    private int cantidad;

    @Column(name = "stock_cantidad_reservada", nullable = false)
    private int cantidadReservada;

    @Column(name = "stock_cantidad_disponible", nullable = false)
    private int cantidadDisponible;

    public StockEntity() {}

    public StockEntity(int cantidad, int cantidadReservada, int cantidadDisponible) {
        this.cantidad = cantidad;
        this.cantidadReservada = cantidadReservada;
        this.cantidadDisponible = cantidadDisponible;
    }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public int getCantidadReservada() { return cantidadReservada; }
    public void setCantidadReservada(int cantidadReservada) {
        this.cantidadReservada = cantidadReservada;
    }

    public int getCantidadDisponible() { return cantidadDisponible; }
    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }
}
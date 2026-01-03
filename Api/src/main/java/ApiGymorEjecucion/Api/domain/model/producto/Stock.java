package ApiGymorEjecucion.Api.domain.model.producto;

import java.util.Objects;

/**
 * Value Object: Stock de un producto
 * Inmutable - cada cambio genera una nueva instancia
 */
public class Stock {
    private final int cantidad;
    private final int cantidadReservada;
    private final int cantidadDisponible;

    private Stock(int cantidad, int cantidadReservada) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        if (cantidadReservada < 0) {
            throw new IllegalArgumentException("La cantidad reservada no puede ser negativa");
        }
        if (cantidadReservada > cantidad) {
            throw new IllegalArgumentException("La cantidad reservada no puede ser mayor a la cantidad total");
        }

        this.cantidad = cantidad;
        this.cantidadReservada = cantidadReservada;
        this.cantidadDisponible = cantidad - cantidadReservada;
    }

    /**
     * Factory method: Crea stock sin reservas
     */
    public static Stock crear(int cantidad) {
        return new Stock(cantidad, 0);
    }

    /**
     * Factory method: Crea stock con reservas
     */
    public static Stock crearConReservas(int cantidad, int cantidadReservada) {
        return new Stock(cantidad, cantidadReservada);
    }

    /**
     * Crea un nuevo Stock incrementando la cantidad
     */
    public Stock incrementar(int cantidadAIncrementar) {
        if (cantidadAIncrementar <= 0) {
            throw new IllegalArgumentException("La cantidad a incrementar debe ser mayor a cero");
        }
        return new Stock(this.cantidad + cantidadAIncrementar, this.cantidadReservada);
    }

    /**
     * Crea un nuevo Stock decrementando la cantidad
     */
    public Stock decrementar(int cantidadADecrementar) {
        if (cantidadADecrementar <= 0) {
            throw new IllegalArgumentException("La cantidad a decrementar debe ser mayor a cero");
        }
        if (this.cantidadDisponible < cantidadADecrementar) {
            throw new IllegalArgumentException(
                    String.format("Stock insuficiente. Disponible: %d, Solicitado: %d",
                            this.cantidadDisponible, cantidadADecrementar)
            );
        }
        return new Stock(this.cantidad - cantidadADecrementar, this.cantidadReservada);
    }

    /**
     * Crea un nuevo Stock reservando cantidad
     */
    public Stock reservar(int cantidadAReservar) {
        if (cantidadAReservar <= 0) {
            throw new IllegalArgumentException("La cantidad a reservar debe ser mayor a cero");
        }
        if (this.cantidadDisponible < cantidadAReservar) {
            throw new IllegalArgumentException(
                    String.format("Stock disponible insuficiente para reservar. Disponible: %d, Solicitado: %d",
                            this.cantidadDisponible, cantidadAReservar)
            );
        }
        return new Stock(this.cantidad, this.cantidadReservada + cantidadAReservar);
    }

    /**
     * Crea un nuevo Stock liberando reservas
     */
    public Stock liberarReserva(int cantidadALiberar) {
        if (cantidadALiberar <= 0) {
            throw new IllegalArgumentException("La cantidad a liberar debe ser mayor a cero");
        }
        if (this.cantidadReservada < cantidadALiberar) {
            throw new IllegalArgumentException(
                    String.format("No hay suficientes reservas para liberar. Reservado: %d, Solicitado: %d",
                            this.cantidadReservada, cantidadALiberar)
            );
        }
        return new Stock(this.cantidad, this.cantidadReservada - cantidadALiberar);
    }

    /**
     * Verifica si hay stock disponible suficiente
     */
    public boolean tieneStockDisponible(int cantidadRequerida) {
        return this.cantidadDisponible >= cantidadRequerida;
    }

    // Getters
    public int getCantidad() {
        return cantidad;
    }

    public int getCantidadReservada() {
        return cantidadReservada;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return cantidad == stock.cantidad && cantidadReservada == stock.cantidadReservada;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cantidad, cantidadReservada);
    }

    @Override
    public String toString() {
        return String.format("Stock{total=%d, reservado=%d, disponible=%d}",
                cantidad, cantidadReservada, cantidadDisponible);
    }
}
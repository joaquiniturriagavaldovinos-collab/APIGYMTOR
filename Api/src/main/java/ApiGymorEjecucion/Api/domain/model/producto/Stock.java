package ApiGymorEjecucion.Api.domain.model.producto;

import java.util.Objects;

/**
 * Value Object: Stock de un producto
 * Inmutable - cada cambio genera una nueva instancia
 * Diseñado para CQRS Liviano con todas las reglas de negocio
 */
public class Stock {
    private final int cantidad;
    private final int cantidadReservada;
    private final int cantidadDisponible;

    // Constantes de negocio
    private static final int CANTIDAD_MAXIMA = 999999;
    private static final int UMBRAL_STOCK_CRITICO = 5;

    private Stock(int cantidad, int cantidadReservada) {
        validarInvariantes(cantidad, cantidadReservada);

        this.cantidad = cantidad;
        this.cantidadReservada = cantidadReservada;
        this.cantidadDisponible = cantidad - cantidadReservada;
    }

    // ===== FACTORY METHODS =====

    /**
     * Crea stock sin reservas (caso común)
     */
    public static Stock crear(int cantidad) {
        return new Stock(cantidad, 0);
    }

    /**
     * Crea stock con reservas existentes (para reconstrucción)
     */
    public static Stock crearConReservas(int cantidad, int cantidadReservada) {
        return new Stock(cantidad, cantidadReservada);
    }

    /**
     * Crea stock inicial vacío
     */
    public static Stock vacio() {
        return new Stock(0, 0);
    }

    // ===== OPERACIONES INMUTABLES =====

    /**
     * Incrementa la cantidad total (reposición de inventario)
     */
    public Stock incrementar(int cantidadAIncrementar) {
        validarCantidadPositiva(cantidadAIncrementar, "incrementar");
        validarNoExcederMaximo(cantidadAIncrementar);

        return new Stock(
                this.cantidad + cantidadAIncrementar,
                this.cantidadReservada
        );
    }

    /**
     * Decrementa la cantidad total (venta confirmada)
     * Reduce tanto del total como del disponible
     */
    public Stock decrementar(int cantidadADecrementar) {
        validarCantidadPositiva(cantidadADecrementar, "decrementar");
        validarStockDisponibleSuficiente(cantidadADecrementar);

        return new Stock(
                this.cantidad - cantidadADecrementar,
                this.cantidadReservada
        );
    }

    /**
     * Reserva stock disponible (carrito, cotización)
     * Mueve stock de disponible a reservado
     */
    public Stock reservar(int cantidadAReservar) {
        validarCantidadPositiva(cantidadAReservar, "reservar");
        validarStockDisponibleSuficiente(cantidadAReservar);

        return new Stock(
                this.cantidad,
                this.cantidadReservada + cantidadAReservar
        );
    }

    /**
     * Libera reserva (cancelación, timeout)
     * Devuelve stock de reservado a disponible
     */
    public Stock liberarReserva(int cantidadALiberar) {
        validarCantidadPositiva(cantidadALiberar, "liberar");
        validarReservaSuficiente(cantidadALiberar);

        return new Stock(
                this.cantidad,
                this.cantidadReservada - cantidadALiberar
        );
    }

    /**
     * Confirma una reserva como venta
     * Reduce el stock total y libera la reserva
     */
    public Stock confirmarReserva(int cantidadAConfirmar) {
        validarCantidadPositiva(cantidadAConfirmar, "confirmar");
        validarReservaSuficiente(cantidadAConfirmar);

        return new Stock(
                this.cantidad - cantidadAConfirmar,
                this.cantidadReservada - cantidadAConfirmar
        );
    }

    /**
     * Ajusta el stock total (inventario físico)
     * Resetea las reservas si el nuevo stock es menor
     */
    public Stock ajustarInventario(int nuevaCantidad) {
        if (nuevaCantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }

        int nuevasReservas = this.cantidadReservada;
        if (nuevaCantidad < this.cantidadReservada) {
            // Si el nuevo stock es menor que las reservas, ajustar
            nuevasReservas = nuevaCantidad;
        }

        return new Stock(nuevaCantidad, nuevasReservas);
    }

    // ===== QUERIES - CONSULTAS DE NEGOCIO =====

    /**
     * Verifica si hay stock disponible suficiente
     */
    public boolean tieneStockDisponible(int cantidadRequerida) {
        return this.cantidadDisponible >= cantidadRequerida;
    }

    /**
     * Verifica si el stock está agotado
     */
    public boolean estaAgotado() {
        return this.cantidadDisponible == 0;
    }

    /**
     * Verifica si hay stock total (incluyendo reservas)
     */
    public boolean tieneStockTotal() {
        return this.cantidad > 0;
    }

    /**
     * Verifica si tiene reservas activas
     */
    public boolean tieneReservas() {
        return this.cantidadReservada > 0;
    }

    /**
     * Verifica si el stock está en nivel crítico
     */
    public boolean esStockCritico() {
        return this.cantidadDisponible > 0 &&
                this.cantidadDisponible <= UMBRAL_STOCK_CRITICO;
    }

    /**
     * Calcula el porcentaje de stock disponible
     */
    public double calcularPorcentajeDisponible() {
        if (this.cantidad == 0) {
            return 0.0;
        }
        return (double) this.cantidadDisponible / this.cantidad * 100;
    }

    /**
     * Calcula el porcentaje de stock reservado
     */
    public double calcularPorcentajeReservado() {
        if (this.cantidad == 0) {
            return 0.0;
        }
        return (double) this.cantidadReservada / this.cantidad * 100;
    }

    /**
     * Verifica si el stock necesita reposición (menos del 30%)
     */
    public boolean necesitaReposicion() {
        return calcularPorcentajeDisponible() < 30.0;
    }

    // ===== VALIDACIONES PRIVADAS =====

    private void validarInvariantes(int cantidad, int cantidadReservada) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        if (cantidadReservada < 0) {
            throw new IllegalArgumentException("La cantidad reservada no puede ser negativa");
        }
        if (cantidadReservada > cantidad) {
            throw new IllegalArgumentException(
                    String.format("La cantidad reservada (%d) no puede ser mayor a la cantidad total (%d)",
                            cantidadReservada, cantidad)
            );
        }
        if (cantidad > CANTIDAD_MAXIMA) {
            throw new IllegalArgumentException(
                    String.format("La cantidad no puede exceder el máximo permitido (%d)",
                            CANTIDAD_MAXIMA)
            );
        }
    }

    private void validarCantidadPositiva(int cantidad, String operacion) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    String.format("La cantidad a %s debe ser mayor a cero", operacion)
            );
        }
    }

    private void validarStockDisponibleSuficiente(int cantidadRequerida) {
        if (this.cantidadDisponible < cantidadRequerida) {
            throw new IllegalArgumentException(
                    String.format("Stock disponible insuficiente. Disponible: %d, Solicitado: %d",
                            this.cantidadDisponible, cantidadRequerida)
            );
        }
    }

    private void validarReservaSuficiente(int cantidadRequerida) {
        if (this.cantidadReservada < cantidadRequerida) {
            throw new IllegalArgumentException(
                    String.format("No hay suficientes reservas. Reservado: %d, Solicitado: %d",
                            this.cantidadReservada, cantidadRequerida)
            );
        }
    }

    private void validarNoExcederMaximo(int cantidadAIncrementar) {
        long nuevoTotal = (long) this.cantidad + cantidadAIncrementar;
        if (nuevoTotal > CANTIDAD_MAXIMA) {
            throw new IllegalArgumentException(
                    String.format("El incremento excede la cantidad máxima permitida (%d)",
                            CANTIDAD_MAXIMA)
            );
        }
    }

    // ===== GETTERS =====

    public int getCantidad() {
        return cantidad;
    }

    public int getCantidadReservada() {
        return cantidadReservada;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    // ===== VALUE OBJECT IDENTITY =====

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return cantidad == stock.cantidad &&
                cantidadReservada == stock.cantidadReservada;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cantidad, cantidadReservada);
    }

    @Override
    public String toString() {
        return String.format(
                "Stock{total=%d, reservado=%d, disponible=%d (%.1f%%)}",
                cantidad, cantidadReservada, cantidadDisponible,
                calcularPorcentajeDisponible()
        );
    }
}
package ApiGymorEjecucion.Api.domain.model.Pago;

/**
 * Estados posibles de un pago
 */
public enum EstadoPago {
    PENDIENTE("Pendiente"),
    PROCESANDO("Procesando"),
    EXITOSO("Exitoso"),
    RECHAZADO("Rechazado"),
    CANCELADO("Cancelado"),
    REEMBOLSADO("Reembolsado");

    private final String descripcion;

    EstadoPago(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean esFinal() {
        return this == EXITOSO || this == RECHAZADO || this == CANCELADO || this == REEMBOLSADO;
    }

    public boolean esExitoso() {
        return this == EXITOSO;
    }
}
package ApiGymorEjecucion.Api.domain.model.pedido;


/**
 * Estados del ciclo de vida de un Pedido.
 * Representa la máquina de estados del negocio según especificación.
 */
public enum EstadoPedido {
    /**
     * Pedido creado, sin pago iniciado
     */
    CREATED("Creado"),

    /**
     * Pago en proceso con la pasarela
     */
    PAYMENT_PENDING("Pago Pendiente"),

    /**
     * Pago confirmado exitosamente
     */
    PAID("Pagado"),

    /**
     * Pedido en preparación logística
     */
    PREPARING("En Preparación"),

    /**
     * Pedido despachado/enviado
     */
    DISPATCHED("Despachado"),

    /**
     * Pedido entregado al cliente (ESTADO FINAL)
     */
    DELIVERED("Entregado"),

    /**
     * Pago fallido (ESTADO FINAL)
     */
    FAILED("Fallido"),

    /**
     * Pedido cancelado (ESTADO FINAL)
     */
    CANCELLED("Cancelado");

    private final String descripcion;

    EstadoPedido(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Verifica si el estado es final (no permite más transiciones)
     */
    public boolean esFinal() {
        return this == DELIVERED || this == FAILED || this == CANCELLED;
    }

    /**
     * Verifica si el estado permite cancelación
     */
    public boolean permiteCanelacion() {
        return this == CREATED || this == PAYMENT_PENDING || this == PAID;
    }
}
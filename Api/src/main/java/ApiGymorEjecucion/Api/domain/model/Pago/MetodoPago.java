package ApiGymorEjecucion.Api.domain.model.Pago;


/**
 * Métodos de pago disponibles en el sistema
 */
public enum MetodoPago {
    TARJETA_CREDITO("Tarjeta de Crédito"),
    TARJETA_DEBITO("Tarjeta de Débito"),
    TRANSFERENCIA_BANCARIA("Transferencia Bancaria"),
    WEBPAY("Webpay"),
    MERCADO_PAGO("Mercado Pago"),
    EFECTIVO("Efectivo");

    private final String descripcion;

    MetodoPago(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Indica si el método requiere procesamiento externo
     */
    public boolean requierePasarelaExterna() {
        return this == TARJETA_CREDITO
                || this == TARJETA_DEBITO
                || this == WEBPAY
                || this == MERCADO_PAGO;
    }

    /**
     * Indica si el método es instantáneo
     */
    public boolean esInstantaneo() {
        return this != TRANSFERENCIA_BANCARIA && this != EFECTIVO;
    }
}
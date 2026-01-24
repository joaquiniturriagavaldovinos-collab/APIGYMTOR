package ApiGymorEjecucion.Api.domain.model.Pago;


import java.util.Arrays;

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


    public static MetodoPago fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El método de pago es obligatorio");
        }
        try {
            // Normaliza: reemplaza espacios por guiones bajos y convierte a mayúsculas
            String normalized = value.trim()
                    .replace(" ", "_")
                    .toUpperCase();
            return MetodoPago.valueOf(normalized);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Método de pago inválido: " + value +
                            ". Valores permitidos: " + Arrays.toString(MetodoPago.values())
            );
        }
    }
}
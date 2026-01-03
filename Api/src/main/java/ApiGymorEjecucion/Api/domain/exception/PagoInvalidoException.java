package ApiGymorEjecucion.Api.domain.exception;


/**
 * Excepción lanzada cuando se intenta procesar un pago inválido
 */
public class PagoInvalidoException extends RuntimeException {

    private final String pagoId;

    public PagoInvalidoException(String mensaje) {
        super(mensaje);
        this.pagoId = null;
    }

    public PagoInvalidoException(String pagoId, String mensaje) {
        super(mensaje);
        this.pagoId = pagoId;
    }

    public PagoInvalidoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
        this.pagoId = null;
    }

    public String getPagoId() {
        return pagoId;
    }
}
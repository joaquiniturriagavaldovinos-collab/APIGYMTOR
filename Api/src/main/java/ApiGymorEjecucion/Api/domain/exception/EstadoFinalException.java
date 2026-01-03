package ApiGymorEjecucion.Api.domain.exception;

/**
 * Excepción lanzada cuando se intenta modificar un pedido en estado final.
 * Los estados finales son: DELIVERED, FAILED, CANCELLED
 */
public class EstadoFinalException extends RuntimeException {

    public EstadoFinalException(String mensaje) {
        super(mensaje);
    }

    public EstadoFinalException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
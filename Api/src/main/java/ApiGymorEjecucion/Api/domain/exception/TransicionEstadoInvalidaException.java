package ApiGymorEjecucion.Api.domain.exception;

/**
 * Excepción lanzada cuando se intenta una transición de estado inválida.
 * Ejemplo: intentar pasar de DELIVERED a PREPARING
 */
public class TransicionEstadoInvalidaException extends RuntimeException {

    public TransicionEstadoInvalidaException(String mensaje) {
        super(mensaje);
    }

    public TransicionEstadoInvalidaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
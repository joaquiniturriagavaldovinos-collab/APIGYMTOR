package ApiGymorEjecucion.Api.domain.exception;

import ApiGymorEjecucion.Api.domain.model.pedido.EstadoPedido;

/**
 * Excepción lanzada cuando se intenta una transición de estado inválida.
 * Ejemplo: intentar pasar de DELIVERED a PREPARING
 */
public class TransicionEstadoInvalidaException extends RuntimeException {

    private final EstadoPedido estadoActual;
    private final EstadoPedido nuevoEstado;

    public TransicionEstadoInvalidaException(
            EstadoPedido estadoActual,
            EstadoPedido nuevoEstado,
            String mensaje
    ) {
        super(mensaje);
        this.estadoActual = estadoActual;
        this.nuevoEstado = nuevoEstado;
    }

    public EstadoPedido getEstadoActual() {
        return estadoActual;
    }

    public EstadoPedido getNuevoEstado() {
        return nuevoEstado;
    }
}

package ApiGymorEjecucion.Api.domain.exception;

/**
 * Excepción lanzada cuando se busca un pedido que no existe
 */
public class PedidoNoEncontradoException extends RuntimeException {

    private final String pedidoId;

    public PedidoNoEncontradoException(String pedidoId) {
        super(String.format("No se encontró el pedido con ID: %s", pedidoId));
        this.pedidoId = pedidoId;
    }

    public String getPedidoId() {
        return pedidoId;
    }
}
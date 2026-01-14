package ApiGymorEjecucion.Api.application.dto.request.pedido;

import java.util.List;

/**
 * DTO para solicitud de creación de pedido
 */
public class CrearPedidoRequest {
    private String pedidoId;
    private String clienteId;
    private List<ItemPedidoRequest> items;

    // Constructors
    public CrearPedidoRequest() {
    }

    public CrearPedidoRequest(String pedidoId, String clienteId, List<ItemPedidoRequest> items) {
        this.pedidoId = pedidoId;
        this.clienteId = clienteId;
        this.items = items;
    }

    // Getters y Setters
    public String getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(String pedidoId) {
        this.pedidoId = pedidoId;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public List<ItemPedidoRequest> getItems() {
        return items;
    }

    public void setItems(List<ItemPedidoRequest> items) {
        this.items = items;
    }
}
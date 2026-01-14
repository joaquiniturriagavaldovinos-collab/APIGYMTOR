package ApiGymorEjecucion.Api.application.dto.request.pedido;


/**
 * DTO para solicitud de preparación de pedido
 */
public class PrepararPedidoRequest {
    private String pedidoId;
    private String operadorId;
    private String observaciones;

    // Constructors
    public PrepararPedidoRequest() {
    }

    public PrepararPedidoRequest(String pedidoId, String operadorId, String observaciones) {
        this.pedidoId = pedidoId;
        this.operadorId = operadorId;
        this.observaciones = observaciones;
    }

    // Getters y Setters
    public String getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(String pedidoId) {
        this.pedidoId = pedidoId;
    }

    public String getOperadorId() {
        return operadorId;
    }

    public void setOperadorId(String operadorId) {
        this.operadorId = operadorId;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
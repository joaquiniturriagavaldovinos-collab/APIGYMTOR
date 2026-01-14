package ApiGymorEjecucion.Api.application.dto.request.pedido;


/**
 * DTO para solicitud de despacho de pedido
 */
public class DespacharPedidoRequest {
    private String pedidoId;
    private String guiaDespacho;
    private String transportista;

    // Constructors
    public DespacharPedidoRequest() {
    }

    public DespacharPedidoRequest(String pedidoId, String guiaDespacho, String transportista) {
        this.pedidoId = pedidoId;
        this.guiaDespacho = guiaDespacho;
        this.transportista = transportista;
    }

    // Getters y Setters
    public String getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(String pedidoId) {
        this.pedidoId = pedidoId;
    }

    public String getGuiaDespacho() {
        return guiaDespacho;
    }

    public void setGuiaDespacho(String guiaDespacho) {
        this.guiaDespacho = guiaDespacho;
    }

    public String getTransportista() {
        return transportista;
    }

    public void setTransportista(String transportista) {
        this.transportista = transportista;
    }
}
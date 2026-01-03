package ApiGymorEjecucion.Api.application.dto.request;
/**
 * DTO para solicitud de inicio de pago
 */
public class IniciarPagoRequest {
    private String pedidoId;
    private String metodoPago; // "TARJETA", "TRANSFERENCIA", etc.

    // Constructors
    public IniciarPagoRequest() {
    }

    public IniciarPagoRequest(String pedidoId, String metodoPago) {
        this.pedidoId = pedidoId;
        this.metodoPago = metodoPago;
    }

    // Getters y Setters
    public String getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(String pedidoId) {
        this.pedidoId = pedidoId;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
}
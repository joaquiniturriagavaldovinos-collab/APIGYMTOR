package ApiGymorEjecucion.Api.application.dto.request;

/**
 * DTO para webhook de confirmación de pago
 */
public class ConfirmarPagoRequest {
    private String pedidoId;
    private boolean exitoso;
    private String referenciaPago;
    private String motivoFallo;

    // Constructors
    public ConfirmarPagoRequest() {
    }

    public ConfirmarPagoRequest(String pedidoId, boolean exitoso,
                                String referenciaPago, String motivoFallo) {
        this.pedidoId = pedidoId;
        this.exitoso = exitoso;
        this.referenciaPago = referenciaPago;
        this.motivoFallo = motivoFallo;
    }

    // Getters y Setters
    public String getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(String pedidoId) {
        this.pedidoId = pedidoId;
    }

    public boolean isExitoso() {
        return exitoso;
    }

    public void setExitoso(boolean exitoso) {
        this.exitoso = exitoso;
    }

    public String getReferenciaPago() {
        return referenciaPago;
    }

    public void setReferenciaPago(String referenciaPago) {
        this.referenciaPago = referenciaPago;
    }

    public String getMotivoFallo() {
        return motivoFallo;
    }

    public void setMotivoFallo(String motivoFallo) {
        this.motivoFallo = motivoFallo;
    }
}


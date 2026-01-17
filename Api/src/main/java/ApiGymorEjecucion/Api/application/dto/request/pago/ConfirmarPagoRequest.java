package ApiGymorEjecucion.Api.application.dto.request.pago;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para webhook de confirmación de pago
 */
public class ConfirmarPagoRequest {

    @NotBlank(message = "El ID del pedido es obligatorio")
    private String pedidoId;

    /**
     * Indica si el pago fue exitoso o no
     */
    private boolean exitoso;

    @Size(max = 100, message = "La referencia de pago no puede superar los 100 caracteres")
    private String referenciaPago;

    @Size(max = 255, message = "El motivo del fallo no puede superar los 255 caracteres")
    private String motivoFallo;

    // Constructors

    public ConfirmarPagoRequest() {
    }

    public ConfirmarPagoRequest(
            String pedidoId,
            boolean exitoso,
            String referenciaPago,
            String motivoFallo
    ) {
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

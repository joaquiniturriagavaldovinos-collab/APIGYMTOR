package ApiGymorEjecucion.Api.application.dto.request.pago;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para webhook de confirmación de pago
 */
public class ConfirmarPagoRequest {

    @NotBlank(message = "El ID del pedido es obligatorio")
    private String pedidoId;

    /**
     * Estado del pago: APROBADO, RECHAZADO, FALLIDO
     */
    @NotBlank(message = "El estado del pago es obligatorio")
    private String estadoPago;

    @Size(max = 100, message = "La referencia no puede superar los 100 caracteres")
    @JsonProperty("referencia") //  Acepta "referencia" o "referenciaPago"
    private String referenciaPago;

    @Size(max = 255, message = "El motivo del fallo no puede superar los 255 caracteres")
    private String motivoFallo;

    // Constructors
    public ConfirmarPagoRequest() {
    }

    public ConfirmarPagoRequest(String pedidoId, String estadoPago, String referenciaPago, String motivoFallo) {
        this.pedidoId = pedidoId;
        this.estadoPago = estadoPago;
        this.referenciaPago = referenciaPago;
        this.motivoFallo = motivoFallo;
    }

    // Método de utilidad
    public boolean isExitoso() {
        return "APROBADO".equalsIgnoreCase(estadoPago);
    }

    // Getters y Setters
    public String getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(String pedidoId) {
        this.pedidoId = pedidoId;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
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
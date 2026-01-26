package ApiGymorEjecucion.Api.application.dto.request.pago;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ConfirmarPagoRequest {

    @NotBlank(message = "La referencia de pago es obligatoria")
    @JsonProperty("referenciaPago")
    private String referenciaPago;

    @NotBlank(message = "El estado del pago es obligatorio")
    private String estadoPago;

    @Size(max = 100)
    private String codigoAutorizacion;

    @Size(max = 255)
    private String motivoFallo;

    // Constructors
    public ConfirmarPagoRequest() {}

    public ConfirmarPagoRequest(String referenciaPago, String estadoPago,
                                String codigoAutorizacion, String motivoFallo) {
        this.referenciaPago = referenciaPago;
        this.estadoPago = estadoPago;
        this.codigoAutorizacion = codigoAutorizacion;
        this.motivoFallo = motivoFallo;
    }

    /**
     * Acepta múltiples variantes de estado exitoso
     */
    public boolean isExitoso() {
        if (estadoPago == null) return false;

        String estado = estadoPago.trim().toUpperCase();

        return estado.equals("APROBADO")
                || estado.equals("EXITOSO")
                || estado.equals("APPROVED")
                || estado.equals("SUCCESS")
                || estado.equals("PAID")
                || estado.equals("COMPLETADO");
    }

    // Getters y Setters
    public String getReferenciaPago() { return referenciaPago; }
    public void setReferenciaPago(String referenciaPago) {
        this.referenciaPago = referenciaPago;
    }

    public String getEstadoPago() { return estadoPago; }
    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public String getCodigoAutorizacion() { return codigoAutorizacion; }
    public void setCodigoAutorizacion(String codigoAutorizacion) {
        this.codigoAutorizacion = codigoAutorizacion;
    }

    public String getMotivoFallo() { return motivoFallo; }
    public void setMotivoFallo(String motivoFallo) {
        this.motivoFallo = motivoFallo;
    }
}
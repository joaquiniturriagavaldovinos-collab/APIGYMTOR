package ApiGymorEjecucion.Api.application.dto.request.pago;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para webhook de confirmación de pago
 *
 * La pasarela de pago NO conoce el pedidoId, solo la referencia
 */
public class ConfirmarPagoRequest {

    /**
     * Referencia única del pago generada por nosotros
     * La pasarela nos la devuelve para identificar el pago
     */
    @NotBlank(message = "La referencia de pago es obligatoria")
    @JsonProperty("referenciaPago") // Acepta "referenciaPago" o "referencia"
    private String referenciaPago;

    /**
     * Estado del pago: APROBADO, RECHAZADO, FALLIDO
     */
    @NotBlank(message = "El estado del pago es obligatorio")
    private String estadoPago;

    /**
     * Código de autorización (solo si el pago fue aprobado)
     */
    @Size(max = 100, message = "El código de autorización no puede superar los 100 caracteres")
    private String codigoAutorizacion;

    /**
     * Motivo del rechazo/fallo (solo si el pago fue rechazado)
     */
    @Size(max = 255, message = "El motivo del fallo no puede superar los 255 caracteres")
    private String motivoFallo;

    // Constructors
    public ConfirmarPagoRequest() {
    }

    public ConfirmarPagoRequest(String referenciaPago, String estadoPago,
                                String codigoAutorizacion, String motivoFallo) {
        this.referenciaPago = referenciaPago;
        this.estadoPago = estadoPago;
        this.codigoAutorizacion = codigoAutorizacion;
        this.motivoFallo = motivoFallo;
    }

    // Método de utilidad
    public boolean isExitoso() {
        return "APROBADO".equalsIgnoreCase(estadoPago);
    }

    // Getters y Setters
    public String getReferenciaPago() {
        return referenciaPago;
    }

    public void setReferenciaPago(String referenciaPago) {
        this.referenciaPago = referenciaPago;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public String getCodigoAutorizacion() {
        return codigoAutorizacion;
    }

    public void setCodigoAutorizacion(String codigoAutorizacion) {
        this.codigoAutorizacion = codigoAutorizacion;
    }

    public String getMotivoFallo() {
        return motivoFallo;
    }

    public void setMotivoFallo(String motivoFallo) {
        this.motivoFallo = motivoFallo;
    }
}
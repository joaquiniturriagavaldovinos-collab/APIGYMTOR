package ApiGymorEjecucion.Api.application.dto.request.suscripcion;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para solicitud de contratación de suscripción
 */
public class ContratarSuscripcionRequest {

    @NotBlank(message = "El ID del cliente es obligatorio")
    private String clienteId;

    @NotBlank(message = "El ID del plan es obligatorio")
    private String planId;

    // Getters y Setters

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }
}

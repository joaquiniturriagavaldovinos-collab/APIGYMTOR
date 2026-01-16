package ApiGymorEjecucion.Api.application.dto.request.suscripcion;

public class ContratarSuscripcionRequest {
    private String clienteId;
    private String planId;

    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }

    public String getPlanId() { return planId; }
    public void setPlanId(String planId) { this.planId = planId; }
}
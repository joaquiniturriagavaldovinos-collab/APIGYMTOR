package ApiGymorEjecucion.Api.application.dto.response.suscripcion;

import java.time.LocalDateTime;

public class SuscripcionResponse {

    private String id;
    private String clienteId;
    private String planId;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaVencimiento;
    private int sesionesRestantes;
    private boolean tieneSesionesIlimitadas;
    private boolean activa;
    private boolean autorrenovable;
    private boolean estaVigente;
    private boolean estaVencida;
    private long diasRestantes;

    // ===== GETTERS & SETTERS =====

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDateTime fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public int getSesionesRestantes() {
        return sesionesRestantes;
    }

    public void setSesionesRestantes(int sesionesRestantes) {
        this.sesionesRestantes = sesionesRestantes;
    }

    public boolean isTieneSesionesIlimitadas() {
        return tieneSesionesIlimitadas;
    }

    public void setTieneSesionesIlimitadas(boolean tieneSesionesIlimitadas) {
        this.tieneSesionesIlimitadas = tieneSesionesIlimitadas;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public boolean isAutorrenovable() {
        return autorrenovable;
    }

    public void setAutorrenovable(boolean autorrenovable) {
        this.autorrenovable = autorrenovable;
    }

    public boolean isEstaVigente() {
        return estaVigente;
    }

    public void setEstaVigente(boolean estaVigente) {
        this.estaVigente = estaVigente;
    }

    public boolean isEstaVencida() {
        return estaVencida;
    }

    public void setEstaVencida(boolean estaVencida) {
        this.estaVencida = estaVencida;
    }

    public long getDiasRestantes() {
        return diasRestantes;
    }

    public void setDiasRestantes(long diasRestantes) {
        this.diasRestantes = diasRestantes;
    }
}

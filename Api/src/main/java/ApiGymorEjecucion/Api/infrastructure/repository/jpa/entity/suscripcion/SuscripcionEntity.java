package ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.suscripcion;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "suscripciones")
public class SuscripcionEntity {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "cliente_id", nullable = false, length = 36)
    private String clienteId;

    @Column(name = "plan_id", nullable = false, length = 36)
    private String planId;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDateTime fechaVencimiento;

    @Column(name = "sesiones_restantes", nullable = false)
    private int sesionesRestantes;

    @Column(name = "activa", nullable = false)
    private boolean activa;

    @Column(name = "autorrenovable", nullable = false)
    private boolean autorrenovable;

    @Column(name = "fecha_contratacion", nullable = false, updatable = false)
    private LocalDateTime fechaContratacion;

    // Constructor vacío para JPA
    protected SuscripcionEntity() {
    }

    // Constructor completo
    public SuscripcionEntity(
            String id,
            String clienteId,
            String planId,
            LocalDateTime fechaInicio,
            LocalDateTime fechaVencimiento,
            int sesionesRestantes,
            boolean activa,
            boolean autorrenovable,
            LocalDateTime fechaContratacion
    ) {
        this.id = id;
        this.clienteId = clienteId;
        this.planId = planId;
        this.fechaInicio = fechaInicio;
        this.fechaVencimiento = fechaVencimiento;
        this.sesionesRestantes = sesionesRestantes;
        this.activa = activa;
        this.autorrenovable = autorrenovable;
        this.fechaContratacion = fechaContratacion;
    }

    // ===== GETTERS =====

    public String getId() {
        return id;
    }

    public String getClienteId() {
        return clienteId;
    }

    public String getPlanId() {
        return planId;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public LocalDateTime getFechaVencimiento() {
        return fechaVencimiento;
    }

    public int getSesionesRestantes() {
        return sesionesRestantes;
    }

    public boolean isActiva() {
        return activa;
    }

    public boolean isAutorrenovable() {
        return autorrenovable;
    }

    public LocalDateTime getFechaContratacion() {
        return fechaContratacion;
    }

    // ===== SETTERS (para JPA) =====

    public void setId(String id) {
        this.id = id;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaVencimiento(LocalDateTime fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public void setSesionesRestantes(int sesionesRestantes) {
        this.sesionesRestantes = sesionesRestantes;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public void setAutorrenovable(boolean autorrenovable) {
        this.autorrenovable = autorrenovable;
    }

    public void setFechaContratacion(LocalDateTime fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }
}
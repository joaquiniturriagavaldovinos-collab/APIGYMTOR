package ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity;

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

    protected SuscripcionEntity() {
        // JPA
    }

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
}

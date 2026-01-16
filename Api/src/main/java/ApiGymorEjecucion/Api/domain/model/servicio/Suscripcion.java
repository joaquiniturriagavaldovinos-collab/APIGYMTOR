package ApiGymorEjecucion.Api.domain.model.servicio;


import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad: Suscripción
 * Representa un plan contratado por un cliente (PlanContratado)
 */
public class Suscripcion {
    private final String id;
    private final String clienteId;
    private final String planId;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaVencimiento;
    private int sesionesRestantes; // -1 = ilimitadas
    private boolean activa;
    private boolean autorrenovable;
    private final LocalDateTime fechaContratacion;

    private Suscripcion(String id, String clienteId, String planId,
                        LocalDateTime fechaInicio, LocalDateTime fechaVencimiento,
                        int sesionesRestantes) {
        this.id = id;
        this.clienteId = clienteId;
        this.planId = planId;
        this.fechaInicio = fechaInicio;
        this.fechaVencimiento = fechaVencimiento;
        this.sesionesRestantes = sesionesRestantes;
        this.activa = true;
        this.autorrenovable = false;
        this.fechaContratacion = LocalDateTime.now();
    }

    /**
     * Factory method: Crea una suscripción basada en un plan
     */
    public static Suscripcion crear(String id, String clienteId, String planId,
                                    int duracionMeses, int sesionesIncluidas) {
        validarDatos(id, clienteId, planId, duracionMeses);

        LocalDateTime fechaInicio = LocalDateTime.now();
        LocalDateTime fechaVencimiento = fechaInicio.plusMonths(duracionMeses);
        int sesiones = sesionesIncluidas == 0 ? -1 : sesionesIncluidas; // -1 = ilimitadas

        return new Suscripcion(id, clienteId, planId, fechaInicio, fechaVencimiento, sesiones);
    }

    private static void validarDatos(String id, String clienteId, String planId, int duracionMeses) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID de la suscripción es requerido");
        }
        if (clienteId == null || clienteId.isBlank()) {
            throw new IllegalArgumentException("El ID del cliente es requerido");
        }
        if (planId == null || planId.isBlank()) {
            throw new IllegalArgumentException("El ID del plan es requerido");
        }
        if (duracionMeses <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor a cero");
        }
    }

    // ===== MÉTODOS DE NEGOCIO =====

    /**
     * Activa la suscripción
     */
    public void activar() {
        if (estaVencida()) {
            throw new IllegalStateException("No se puede activar una suscripción vencida");
        }
        this.activa = true;
    }

    /**
     * Suspende la suscripción
     */
    public void suspender() {
        this.activa = false;
    }

    /**
     * Cancela la suscripción
     */
    public void cancelar() {
        this.activa = false;
        this.autorrenovable = false;
    }

    /**
     * Consume una sesión de la suscripción
     */
    public void consumirSesion() {
        if (!this.activa) {
            throw new IllegalStateException("La suscripción no está activa");
        }
        if (estaVencida()) {
            throw new IllegalStateException("La suscripción está vencida");
        }
        if (!tieneSesionesIlimitadas() && this.sesionesRestantes <= 0) {
            throw new IllegalStateException("No quedan sesiones disponibles");
        }

        if (!tieneSesionesIlimitadas()) {
            this.sesionesRestantes--;
        }
    }

    /**
     * Renueva la suscripción por el mismo período
     */
    public void renovar(int duracionMeses) {
        if (duracionMeses <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor a cero");
        }

        // Si ya venció, se renueva desde ahora
        if (estaVencida()) {
            this.fechaInicio = LocalDateTime.now();
            this.fechaVencimiento = this.fechaInicio.plusMonths(duracionMeses);
        } else {
            // Si está vigente, extiende desde la fecha de vencimiento actual
            this.fechaVencimiento = this.fechaVencimiento.plusMonths(duracionMeses);
        }

        this.activa = true;
    }

    /**
     * Habilita la autorenovación
     */
    public void habilitarAutorenovacion() {
        this.autorrenovable = true;
    }

    /**
     * Deshabilita la autorenovación
     */
    public void deshabilitarAutorenovacion() {
        this.autorrenovable = false;
    }

    // ===== MÉTODOS DE CONSULTA =====

    public boolean estaVencida() {
        return LocalDateTime.now().isAfter(this.fechaVencimiento);
    }

    public boolean estaVigente() {
        return this.activa && !estaVencida();
    }

    public boolean tieneSesionesIlimitadas() {
        return this.sesionesRestantes == -1;
    }

    public boolean tieneSesionesDisponibles() {
        return tieneSesionesIlimitadas() || this.sesionesRestantes > 0;
    }

    public long diasRestantes() {
        if (estaVencida()) {
            return 0;
        }
        return java.time.Duration.between(LocalDateTime.now(), fechaVencimiento).toDays();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Suscripcion that = (Suscripcion) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Suscripcion{id='%s', clienteId='%s', activa=%s, vence=%s}",
                id, clienteId, activa, fechaVencimiento);
    }

    private Suscripcion(String id,
                        String clienteId,
                        String planId,
                        LocalDateTime fechaInicio,
                        LocalDateTime fechaVencimiento,
                        int sesionesRestantes,
                        LocalDateTime fechaContratacion) {

        this.id = id;
        this.clienteId = clienteId;
        this.planId = planId;
        this.fechaInicio = fechaInicio;
        this.fechaVencimiento = fechaVencimiento;
        this.sesionesRestantes = sesionesRestantes;
        this.activa = true;
        this.autorrenovable = false;
        this.fechaContratacion = fechaContratacion;
    }


    public static Suscripcion crearDesdePersistencia(
            String id,
            String clienteId,
            String planId,
            LocalDateTime fechaInicio,
            LocalDateTime fechaVencimiento,
            int sesionesRestantes,
            LocalDateTime fechaContratacion
    ) {
        return new Suscripcion(
                id,
                clienteId,
                planId,
                fechaInicio,
                fechaVencimiento,
                sesionesRestantes,
                fechaContratacion
        );
    }

}
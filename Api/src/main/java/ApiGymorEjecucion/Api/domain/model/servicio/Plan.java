package ApiGymorEjecucion.Api.domain.model.servicio;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad: Plan
 * Representa un plan de suscripción (mensual, trimestral, anual)
 */
public class Plan {
    private final String id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private int duracionMeses;
    private int sesionesIncluidas; // 0 = ilimitadas
    private boolean activo;
    private LocalDateTime fechaCreacion;

    private Plan(String id, String nombre, String descripcion, BigDecimal precio,
                 int duracionMeses, int sesionesIncluidas) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.duracionMeses = duracionMeses;
        this.sesionesIncluidas = sesionesIncluidas;
        this.activo = true;
        this.fechaCreacion = LocalDateTime.now();
    }

    public static Plan reconstruir(
            String id,
            String nombre,
            String descripcion,
            BigDecimal precio,
            int duracionMeses,
            int sesionesIncluidas,
            boolean activo,
            LocalDateTime fechaCreacion
    ) {
        return new Plan(
                id,
                nombre,
                descripcion,
                precio,
                duracionMeses,
                sesionesIncluidas,
                activo,
                fechaCreacion
        );
    }
    private Plan(
            String id,
            String nombre,
            String descripcion,
            BigDecimal precio,
            int duracionMeses,
            int sesionesIncluidas,
            boolean activo,
            LocalDateTime fechaCreacion
    ) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.duracionMeses = duracionMeses;
        this.sesionesIncluidas = sesionesIncluidas;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
    }


    public static Plan crear(String id, String nombre, String descripcion,
                             BigDecimal precio, int duracionMeses, int sesionesIncluidas) {
        validarDatos(id, nombre, precio, duracionMeses, sesionesIncluidas);
        return new Plan(id, nombre, descripcion, precio, duracionMeses, sesionesIncluidas);
    }

    private static void validarDatos(String id, String nombre, BigDecimal precio,
                                     int duracionMeses, int sesionesIncluidas) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID del plan es requerido");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del plan es requerido");
        }
        if (precio == null || precio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }
        if (duracionMeses <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor a cero");
        }
        if (sesionesIncluidas < 0) {
            throw new IllegalArgumentException("Las sesiones incluidas no pueden ser negativas");
        }
    }

    // Métodos de negocio
    public void actualizarPrecio(BigDecimal nuevoPrecio) {
        if (nuevoPrecio == null || nuevoPrecio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }
        this.precio = nuevoPrecio;
    }

    public void desactivar() {
        this.activo = false;
    }

    public void activar() {
        this.activo = true;
    }

    public boolean esSesionesIlimitadas() {
        return this.sesionesIncluidas == 0;
    }

    public BigDecimal calcularPrecioPorMes() {
        return precio.divide(BigDecimal.valueOf(duracionMeses), 2, java.math.RoundingMode.HALF_UP);
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public int getDuracionMeses() {
        return duracionMeses;
    }

    public int getSesionesIncluidas() {
        return sesionesIncluidas;
    }

    public boolean isActivo() {
        return activo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plan plan = (Plan) o;
        return Objects.equals(id, plan.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Plan{id='%s', nombre='%s', duracion=%d meses, precio=%s}",
                id, nombre, duracionMeses, precio);
    }
}
package ApiGymorEjecucion.Api.domain.model.servicio;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad: Servicio
 * Representa una clase o entrenamiento ofrecido por GYMOR
 */
public class Servicio {
    private final String id;
    private String nombre;
    private String descripcion;
    private ModalidadClase modalidad;
    private BigDecimal precioSesion;
    private int duracionMinutos;
    private int capacidadMaxima;
    private boolean activo;
    private LocalDateTime fechaCreacion;

    private Servicio(String id, String nombre, String descripcion, ModalidadClase modalidad,
                     BigDecimal precioSesion, int duracionMinutos, int capacidadMaxima) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.modalidad = modalidad;
        this.precioSesion = precioSesion;
        this.duracionMinutos = duracionMinutos;
        this.capacidadMaxima = capacidadMaxima;
        this.activo = true;
        this.fechaCreacion = LocalDateTime.now();
    }

    public static Servicio crear(String id, String nombre, String descripcion,
                                 ModalidadClase modalidad, BigDecimal precioSesion,
                                 int duracionMinutos, int capacidadMaxima) {
        validarDatos(id, nombre, modalidad, precioSesion, duracionMinutos, capacidadMaxima);
        return new Servicio(id, nombre, descripcion, modalidad, precioSesion,
                duracionMinutos, capacidadMaxima);
    }

    private static void validarDatos(String id, String nombre, ModalidadClase modalidad,
                                     BigDecimal precioSesion, int duracionMinutos,
                                     int capacidadMaxima) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID del servicio es requerido");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del servicio es requerido");
        }
        if (modalidad == null) {
            throw new IllegalArgumentException("La modalidad es requerida");
        }
        if (precioSesion == null || precioSesion.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }
        if (duracionMinutos <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor a cero");
        }
        if (capacidadMaxima <= 0) {
            throw new IllegalArgumentException("La capacidad máxima debe ser mayor a cero");
        }
    }

    public static Servicio reconstruir(
            String id,
            String nombre,
            String descripcion,
            ModalidadClase modalidad,
            BigDecimal precioSesion,
            int duracionMinutos,
            int capacidadMaxima,
            boolean activo,
            LocalDateTime fechaCreacion
    ) {
        Servicio servicio = new Servicio(
                id,
                nombre,
                descripcion,
                modalidad,
                precioSesion,
                duracionMinutos,
                capacidadMaxima
        );

        servicio.activo = activo;
        servicio.fechaCreacion = fechaCreacion;

        return servicio;
    }


    // Métodos de negocio
    public void actualizarPrecio(BigDecimal nuevoPrecio) {
        if (nuevoPrecio == null || nuevoPrecio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }
        this.precioSesion = nuevoPrecio;
    }

    public void desactivar() {
        this.activo = false;
    }

    public void activar() {
        this.activo = true;
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

    public ModalidadClase getModalidad() {
        return modalidad;
    }

    public BigDecimal getPrecioSesion() {
        return precioSesion;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
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
        Servicio servicio = (Servicio) o;
        return Objects.equals(id, servicio.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Servicio{id='%s', nombre='%s', modalidad=%s}",
                id, nombre, modalidad);
    }
}
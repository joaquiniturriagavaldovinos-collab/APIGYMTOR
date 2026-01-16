package ApiGymorEjecucion.Api.application.dto.response.servicio;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public static class ServicioResponse {
    private String id;
    private String nombre;
    private String descripcion;
    private String modalidad;
    private String modalidadDescripcion;
    private BigDecimal precioSesion;
    private int duracionMinutos;
    private int capacidadMaxima;
    private boolean activo;
    private LocalDateTime fechaCreacion;

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getModalidad() { return modalidad; }
    public void setModalidad(String modalidad) { this.modalidad = modalidad; }

    public String getModalidadDescripcion() { return modalidadDescripcion; }
    public void setModalidadDescripcion(String modalidadDescripcion) {
        this.modalidadDescripcion = modalidadDescripcion;
    }

    public BigDecimal getPrecioSesion() { return precioSesion; }
    public void setPrecioSesion(BigDecimal precioSesion) { this.precioSesion = precioSesion; }

    public int getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(int duracionMinutos) { this.duracionMinutos = duracionMinutos; }

    public int getCapacidadMaxima() { return capacidadMaxima; }
    public void setCapacidadMaxima(int capacidadMaxima) { this.capacidadMaxima = capacidadMaxima; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}

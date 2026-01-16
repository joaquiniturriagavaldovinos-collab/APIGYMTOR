package ApiGymorEjecucion.Api.application.dto.request.servicio;

import java.math.BigDecimal;

// DTOs
public class CrearServicioRequest {
    private String nombre;
    private String descripcion;
    private String modalidad; // PRESENCIAL, ONLINE, HIBRIDO
    private BigDecimal precioSesion;
    private int duracionMinutos;
    private int capacidadMaxima;

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getModalidad() { return modalidad; }
    public void setModalidad(String modalidad) { this.modalidad = modalidad; }

    public BigDecimal getPrecioSesion() { return precioSesion; }
    public void setPrecioSesion(BigDecimal precioSesion) { this.precioSesion = precioSesion; }

    public int getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(int duracionMinutos) { this.duracionMinutos = duracionMinutos; }

    public int getCapacidadMaxima() { return capacidadMaxima; }
    public void setCapacidadMaxima(int capacidadMaxima) { this.capacidadMaxima = capacidadMaxima; }
}

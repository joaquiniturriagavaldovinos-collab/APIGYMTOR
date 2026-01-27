package ApiGymorEjecucion.Api.application.dto.request.servicio;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * DTO para actualización general de servicios
 */
public class ActualizarServicioRequest {

    @Size(max = 100, message = "El nombre del servicio no puede superar los 100 caracteres")
    private String nombre;

    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    private String descripcion;

    /**
     * Modalidad del servicio:
     * - PRESENCIAL
     * - ONLINE
     * - HIBRIDO
     */
    @Pattern(
            regexp = "PRESENCIAL|ONLINE|HIBRIDO",
            message = "La modalidad debe ser PRESENCIAL, ONLINE o HIBRIDO"
    )
    private String modalidad;

    @DecimalMin(value = "0.01", message = "El precio por sesión debe ser mayor a cero")
    private BigDecimal precioSesion;

    @Min(value = 1, message = "La duración debe ser de al menos 1 minuto")
    private Integer duracionMinutos;

    @Min(value = 1, message = "La capacidad máxima debe ser al menos 1 persona")
    private Integer capacidadMaxima;

    // Getters y Setters

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public BigDecimal getPrecioSesion() {
        return precioSesion;
    }

    public void setPrecioSesion(BigDecimal precioSesion) {
        this.precioSesion = precioSesion;
    }

    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public Integer getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(Integer capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }
}
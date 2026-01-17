package ApiGymorEjecucion.Api.application.dto.request.servicio;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * DTO para creación de servicios
 */
public class CrearServicioRequest {

    @NotBlank(message = "El nombre del servicio es obligatorio")
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
    @NotBlank(message = "La modalidad es obligatoria")
    @Pattern(
            regexp = "PRESENCIAL|ONLINE|HIBRIDO",
            message = "La modalidad debe ser PRESENCIAL, ONLINE o HIBRIDO"
    )
    private String modalidad;

    @NotNull(message = "El precio por sesión es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio por sesión debe ser mayor a cero")
    private BigDecimal precioSesion;

    @Min(value = 1, message = "La duración debe ser de al menos 1 minuto")
    private int duracionMinutos;

    @Min(value = 1, message = "La capacidad máxima debe ser al menos 1 persona")
    private int capacidadMaxima;

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

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }
}

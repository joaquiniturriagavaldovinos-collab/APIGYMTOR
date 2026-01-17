package ApiGymorEjecucion.Api.application.dto.request.plan;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * DTO para creación de planes
 */
public class CrearPlanRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a cero")
    private BigDecimal precio;

    @Min(value = 1, message = "La duración del plan debe ser de al menos 1 mes")
    private int duracionMeses;

    /**
     * Cantidad de sesiones incluidas.
     * 0 = sesiones ilimitadas
     */
    @Min(value = 0, message = "Las sesiones incluidas no pueden ser negativas")
    private int sesionesIncluidas;

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

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getDuracionMeses() {
        return duracionMeses;
    }

    public void setDuracionMeses(int duracionMeses) {
        this.duracionMeses = duracionMeses;
    }

    public int getSesionesIncluidas() {
        return sesionesIncluidas;
    }

    public void setSesionesIncluidas(int sesionesIncluidas) {
        this.sesionesIncluidas = sesionesIncluidas;
    }
}

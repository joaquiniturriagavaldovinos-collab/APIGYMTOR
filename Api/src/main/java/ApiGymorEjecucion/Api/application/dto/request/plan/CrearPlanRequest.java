package ApiGymorEjecucion.Api.application.dto.request.plan;

import java.math.BigDecimal;

public class CrearPlanRequest {
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private int duracionMeses;
    private int sesionesIncluidas; // 0 = ilimitadas

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public int getDuracionMeses() { return duracionMeses; }
    public void setDuracionMeses(int duracionMeses) { this.duracionMeses = duracionMeses; }

    public int getSesionesIncluidas() { return sesionesIncluidas; }
    public void setSesionesIncluidas(int sesionesIncluidas) { this.sesionesIncluidas = sesionesIncluidas; }
}

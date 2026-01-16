package ApiGymorEjecucion.Api.application.dto.response.plan;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PlanResponse {
    private String id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private int duracionMeses;
    private int sesionesIncluidas;
    private boolean esSesionesIlimitadas;
    private BigDecimal precioPorMes;
    private boolean activo;
    private LocalDateTime fechaCreacion;

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

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

    public boolean isEsSesionesIlimitadas() { return esSesionesIlimitadas; }
    public void setEsSesionesIlimitadas(boolean esSesionesIlimitadas) {
        this.esSesionesIlimitadas = esSesionesIlimitadas;
    }

    public BigDecimal getPrecioPorMes() { return precioPorMes; }
    public void setPrecioPorMes(BigDecimal precioPorMes) { this.precioPorMes = precioPorMes; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}

package ApiGymorEjecucion.Api.application.dto.request.producto;

public class ActualizarInformacionRequest {
    private String nombre;
    private String descripcion;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}

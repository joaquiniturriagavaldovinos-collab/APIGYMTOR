package ApiGymorEjecucion.Api.application.usecase.cliente;

public class ClienteListResponse {
    private String id;
    private String nombreCompleto;
    private String email;
    private String rut;
    private String tipo;
    private String tipoDescripcion;
    private boolean activo;

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getTipoDescripcion() { return tipoDescripcion; }
    public void setTipoDescripcion(String tipoDescripcion) { this.tipoDescripcion = tipoDescripcion; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
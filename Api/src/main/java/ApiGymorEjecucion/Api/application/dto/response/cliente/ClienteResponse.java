package ApiGymorEjecucion.Api.application.dto.response.cliente;

import java.time.LocalDateTime;

public class ClienteResponse {
    private String id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String rut;
    private String tipo;
    private String tipoDescripcion;
    private boolean activo;
    private LocalDateTime fechaRegistro;
    private String direccionPrincipal;
    private int cantidadDirecciones;

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getTipoDescripcion() { return tipoDescripcion; }
    public void setTipoDescripcion(String tipoDescripcion) { this.tipoDescripcion = tipoDescripcion; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public String getDireccionPrincipal() { return direccionPrincipal; }
    public void setDireccionPrincipal(String direccionPrincipal) { this.direccionPrincipal = direccionPrincipal; }

    public int getCantidadDirecciones() { return cantidadDirecciones; }
    public void setCantidadDirecciones(int cantidadDirecciones) { this.cantidadDirecciones = cantidadDirecciones; }
}

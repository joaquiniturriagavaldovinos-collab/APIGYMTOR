package ApiGymorEjecucion.Api.application.dto.request.cliente;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegistrarClienteRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    private String email;

    @Pattern(
            regexp = "^\\+?[0-9]{8,15}$",
            message = "El teléfono debe tener entre 8 y 15 dígitos y puede incluir prefijo internacional"
    )
    private String telefono;

    @NotBlank(message = "El RUT es obligatorio")
    @Pattern(
            regexp = "^[0-9]{7,8}-[0-9Kk]$",
            message = "El RUT debe tener el formato 12345678-9"
    )
    private String rut;

    /**
     * Tipo de cliente:
     * - MINORISTA
     * - MAYORISTA
     */
    @NotBlank(message = "El tipo de cliente es obligatorio")
    @Pattern(
            regexp = "MINORISTA|MAYORISTA",
            message = "El tipo de cliente debe ser MINORISTA o MAYORISTA"
    )
    private String tipo;

    /**
     * Dirección principal del cliente
     */
    @NotNull(message = "La dirección es obligatoria")
    @Valid
    private DireccionRequest direccion;

    // Getters y Setters

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public DireccionRequest getDireccion() {
        return direccion;
    }

    public void setDireccion(DireccionRequest direccion) {
        this.direccion = direccion;
    }
}

package ApiGymorEjecucion.Api.application.dto.request.cliente;

import ApiGymorEjecucion.Api.application.usecase.cliente.RegistrarClienteUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegistrarClienteRequest {
    @NotBlank(message = "El nombre es requerido")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es requerido")
    @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    private String apellido;

    @NotBlank(message = "El email es requerido")
    @Email(message = "El email no tiene un formato válido")
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "El teléfono no tiene un formato válido")
    private String telefono;

    @NotBlank(message = "El RUT es requerido")
    @Pattern(regexp = "^[0-9]{7,8}-[0-9Kk]$", message = "El RUT debe tener formato 12345678-9")
    private String rut;

    @NotBlank(message = "El tipo es requerido")
    @Pattern(regexp = "MINORISTA|MAYORISTA", message = "El tipo debe ser MINORISTA o MAYORISTA")
    private String tipo;

    @Valid  // Valida el objeto anidado
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
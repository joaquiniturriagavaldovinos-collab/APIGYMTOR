package ApiGymorEjecucion.Api.application.dto.request.cliente;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class DireccionRequest {

    @NotBlank(message = "La calle es obligatoria")
    @Size(max = 255, message = "La calle no puede superar los 255 caracteres")
    private String calle;

    @NotBlank(message = "El número es obligatorio")
    @Size(max = 10, message = "El número no puede superar los 10 caracteres")
    private String numero;

    @NotBlank(message = "La comuna es obligatoria")
    @Size(max = 100, message = "La comuna no puede superar los 100 caracteres")
    private String comuna;

    @NotBlank(message = "La ciudad es obligatoria")
    @Size(max = 100, message = "La ciudad no puede superar los 100 caracteres")
    private String ciudad;

    @NotBlank(message = "La región es obligatoria")
    @Size(max = 100, message = "La región no puede superar los 100 caracteres")
    private String region;

    @Size(max = 10, message = "El código postal no puede superar los 10 caracteres")
    private String codigoPostal;

    @Size(max = 500, message = "La referencia no puede superar los 500 caracteres")
    private String referencia;

    /**
     * Indica si la dirección es la principal del cliente
     */
    private boolean esPrincipal;

    // Getters y Setters

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComuna() {
        return comuna;
    }

    public void setComuna(String comuna) {
        this.comuna = comuna;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public boolean isEsPrincipal() {
        return esPrincipal;
    }

    public void setEsPrincipal(boolean esPrincipal) {
        this.esPrincipal = esPrincipal;
    }
}

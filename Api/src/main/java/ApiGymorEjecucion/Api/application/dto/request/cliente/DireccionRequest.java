package ApiGymorEjecucion.Api.application.dto.request.cliente;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public class DireccionRequest {


    @NotBlank(message = "La calle es requerida")
    @Size(max = 255)
    private String calle;

    @NotBlank(message = "El número es requerido")
    @Size(max = 10)
    private String numero;

    @NotBlank(message = "La comuna es requerida")
    @Size(max = 100)
    private String comuna;

    @NotBlank(message = "La ciudad es requerida")
    @Size(max = 100)
    private String ciudad;

    @NotBlank(message = "La región es requerida")
    @Size(max = 100)
    private String region;

    @Size(max = 10)
    private String codigoPostal;

    @Size(max = 500)
    private String referencia;

    private boolean esPrincipal;
    // Getters y Setters
    public String getCalle() { return calle; }
    public void setCalle(String calle) { this.calle = calle; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getComuna() { return comuna; }
    public void setComuna(String comuna) { this.comuna = comuna; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    public boolean isEsPrincipal() { return esPrincipal; }
    public void setEsPrincipal(boolean esPrincipal) { this.esPrincipal = esPrincipal; }
}

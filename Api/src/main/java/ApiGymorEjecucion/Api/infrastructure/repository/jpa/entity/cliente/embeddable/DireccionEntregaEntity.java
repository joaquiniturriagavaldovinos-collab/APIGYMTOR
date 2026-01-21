package ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.cliente.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class DireccionEntregaEntity {
    private String calle;
    private String numero;
    private String comuna;
    private String ciudad;
    private String region;

    @Column(name = "codigo_postal")
    private String codigoPostal;

    private String referencia;

    public DireccionEntregaEntity() {}

    public DireccionEntregaEntity(String calle, String numero, String comuna,
                                  String ciudad, String region, String codigoPostal,
                                  String referencia) {
        this.calle = calle;
        this.numero = numero;
        this.comuna = comuna;
        this.ciudad = ciudad;
        this.region = region;
        this.codigoPostal = codigoPostal;
        this.referencia = referencia;
    }

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
}
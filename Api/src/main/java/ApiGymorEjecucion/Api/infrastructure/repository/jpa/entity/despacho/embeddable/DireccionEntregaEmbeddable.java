package ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.despacho.embeddable;

import jakarta.persistence.Embeddable;

@Embeddable
public class DireccionEntregaEmbeddable {

    private String direccionCompleta;

    protected DireccionEntregaEmbeddable() {
    }

    public DireccionEntregaEmbeddable(String direccionCompleta) {
        this.direccionCompleta = direccionCompleta;
    }

    public String getDireccionCompleta() {
        return direccionCompleta;
    }
}

package ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.despacho.embeddable;


import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;

@Embeddable
public class GuiaDespachoEmbeddable {

    private String numero;
    private LocalDateTime fechaEmision;
    private String urlTracking;

    /* === CONSTRUCTOR VACÍO (OBLIGATORIO JPA) === */
    protected GuiaDespachoEmbeddable() {
    }

    /* === CONSTRUCTOR DE CONVENIENCIA === */
    public GuiaDespachoEmbeddable(
            String numero,
            LocalDateTime fechaEmision,
            String urlTracking
    ) {
        this.numero = numero;
        this.fechaEmision = fechaEmision;
        this.urlTracking = urlTracking;
    }

    /* === GETTERS === */

    public String getNumero() {
        return numero;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public String getUrlTracking() {
        return urlTracking;
    }
}

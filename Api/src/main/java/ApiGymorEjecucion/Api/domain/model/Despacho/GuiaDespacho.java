package ApiGymorEjecucion.Api.domain.model.Despacho;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Value Object: Guía de despacho
 */
public class GuiaDespacho {
    private final String numero;
    private final LocalDateTime fechaEmision;
    private final String urlTracking;

    private GuiaDespacho(String numero, LocalDateTime fechaEmision, String urlTracking) {
        this.numero = numero;
        this.fechaEmision = fechaEmision;
        this.urlTracking = urlTracking;
    }

    public static GuiaDespacho crear(String numero, String urlTracking) {
        if (numero == null || numero.isBlank()) {
            throw new IllegalArgumentException("El número de guía es requerido");
        }
        return new GuiaDespacho(numero, LocalDateTime.now(), urlTracking);
    }

    // Getters
    public String getNumero() {
        return numero;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public String getUrlTracking() {
        return urlTracking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GuiaDespacho that = (GuiaDespacho) o;
        return Objects.equals(numero, that.numero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }

    @Override
    public String toString() {
        return String.format("GuiaDespacho{numero='%s', fecha=%s}", numero, fechaEmision);
    }
}
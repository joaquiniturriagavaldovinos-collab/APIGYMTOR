package ApiGymorEjecucion.Api.domain.model.Despacho;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
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
            throw new IllegalArgumentException("Número de guía requerido");
        }
        return new GuiaDespacho(numero, LocalDateTime.now(), urlTracking);
    }

    /* === VALUE OBJECT === */

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
        return "GuiaDespacho{" +
                "numero='" + numero + '\'' +
                ", fechaEmision=" + fechaEmision +
                '}';
    }
}

package ApiGymorEjecucion.Api.domain.model.Despacho;

import java.util.Objects;


public class DireccionEntrega {

    private final String direccionCompleta;

    public DireccionEntrega(String direccionCompleta) {
        if (direccionCompleta == null || direccionCompleta.isBlank()) {
            throw new IllegalArgumentException("Dirección requerida");
        }
        this.direccionCompleta = direccionCompleta;
    }

    public String getDireccionCompleta() {
        return direccionCompleta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DireccionEntrega that = (DireccionEntrega) o;
        return Objects.equals(direccionCompleta, that.direccionCompleta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(direccionCompleta);
    }

    @Override
    public String toString() {
        return direccionCompleta;
    }
}

package ApiGymorEjecucion.Api.domain.model.Cliente;

import java.util.Objects;

/**
 * Value Object: Dirección de entrega
 * Inmutable
 */
public class DireccionEntrega {
    private final String calle;
    private final String numero;
    private final String comuna;
    private final String ciudad;
    private final String region;
    private final String codigoPostal;
    private final String referencia;

    private DireccionEntrega(String calle, String numero, String comuna, String ciudad,
                             String region, String codigoPostal, String referencia) {
        this.calle = calle;
        this.numero = numero;
        this.comuna = comuna;
        this.ciudad = ciudad;
        this.region = region;
        this.codigoPostal = codigoPostal;
        this.referencia = referencia;
    }

    public static DireccionEntrega crear(String calle, String numero, String comuna,
                                         String ciudad, String region, String codigoPostal,
                                         String referencia) {
        validarDatos(calle, numero, comuna, ciudad, region);
        return new DireccionEntrega(calle, numero, comuna, ciudad, region, codigoPostal, referencia);
    }

    private static void validarDatos(String calle, String numero, String comuna,
                                     String ciudad, String region) {
        if (calle == null || calle.isBlank()) {
            throw new IllegalArgumentException("La calle es requerida");
        }
        if (numero == null || numero.isBlank()) {
            throw new IllegalArgumentException("El número es requerido");
        }
        if (comuna == null || comuna.isBlank()) {
            throw new IllegalArgumentException("La comuna es requerida");
        }
        if (ciudad == null || ciudad.isBlank()) {
            throw new IllegalArgumentException("La ciudad es requerida");
        }
        if (region == null || region.isBlank()) {
            throw new IllegalArgumentException("La región es requerida");
        }
    }

    public String getDireccionCompleta() {
        return String.format("%s %s, %s, %s, %s",
                calle, numero, comuna, ciudad, region);
    }

    // Getters
    public String getCalle() {
        return calle;
    }

    public String getNumero() {
        return numero;
    }

    public String getComuna() {
        return comuna;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getRegion() {
        return region;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public String getReferencia() {
        return referencia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DireccionEntrega that = (DireccionEntrega) o;
        return Objects.equals(calle, that.calle) &&
                Objects.equals(numero, that.numero) &&
                Objects.equals(comuna, that.comuna) &&
                Objects.equals(ciudad, that.ciudad) &&
                Objects.equals(region, that.region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(calle, numero, comuna, ciudad, region);
    }

    @Override
    public String toString() {
        return getDireccionCompleta();
    }
}
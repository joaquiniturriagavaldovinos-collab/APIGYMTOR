package ApiGymorEjecucion.Api.domain.model.Despacho;

import java.util.Objects;

/**
 * Value Object: Información del transportista
 */
public class Transportista {
    private final String nombre;
    private final String codigo;
    private final String telefono;

    private Transportista(String nombre, String codigo, String telefono) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.telefono = telefono;
    }

    public static Transportista crear(String nombre, String codigo, String telefono) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del transportista es requerido");
        }
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código del transportista es requerido");
        }
        return new Transportista(nombre, codigo, telefono);
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getTelefono() {
        return telefono;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transportista that = (Transportista) o;
        return Objects.equals(codigo, that.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return String.format("Transportista{nombre='%s', codigo='%s'}", nombre, codigo);
    }
}
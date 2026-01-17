package ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.despacho.embeddable;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Embeddable
public class TransportistaEmbeddable {

    private String nombre;
    private String codigo;
    private String telefono;

    protected TransportistaEmbeddable() {
    }

    public TransportistaEmbeddable(String nombre, String codigo, String telefono) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getTelefono() {
        return telefono;
    }
}


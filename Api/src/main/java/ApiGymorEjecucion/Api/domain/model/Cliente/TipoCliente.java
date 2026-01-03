package ApiGymorEjecucion.Api.domain.model.Cliente;


/**
 * Tipo de cliente según el modelo de negocio
 */
public enum TipoCliente {
    MINORISTA("Minorista"),
    MAYORISTA("Mayorista");

    private final String descripcion;

    TipoCliente(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Los mayoristas pueden tener condiciones especiales de precio
     */
    public boolean aplicaDescuentoMayorista() {
        return this == MAYORISTA;
    }
}
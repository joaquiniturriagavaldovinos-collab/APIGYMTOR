package ApiGymorEjecucion.Api.domain.model.producto;


/**
 * Tipos de productos que comercializa GYMOR
 */
public enum TipoProducto {
    DISCO("Disco de Pesas"),
    MAQUINA("Máquina de Ejercicio"),
    ACCESORIO("Accesorio"),
    BARRA("Barra"),
    RACK("Rack"),
    CARDIO("Equipo Cardiovascular");

    private final String descripcion;

    TipoProducto(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Indica si el producto es fabricado por GYMOR
     */
    public boolean esFabricado() {
        return this == DISCO;
    }

    /**
     * Indica si el producto es de gran tamaño
     */
    public boolean requiereDespachoEspecial() {
        return this == MAQUINA || this == RACK || this == CARDIO;
    }
}
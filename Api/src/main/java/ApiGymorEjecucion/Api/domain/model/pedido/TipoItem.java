package ApiGymorEjecucion.Api.domain.model.pedido;


/**
 * Tipo de item que puede formar parte de un pedido
 */
public enum TipoItem {
    PRODUCTO_FISICO("Producto Físico"),
    SERVICIO("Servicio"),
    SUPLEMENTO("Suplemento");  // <-- nuevo tipo

    private final String descripcion;

    TipoItem(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
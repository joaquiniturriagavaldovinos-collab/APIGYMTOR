package ApiGymorEjecucion.Api.application.dto.response.producto;

public class ActualizarStockResponse {
    private String productoId;
    private String nombreProducto;
    private int nuevoStock;
    private String tipoOperacion; // "INCREMENTO", "DECREMENTO", "AJUSTE"
    private int cantidadAfectada;

    public ActualizarStockResponse(String productoId, String nombreProducto,
                                   int nuevoStock, String tipoOperacion,
                                   int cantidadAfectada) {
        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.nuevoStock = nuevoStock;
        this.tipoOperacion = tipoOperacion;
        this.cantidadAfectada = cantidadAfectada;
    }

    // Getters
    public String getProductoId() {
        return productoId;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public int getNuevoStock() {
        return nuevoStock;
    }

    public String getTipoOperacion() {
        return tipoOperacion;
    }

    public int getCantidadAfectada() {
        return cantidadAfectada;
    }
}
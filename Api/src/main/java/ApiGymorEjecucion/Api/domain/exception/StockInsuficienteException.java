package ApiGymorEjecucion.Api.domain.exception;

/**
 * Excepción lanzada cuando no hay stock suficiente para un producto
 */
public class StockInsuficienteException extends RuntimeException {

    private final String productoId;
    private final int stockDisponible;
    private final int cantidadSolicitada;

    public StockInsuficienteException(String productoId, int stockDisponible, int cantidadSolicitada) {
        super(String.format(
                "Stock insuficiente para producto %s. Disponible: %d, Solicitado: %d",
                productoId, stockDisponible, cantidadSolicitada
        ));
        this.productoId = productoId;
        this.stockDisponible = stockDisponible;
        this.cantidadSolicitada = cantidadSolicitada;
    }

    public String getProductoId() {
        return productoId;
    }

    public int getStockDisponible() {
        return stockDisponible;
    }

    public int getCantidadSolicitada() {
        return cantidadSolicitada;
    }
}
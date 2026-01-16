package ApiGymorEjecucion.Api.application.dto.response.dashboard;

public class MetricasGeneralesResponse {
    private long totalPedidos;
    private long pedidosCompletados;
    private long pedidosPendientes;
    private long totalClientes;
    private long clientesActivos;
    private long clientesNuevosMes;
    private long totalProductos;
    private long productosConStock;
    private long productosBajoStock;

    // Getters y Setters
    public long getTotalPedidos() { return totalPedidos; }
    public void setTotalPedidos(long totalPedidos) { this.totalPedidos = totalPedidos; }

    public long getPedidosCompletados() { return pedidosCompletados; }
    public void setPedidosCompletados(long pedidosCompletados) {
        this.pedidosCompletados = pedidosCompletados;
    }

    public long getPedidosPendientes() { return pedidosPendientes; }
    public void setPedidosPendientes(long pedidosPendientes) {
        this.pedidosPendientes = pedidosPendientes;
    }

    public long getTotalClientes() { return totalClientes; }
    public void setTotalClientes(long totalClientes) { this.totalClientes = totalClientes; }

    public long getClientesActivos() { return clientesActivos; }
    public void setClientesActivos(long clientesActivos) { this.clientesActivos = clientesActivos; }

    public long getClientesNuevosMes() { return clientesNuevosMes; }
    public void setClientesNuevosMes(long clientesNuevosMes) {
        this.clientesNuevosMes = clientesNuevosMes;
    }

    public long getTotalProductos() { return totalProductos; }
    public void setTotalProductos(long totalProductos) { this.totalProductos = totalProductos; }

    public long getProductosConStock() { return productosConStock; }
    public void setProductosConStock(long productosConStock) {
        this.productosConStock = productosConStock;
    }

    public long getProductosBajoStock() { return productosBajoStock; }
    public void setProductosBajoStock(long productosBajoStock) {
        this.productosBajoStock = productosBajoStock;
    }
}

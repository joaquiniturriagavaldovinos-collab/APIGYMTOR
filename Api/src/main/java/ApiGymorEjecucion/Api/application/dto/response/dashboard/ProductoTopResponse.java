package ApiGymorEjecucion.Api.application.dto.response.dashboard;

import java.math.BigDecimal;

public class ProductoTopResponse {
    private String productoId;
    private String nombre;
    private int cantidadVendida;
    private BigDecimal totalVentas;

    public String getProductoId() { return productoId; }
    public void setProductoId(String productoId) { this.productoId = productoId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getCantidadVendida() { return cantidadVendida; }
    public void setCantidadVendida(int cantidadVendida) { this.cantidadVendida = cantidadVendida; }

    public BigDecimal getTotalVentas() { return totalVentas; }
    public void setTotalVentas(BigDecimal totalVentas) { this.totalVentas = totalVentas; }
}

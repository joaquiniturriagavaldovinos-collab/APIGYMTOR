package ApiGymorEjecucion.Api.application.dto.request.pedido;

import java.math.BigDecimal;

/**
 * DTO para un item dentro de la solicitud de pedido
 */
public class ItemPedidoRequest {
    private String productoId;
    private String nombre;
    private String tipo; // "PRODUCTO_FISICO" o "SERVICIO"
    private int cantidad;
    private BigDecimal precioUnitario;

    // Constructors
    public ItemPedidoRequest() {
    }

    public ItemPedidoRequest(String productoId, String nombre, String tipo,
                             int cantidad, BigDecimal precioUnitario) {
        this.productoId = productoId;
        this.nombre = nombre;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    // Getters y Setters
    public String getProductoId() {
        return productoId;
    }

    public void setProductoId(String productoId) {
        this.productoId = productoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
}
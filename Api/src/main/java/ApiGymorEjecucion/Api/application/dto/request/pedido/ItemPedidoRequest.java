package ApiGymorEjecucion.Api.application.dto.request.pedido;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * DTO para un ítem dentro de la solicitud de pedido
 */
public class ItemPedidoRequest {

    @NotBlank(message = "El ID del producto es obligatorio")
    private String productoId;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 255, message = "El nombre del producto no puede superar los 255 caracteres")
    private String nombre;

    /**
     * Tipo de ítem:
     * - PRODUCTO_FISICO
     * - SERVICIO
     */
    @NotBlank(message = "El tipo de ítem es obligatorio")
    @Pattern(
            regexp = "PRODUCTO_FISICO|SERVICIO",
            message = "El tipo debe ser PRODUCTO_FISICO o SERVICIO"
    )
    private String tipo;

    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private int cantidad;

    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio unitario debe ser mayor a cero")
    private BigDecimal precioUnitario;

    // Constructors

    public ItemPedidoRequest() {
    }

    public ItemPedidoRequest(
            String productoId,
            String nombre,
            String tipo,
            int cantidad,
            BigDecimal precioUnitario
    ) {
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

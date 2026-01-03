package ApiGymorEjecucion.Api.domain.model.pedido;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Value Object: Representa una línea de pedido.
 * Inmutable - no puede modificarse después de creado.
 */
public class ItemPedido {
    private final String productoId;
    private final String nombre;
    private final TipoItem tipo;
    private final int cantidad;
    private final BigDecimal precioUnitario;
    private final BigDecimal subtotal;

    private ItemPedido(String productoId, String nombre, TipoItem tipo,
                       int cantidad, BigDecimal precioUnitario) {
        this.productoId = productoId;
        this.nombre = nombre;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

    /**
     * Factory method para crear un item
     */
    public static ItemPedido crear(String productoId, String nombre, TipoItem tipo,
                                   int cantidad, BigDecimal precioUnitario) {
        validarDatos(productoId, nombre, cantidad, precioUnitario);
        return new ItemPedido(productoId, nombre, tipo, cantidad, precioUnitario);
    }

    private static void validarDatos(String productoId, String nombre,
                                     int cantidad, BigDecimal precioUnitario) {
        if (productoId == null || productoId.isBlank()) {
            throw new IllegalArgumentException("El ID del producto es requerido");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del producto es requerido");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        if (precioUnitario == null || precioUnitario.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }
    }

    // Getters
    public String getProductoId() {
        return productoId;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoItem getTipo() {
        return tipo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemPedido that = (ItemPedido) o;
        return cantidad == that.cantidad &&
                Objects.equals(productoId, that.productoId) &&
                Objects.equals(precioUnitario, that.precioUnitario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productoId, cantidad, precioUnitario);
    }

    @Override
    public String toString() {
        return String.format("ItemPedido{producto='%s', cantidad=%d, subtotal=%s}",
                nombre, cantidad, subtotal);
    }
}
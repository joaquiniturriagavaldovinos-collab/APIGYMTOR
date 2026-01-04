package ApiGymorEjecucion.Api.domain.service;

import ApiGymorEjecucion.Api.domain.exception.StockInsuficienteException;
import ApiGymorEjecucion.Api.domain.model.pedido.ItemPedido;
import ApiGymorEjecucion.Api.domain.model.producto.Producto;
import ApiGymorEjecucion.Api.domain.repository.ProductoRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio de Dominio: Validador de Stock
 *
 * Valida disponibilidad de stock antes de crear pedidos
 */
public class ValidadorStockService {

    private final ProductoRepository productoRepository;

    public ValidadorStockService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * Valida que hay stock suficiente para todos los items de un pedido
     *
     * @param items Items del pedido a validar
     * @throws StockInsuficienteException si algún producto no tiene stock
     */
    public void validarStockDisponible(List<ItemPedido> items) {
        List<String> productosSinStock = new ArrayList<>();

        for (ItemPedido item : items) {
            Producto producto = productoRepository.buscarPorId(item.getProductoId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Producto no encontrado: " + item.getProductoId()
                    ));

            if (!producto.tieneStockDisponible(item.getCantidad())) {
                throw new StockInsuficienteException(
                        producto.getId(),
                        producto.getStockDisponible(),
                        item.getCantidad()
                );
            }

            if (!producto.isActivo()) {
                throw new IllegalStateException(
                        "El producto " + producto.getNombre() + " no está activo"
                );
            }
        }
    }

    /**
     * Reserva el stock para los items de un pedido
     *
     * @param items Items cuyo stock se debe reservar
     */
    public void reservarStock(List<ItemPedido> items) {
        for (ItemPedido item : items) {
            Producto producto = productoRepository.buscarPorId(item.getProductoId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Producto no encontrado: " + item.getProductoId()
                    ));

            producto.reservarStock(item.getCantidad());
            productoRepository.guardar(producto);
        }
    }

    /**
     * Libera las reservas de stock de un pedido cancelado
     *
     * @param items Items cuyas reservas se deben liberar
     */
    public void liberarReservas(List<ItemPedido> items) {
        for (ItemPedido item : items) {
            Producto producto = productoRepository.buscarPorId(item.getProductoId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Producto no encontrado: " + item.getProductoId()
                    ));

            producto.liberarReserva(item.getCantidad());
            productoRepository.guardar(producto);
        }
    }

    /**
     * Confirma la venta y decrementa el stock definitivamente
     *
     * @param items Items vendidos
     */
    public void confirmarVenta(List<ItemPedido> items) {
        for (ItemPedido item : items) {
            Producto producto = productoRepository.buscarPorId(item.getProductoId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Producto no encontrado: " + item.getProductoId()
                    ));

            // Liberar la reserva y decrementar el stock
            producto.liberarReserva(item.getCantidad());
            producto.decrementarStock(item.getCantidad());
            productoRepository.guardar(producto);
        }
    }

    /**
     * Verifica si un producto específico tiene stock disponible
     */
    public boolean tieneStockDisponible(String productoId, int cantidad) {
        Producto producto = productoRepository.buscarPorId(productoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Producto no encontrado: " + productoId
                ));

        return producto.tieneStockDisponible(cantidad);
    }
}
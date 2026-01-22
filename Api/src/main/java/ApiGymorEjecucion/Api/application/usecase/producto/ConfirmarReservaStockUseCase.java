package ApiGymorEjecucion.Api.application.usecase.producto;

import ApiGymorEjecucion.Api.domain.model.producto.Producto;
import ApiGymorEjecucion.Api.domain.model.producto.Stock;
import ApiGymorEjecucion.Api.domain.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de Uso: Confirmar Reserva de Stock
 * Convierte una reserva en venta confirmada
 * (reduce stock total y libera la reserva)
 */
@Service
public class ConfirmarReservaStockUseCase {

    private final ProductoRepository productoRepository;

    public ConfirmarReservaStockUseCase(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional
    public Producto ejecutar(String id, int cantidad) {
        Producto producto = productoRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Producto no encontrado con ID: " + id));

        // Obtener stock actual y confirmar la reserva
        Stock stockActual = producto.getStock();
        Stock nuevoStock = stockActual.confirmarReserva(cantidad);

        producto.establecerStock(nuevoStock);

        return productoRepository.guardar(producto);
    }
}
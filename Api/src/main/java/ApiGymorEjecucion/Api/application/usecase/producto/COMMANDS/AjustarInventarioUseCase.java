package ApiGymorEjecucion.Api.application.usecase.producto.COMMANDS;

import ApiGymorEjecucion.Api.domain.model.producto.Producto;
import ApiGymorEjecucion.Api.domain.model.producto.Stock;
import ApiGymorEjecucion.Api.domain.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de Uso: Ajustar Inventario
 * Ajusta el stock según inventario físico
 * Útil para correcciones por mermas, auditorías, etc.
 */
@Service
public class AjustarInventarioUseCase {

    private final ProductoRepository productoRepository;

    public AjustarInventarioUseCase(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional
    public Producto ejecutar(String id, int nuevaCantidad) {
        if (nuevaCantidad < 0) {
            throw new IllegalArgumentException(
                    "La cantidad del inventario no puede ser negativa");
        }

        Producto producto = productoRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Producto no encontrado con ID: " + id));

        // Usar el método de ajuste de inventario del Value Object
        Stock stockActual = producto.getStock();
        Stock nuevoStock = stockActual.ajustarInventario(nuevaCantidad);

        producto.establecerStock(nuevoStock);

        return productoRepository.guardar(producto);
    }
}
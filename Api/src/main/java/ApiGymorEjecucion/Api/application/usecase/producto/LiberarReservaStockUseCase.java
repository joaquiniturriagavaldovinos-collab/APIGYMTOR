package ApiGymorEjecucion.Api.application.usecase.producto;

import ApiGymorEjecucion.Api.domain.model.producto.Producto;
import ApiGymorEjecucion.Api.domain.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de Uso: Liberar Reserva de Stock
 * Libera stock previamente reservado (ej: cuando se cancela un carrito)
 */
@Service
public class LiberarReservaStockUseCase {

    private final ProductoRepository productoRepository;

    public LiberarReservaStockUseCase(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional
    public Producto ejecutar(String id, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad a liberar debe ser mayor a cero");
        }

        Producto producto = productoRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Producto no encontrado con ID: " + id));

        producto.liberarReserva(cantidad);
        return productoRepository.guardar(producto);
    }
}
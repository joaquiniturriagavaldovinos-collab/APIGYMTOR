package ApiGymorEjecucion.Api.application.usecase.producto.COMMANDS;

import ApiGymorEjecucion.Api.domain.model.producto.Producto;
import ApiGymorEjecucion.Api.domain.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de Uso: Reservar Stock de Producto
 * Reserva stock para operaciones futuras (carrito, cotización)
 * La validación de negocio está en el dominio
 */
@Service
public class ReservarStockProductoUseCase {

    private final ProductoRepository productoRepository;

    public ReservarStockProductoUseCase(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional
    public Producto ejecutar(String id, int cantidad) {
        Producto producto = productoRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Producto no encontrado con ID: " + id));

        // La lógica de validación está en el dominio
        producto.reservarStock(cantidad);

        return productoRepository.guardar(producto);
    }
}
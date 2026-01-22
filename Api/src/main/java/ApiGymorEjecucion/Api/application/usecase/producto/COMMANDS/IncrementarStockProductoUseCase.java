package ApiGymorEjecucion.Api.application.usecase.producto.COMMANDS;

import ApiGymorEjecucion.Api.domain.model.producto.Producto;
import ApiGymorEjecucion.Api.domain.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de Uso: Incrementar Stock de Producto
 * Aumenta el stock (reposición de mercancía)
 * La validación de negocio está en el dominio
 */
@Service
public class IncrementarStockProductoUseCase {

    private final ProductoRepository productoRepository;

    public IncrementarStockProductoUseCase(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional
    public Producto ejecutar(String id, int cantidad) {
        Producto producto = productoRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Producto no encontrado con ID: " + id));

        // La lógica de validación está en el dominio
        producto.incrementarStock(cantidad);

        return productoRepository.guardar(producto);
    }
}
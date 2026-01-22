package ApiGymorEjecucion.Api.application.usecase.producto.COMMANDS;

import ApiGymorEjecucion.Api.domain.model.producto.Producto;
import ApiGymorEjecucion.Api.domain.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de Uso: Desactivar Producto
 * Marca un producto como inactivo (soft delete)
 * La validación de negocio está en el dominio
 */
@Service
public class DesactivarProductoUseCase {

    private final ProductoRepository productoRepository;

    public DesactivarProductoUseCase(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional
    public Producto ejecutar(String id) {
        Producto producto = productoRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Producto no encontrado con ID: " + id));

        // La lógica de validación está en el dominio (incluyendo validación de reservas)
        producto.desactivar();

        return productoRepository.guardar(producto);
    }
}
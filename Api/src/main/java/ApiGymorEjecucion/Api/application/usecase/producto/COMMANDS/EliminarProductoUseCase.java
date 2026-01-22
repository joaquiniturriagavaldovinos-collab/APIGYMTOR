package ApiGymorEjecucion.Api.application.usecase.producto.COMMANDS;

import ApiGymorEjecucion.Api.domain.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de Uso: Eliminar Producto
 * Elimina físicamente un producto del sistema
 * Nota: En la mayoría de casos es preferible usar DesactivarProductoUseCase
 */
@Service
public class EliminarProductoUseCase {

    private final ProductoRepository productoRepository;

    public EliminarProductoUseCase(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional
    public void ejecutar(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException(
                    "El ID del producto es requerido");
        }

        // Verificar que el producto existe antes de eliminar
        if (!productoRepository.buscarPorId(id).isPresent()) {
            throw new IllegalArgumentException(
                    "Producto no encontrado con ID: " + id);
        }

        boolean eliminado = productoRepository.eliminar(id);

        if (!eliminado) {
            throw new IllegalStateException(
                    "No se pudo eliminar el producto con ID: " + id);
        }
    }
}
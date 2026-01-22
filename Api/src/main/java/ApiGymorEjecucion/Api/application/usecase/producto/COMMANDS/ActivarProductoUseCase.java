package ApiGymorEjecucion.Api.application.usecase.producto.COMMANDS;

import ApiGymorEjecucion.Api.domain.model.producto.Producto;
import ApiGymorEjecucion.Api.domain.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de Uso: Activar Producto
 * Activa un producto previamente desactivado
 * La validación de negocio está en el dominio
 */
@Service
public class ActivarProductoUseCase {

    private final ProductoRepository productoRepository;

    public ActivarProductoUseCase(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional
    public Producto ejecutar(String id) {
        Producto producto = productoRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Producto no encontrado con ID: " + id));

        // La lógica de validación está en el dominio
        producto.activar();

        return productoRepository.guardar(producto);
    }
}
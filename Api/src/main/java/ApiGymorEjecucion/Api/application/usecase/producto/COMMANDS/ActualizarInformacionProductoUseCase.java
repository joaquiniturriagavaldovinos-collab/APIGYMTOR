package ApiGymorEjecucion.Api.application.usecase.producto.COMMANDS;

import ApiGymorEjecucion.Api.domain.model.producto.Producto;
import ApiGymorEjecucion.Api.domain.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de Uso: Actualizar Información de Producto
 * Actualiza nombre y/o descripción del producto
 */
@Service
public class ActualizarInformacionProductoUseCase {

    private final ProductoRepository productoRepository;

    public ActualizarInformacionProductoUseCase(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional
    public Producto ejecutar(String id, String nuevoNombre, String nuevaDescripcion) {
        Producto producto = productoRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Producto no encontrado con ID: " + id));

        producto.actualizarInformacion(nuevoNombre, nuevaDescripcion);

        return productoRepository.guardar(producto);
    }
}
package ApiGymorEjecucion.Api.application.usecase.producto.COMMANDS;

import ApiGymorEjecucion.Api.domain.model.producto.Producto;
import ApiGymorEjecucion.Api.domain.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Caso de Uso: Actualizar Precio de Producto
 * Cambia el precio de un producto
 * La validación está en el dominio
 */
@Service
public class ActualizarPrecioProductoUseCase {

    private final ProductoRepository productoRepository;

    public ActualizarPrecioProductoUseCase(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional
    public Producto ejecutar(String id, BigDecimal nuevoPrecio) {
        Producto producto = productoRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Producto no encontrado con ID: " + id));

        // La validación de negocio (precio > 0, cambios drásticos) está en el dominio
        producto.actualizarPrecio(nuevoPrecio);

        return productoRepository.guardar(producto);
    }
}
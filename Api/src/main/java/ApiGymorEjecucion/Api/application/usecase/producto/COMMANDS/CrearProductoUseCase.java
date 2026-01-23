package ApiGymorEjecucion.Api.application.usecase.producto.COMMANDS;

import ApiGymorEjecucion.Api.domain.model.producto.Producto;
import ApiGymorEjecucion.Api.domain.model.producto.TipoProducto;
import ApiGymorEjecucion.Api.domain.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Caso de Uso: Crear Producto
 * Crea un nuevo producto en el sistema
 */
@Service
public class CrearProductoUseCase {

    private final ProductoRepository productoRepository;

    public CrearProductoUseCase(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional
    public Producto ejecutar(String codigo, String nombre, String descripcion,
                             TipoProducto tipo, BigDecimal precio) {



        // Validar que el código no exista
        if (productoRepository.existePorCodigo(codigo)) {
            throw new IllegalArgumentException(
                    "Ya existe un producto con el código: " + codigo);
        }

        // Crear el producto usando el factory method del dominio
        String id = UUID.randomUUID().toString();
        Producto producto = Producto.crear(id, codigo, nombre, descripcion, tipo, precio);

        return productoRepository.guardar(producto);
    }
}
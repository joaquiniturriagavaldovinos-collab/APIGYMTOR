package ApiGymorEjecucion.Api.application.usecase.producto;

import ApiGymorEjecucion.Api.domain.model.producto.Producto;
import ApiGymorEjecucion.Api.domain.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de Uso: Buscar Producto por ID
 * Busca un producto específico por su identificador único
 */
@Service
public class BuscarProductoPorIdUseCase {

    private final ProductoRepository productoRepository;

    public BuscarProductoPorIdUseCase(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional(readOnly = true)
    public Producto ejecutar(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException(
                    "El ID del producto es requerido");
        }

        return productoRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Producto no encontrado con ID: " + id));
    }
}
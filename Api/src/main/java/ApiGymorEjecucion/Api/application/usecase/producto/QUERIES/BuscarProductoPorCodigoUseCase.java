package ApiGymorEjecucion.Api.application.usecase.producto.QUERIES;

import ApiGymorEjecucion.Api.domain.model.producto.Producto;
import ApiGymorEjecucion.Api.domain.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de Uso: Buscar Producto por Código
 * Busca un producto específico por su código único
 */
@Service
public class BuscarProductoPorCodigoUseCase {

    private final ProductoRepository productoRepository;

    public BuscarProductoPorCodigoUseCase(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional(readOnly = true)
    public Producto ejecutar(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException(
                    "El código del producto es requerido");
        }

        return productoRepository.buscarPorCodigo(codigo)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Producto no encontrado con código: " + codigo));
    }
}
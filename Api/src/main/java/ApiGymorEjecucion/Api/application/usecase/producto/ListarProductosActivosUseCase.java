package ApiGymorEjecucion.Api.application.usecase.producto;

import ApiGymorEjecucion.Api.domain.model.producto.Producto;
import ApiGymorEjecucion.Api.domain.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Caso de Uso: Listar Productos Activos
 * Obtiene todos los productos que están activos en el sistema
 */
@Service
public class ListarProductosActivosUseCase {

    private final ProductoRepository productoRepository;

    public ListarProductosActivosUseCase(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional(readOnly = true)
    public List<Producto> ejecutar() {
        return productoRepository.buscarActivos();
    }
}
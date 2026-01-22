package ApiGymorEjecucion.Api.application.usecase.producto.QUERIES;

import ApiGymorEjecucion.Api.domain.model.producto.Producto;
import ApiGymorEjecucion.Api.domain.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Caso de Uso: Listar Productos con Stock Bajo
 * Query que aprovecha la lógica de negocio del dominio
 */
@Service
public class ListarProductosStockBajoUseCase {

    private final ProductoRepository productoRepository;

    public ListarProductosStockBajoUseCase(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional(readOnly = true)
    public List<Producto> ejecutar() {
        return productoRepository.buscarActivos().stream()
                .filter(Producto::tieneStockBajo)
                .collect(Collectors.toList());
    }
}
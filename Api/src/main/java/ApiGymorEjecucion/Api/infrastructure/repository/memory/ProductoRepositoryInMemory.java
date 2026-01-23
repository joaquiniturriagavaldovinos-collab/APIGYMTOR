package ApiGymorEjecucion.Api.infrastructure.repository.memory;


import ApiGymorEjecucion.Api.domain.model.producto.Producto;
import ApiGymorEjecucion.Api.domain.model.producto.TipoProducto;
import ApiGymorEjecucion.Api.domain.repository.ProductoRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Implementación en memoria del repositorio de Productos
 */
@Repository
@Profile("test")  // ← SOLO TESTS
public class ProductoRepositoryInMemory implements ProductoRepository {

    private final Map<String, Producto> productos = new ConcurrentHashMap<>();

    @Override
    public Producto guardar(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        productos.put(producto.getId(), producto);
        return producto;
    }

    @Override
    public Optional<Producto> buscarPorId(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(productos.get(id));
    }

    @Override
    public Optional<Producto> buscarPorCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            return Optional.empty();
        }
        return productos.values().stream()
                .filter(producto -> producto.getCodigo().equals(codigo))
                .findFirst();
    }

    @Override
    public List<Producto> buscarPorTipo(TipoProducto tipo) {
        if (tipo == null) {
            return new ArrayList<>();
        }
        return productos.values().stream()
                .filter(producto -> producto.getTipo() == tipo)
                .collect(Collectors.toList());
    }

    @Override
    public List<Producto> buscarConStock() {
        return productos.values().stream()
                .filter(producto -> producto.getStockDisponible() > 0)
                .collect(Collectors.toList());
    }

    @Override
    public List<Producto> buscarActivos() {
        return productos.values().stream()
                .filter(Producto::isActivo)
                .collect(Collectors.toList());
    }

    @Override
    public List<Producto> buscarTodos() {
        return new ArrayList<>(productos.values());
    }

    @Override
    public boolean existePorCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            return false;
        }
        return productos.values().stream()
                .anyMatch(producto -> producto.getCodigo().equals(codigo));
    }

    @Override
    public boolean eliminar(String id) {
        if (id == null || id.isBlank()) {
            return false;
        }
        return productos.remove(id) != null;
    }

    @Override
    public long contar() {
        return productos.size();
    }

    /**
     * Método auxiliar para limpiar todos los datos (útil para tests)
     */
    public void limpiar() {
        productos.clear();
    }

    @Override
    public Producto obtenerPorId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID del producto no puede ser nulo o vacío");
        }
        Producto producto = productos.get(id);
        if (producto == null) {
            throw new IllegalArgumentException("Producto no encontrado con ID: " + id);
        }
        return producto;
    }


}
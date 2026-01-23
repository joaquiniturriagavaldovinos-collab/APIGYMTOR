package ApiGymorEjecucion.Api.domain.repository;


import ApiGymorEjecucion.Api.domain.model.producto.Producto;
import ApiGymorEjecucion.Api.domain.model.producto.TipoProducto;

import java.util.List;
import java.util.Optional;

/**
 * Puerto (interfaz) del repositorio de Productos
 */
public interface ProductoRepository {


    Producto obtenerPorId(String id);
    /**
     *
     * Guarda o actualiza un producto
     */
    Producto guardar(Producto producto);

    /**
     * Busca un producto por su ID
     */
    Optional<Producto> buscarPorId(String id);

    /**
     * Busca un producto por su código
     */
    Optional<Producto> buscarPorCodigo(String codigo);

    /**
     * Busca productos por tipo
     */
    List<Producto> buscarPorTipo(TipoProducto tipo);

    /**
     * Busca productos con stock disponible
     */
    List<Producto> buscarConStock();

    /**
     * Busca productos activos
     */
    List<Producto> buscarActivos();

    /**
     * Obtiene todos los productos
     */
    List<Producto> buscarTodos();

    /**
     * Verifica si existe un producto con el código dado
     */
    boolean existePorCodigo(String codigo);

    /**
     * Elimina un producto
     */
    boolean eliminar(String id);

    /**
     * Cuenta total de productos
     */
    long contar();
}
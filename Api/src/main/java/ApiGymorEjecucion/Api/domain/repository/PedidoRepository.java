package ApiGymorEjecucion.Api.domain.repository;


import ApiGymorEjecucion.Api.domain.model.pedido.EstadoPedido;
import ApiGymorEjecucion.Api.domain.model.pedido.Pedido;

import java.util.List;
import java.util.Optional;

/**
 * Puerto (interfaz) del repositorio de Pedidos.
 * Define el contrato sin implementación técnica.
 * La implementación puede ser en memoria, JPA, MongoDB, etc.
 */
public interface PedidoRepository {

    /**
     * Guarda o actualiza un pedido
     * @param pedido Pedido a persistir
     * @return Pedido guardado
     */
    Pedido guardar(Pedido pedido);

    /**
     * Busca un pedido por su ID
     * @param id Identificador del pedido
     * @return Optional con el pedido si existe
     */
    Optional<Pedido> buscarPorId(String id);

    /**
     * Busca todos los pedidos de un cliente
     * @param clienteId Identificador del cliente
     * @return Lista de pedidos del cliente
     */
    List<Pedido> buscarPorCliente(String clienteId);

    /**
     * Busca pedidos por estado
     * @param estado Estado a filtrar
     * @return Lista de pedidos en ese estado
     */
    List<Pedido> buscarPorEstado(EstadoPedido estado);

    /**
     * Obtiene todos los pedidos
     * @return Lista de todos los pedidos
     */
    List<Pedido> buscarTodos();

    /**
     * Elimina un pedido (para casos excepcionales)
     * @param id Identificador del pedido a eliminar
     * @return true si se eliminó, false si no existía
     */
    boolean eliminar(String id);

    /**
     * Verifica si existe un pedido con el ID dado
     * @param id Identificador del pedido
     * @return true si existe
     */
    boolean existe(String id);

    /**
     * Cuenta total de pedidos
     * @return Cantidad total de pedidos
     */
    long contar();
}
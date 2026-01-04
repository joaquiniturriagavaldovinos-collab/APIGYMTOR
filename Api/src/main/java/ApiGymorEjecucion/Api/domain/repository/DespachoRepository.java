package ApiGymorEjecucion.Api.domain.repository;

import ApiGymorEjecucion.Api.domain.model.Despacho.Despacho;

import java.util.List;
import java.util.Optional;

/**
 * Puerto (interfaz) del repositorio de Despachos
 */
public interface DespachoRepository {

    /**
     * Guarda o actualiza un despacho
     */
    Despacho guardar(Despacho despacho);

    /**
     * Busca un despacho por su ID
     */
    Optional<Despacho> buscarPorId(String id);

    /**
     * Busca el despacho de un pedido
     */
    Optional<Despacho> buscarPorPedidoId(String pedidoId);

    /**
     * Busca despachos por número de guía
     */
    Optional<Despacho> buscarPorGuia(String numeroGuia);

    /**
     * Busca despachos pendientes (no despachados)
     */
    List<Despacho> buscarPendientes();

    /**
     * Busca despachos despachados pero no entregados
     */
    List<Despacho> buscarEnTransito();

    /**
     * Busca despachos entregados
     */
    List<Despacho> buscarEntregados();

    /**
     * Obtiene todos los despachos
     */
    List<Despacho> buscarTodos();

    /**
     * Verifica si existe un despacho para un pedido
     */
    boolean existePorPedidoId(String pedidoId);

    /**
     * Elimina un despacho
     */
    boolean eliminar(String id);

    /**
     * Cuenta total de despachos
     */
    long contar();
}
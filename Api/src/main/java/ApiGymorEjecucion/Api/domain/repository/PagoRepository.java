package ApiGymorEjecucion.Api.domain.repository;


import ApiGymorEjecucion.Api.domain.model.Pago.EstadoPago;
import ApiGymorEjecucion.Api.domain.model.Pago.Pago;

import java.util.List;
import java.util.Optional;

/**
 * Puerto (interfaz) del repositorio de Pagos
 */
public interface PagoRepository {

    /**
     * Guarda o actualiza un pago
     */
    Pago guardar(Pago pago);

    /**
     * Busca un pago por su ID
     */
    Optional<Pago> buscarPorId(String id);

    /**
     * Busca todos los pagos de un pedido
     */
    List<Pago> buscarPorPedidoId(String pedidoId);

    /**
     * Busca un pago por referencia de pasarela
     */
    Optional<Pago> buscarPorReferenciaPasarela(String referencia);

    /**
     * Busca pagos por estado
     */
    List<Pago> buscarPorEstado(EstadoPago estado);

    /**
     * Obtiene todos los pagos
     */
    List<Pago> buscarTodos();

    /**
     * Verifica si existe un pago para un pedido
     */
    boolean existePorPedidoId(String pedidoId);

    /**
     * Elimina un pago
     */
    boolean eliminar(String id);

    /**
     * Cuenta total de pagos
     */
    long contar();
}
package ApiGymorEjecucion.Api.domain.repository;



import ApiGymorEjecucion.Api.domain.model.Cliente.Cliente;
import ApiGymorEjecucion.Api.domain.model.Cliente.TipoCliente;

import java.util.List;
import java.util.Optional;

/**
 * Puerto (interfaz) del repositorio de Clientes
 */
public interface ClienteRepository {

    /**
     * Guarda o actualiza un cliente
     */
    Cliente guardar(Cliente cliente);

    /**
     * Busca un cliente por su ID
     */
    Optional<Cliente> buscarPorId(String id);

    /**
     * Busca un cliente por su RUT
     */
    Optional<Cliente> buscarPorRut(String rut);

    /**
     * Busca un cliente por su email
     */
    Optional<Cliente> buscarPorEmail(String email);

    /**
     * Busca clientes por tipo (minorista/mayorista)
     */
    List<Cliente> buscarPorTipo(TipoCliente tipo);

    /**
     * Obtiene todos los clientes
     */
    List<Cliente> buscarTodos();

    /**
     * Obtiene solo clientes activos
     */
    List<Cliente> buscarActivos();

    /**
     * Verifica si existe un cliente con el RUT dado
     */
    boolean existePorRut(String rut);

    /**
     * Verifica si existe un cliente con el email dado
     */
    boolean existePorEmail(String email);

    /**
     * Elimina un cliente
     */
    boolean eliminar(String id);

    /**
     * Cuenta total de clientes
     */
    long contar();
}
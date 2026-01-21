package ApiGymorEjecucion.Api.infrastructure.repository.memory;

import ApiGymorEjecucion.Api.domain.model.Cliente.Cliente;
import ApiGymorEjecucion.Api.domain.model.Cliente.TipoCliente;
import ApiGymorEjecucion.Api.domain.repository.ClienteRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Implementación en memoria del repositorio de Clientes
 *
 * ⚠️ SOLO PARA TESTS UNITARIOS
 *
 * En local y producción se usa ClienteRepositoryJpa con PostgreSQL
 */
@Repository
@Profile("test")  // ← SOLO TESTS
public class ClienteRepositoryInMemory implements ClienteRepository {

    private final Map<String, Cliente> clientes = new ConcurrentHashMap<>();

    @Override
    public Cliente guardar(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }
        clientes.put(cliente.getId(), cliente);
        return cliente;
    }

    @Override
    public Optional<Cliente> buscarPorId(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(clientes.get(id));
    }

    @Override
    public Optional<Cliente> buscarPorRut(String rut) {
        if (rut == null || rut.isBlank()) {
            return Optional.empty();
        }
        return clientes.values().stream()
                .filter(cliente -> cliente.getRut().equals(rut))
                .findFirst();
    }

    @Override
    public Optional<Cliente> buscarPorEmail(String email) {
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }
        return clientes.values().stream()
                .filter(cliente -> cliente.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public List<Cliente> buscarPorTipo(TipoCliente tipo) {
        if (tipo == null) {
            return new ArrayList<>();
        }
        return clientes.values().stream()
                .filter(cliente -> cliente.getTipo() == tipo)
                .collect(Collectors.toList());
    }

    @Override
    public List<Cliente> buscarTodos() {
        return new ArrayList<>(clientes.values());
    }

    @Override
    public List<Cliente> buscarActivos() {
        return clientes.values().stream()
                .filter(Cliente::isActivo)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existePorRut(String rut) {
        if (rut == null || rut.isBlank()) {
            return false;
        }
        return clientes.values().stream()
                .anyMatch(cliente -> cliente.getRut().equals(rut));
    }

    @Override
    public boolean existePorEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return clientes.values().stream()
                .anyMatch(cliente -> cliente.getEmail().equalsIgnoreCase(email));
    }

    @Override
    public boolean eliminar(String id) {
        if (id == null || id.isBlank()) {
            return false;
        }
        return clientes.remove(id) != null;
    }

    @Override
    public long contar() {
        return clientes.size();
    }

    /**
     * Método auxiliar para limpiar todos los datos (útil para tests)
     */
    public void limpiar() {
        clientes.clear();
    }
}
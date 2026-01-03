package ApiGymorEjecucion.Api.infrastructure.repository.memory;



import ApiGymorEjecucion.Api.domain.model.pedido.EstadoPedido;
import ApiGymorEjecucion.Api.domain.model.pedido.Pedido;
import ApiGymorEjecucion.Api.domain.repository.PedidoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Implementación en memoria del repositorio de Pedidos.
 * Utiliza ConcurrentHashMap para thread-safety.
 *
 * NOTA: Los datos se pierden al reiniciar la aplicación.
 * Esta implementación es temporal para desarrollo inicial.
 */
@Repository
public class PedidoRepositoryInMemory implements PedidoRepository {

    private final Map<String, Pedido> pedidos = new ConcurrentHashMap<>();

    @Override
    public Pedido guardar(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no puede ser nulo");
        }
        pedidos.put(pedido.getId(), pedido);
        return pedido;
    }

    @Override
    public Optional<Pedido> buscarPorId(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(pedidos.get(id));
    }

    @Override
    public List<Pedido> buscarPorCliente(String clienteId) {
        if (clienteId == null || clienteId.isBlank()) {
            return new ArrayList<>();
        }

        return pedidos.values().stream()
                .filter(pedido -> pedido.getClienteId().equals(clienteId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Pedido> buscarPorEstado(EstadoPedido estado) {
        if (estado == null) {
            return new ArrayList<>();
        }

        return pedidos.values().stream()
                .filter(pedido -> pedido.getEstado() == estado)
                .collect(Collectors.toList());
    }

    @Override
    public List<Pedido> buscarTodos() {
        return new ArrayList<>(pedidos.values());
    }

    @Override
    public boolean eliminar(String id) {
        if (id == null || id.isBlank()) {
            return false;
        }
        return pedidos.remove(id) != null;
    }

    @Override
    public boolean existe(String id) {
        if (id == null || id.isBlank()) {
            return false;
        }
        return pedidos.containsKey(id);
    }

    @Override
    public long contar() {
        return pedidos.size();
    }

    /**
     * Método auxiliar para limpiar todos los datos (útil para tests)
     */
    public void limpiar() {
        pedidos.clear();
    }
}
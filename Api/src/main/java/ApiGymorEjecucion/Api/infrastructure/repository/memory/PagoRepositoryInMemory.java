package ApiGymorEjecucion.Api.infrastructure.repository.memory;

import ApiGymorEjecucion.Api.domain.model.Pago.EstadoPago;
import ApiGymorEjecucion.Api.domain.model.Pago.Pago;
import ApiGymorEjecucion.Api.domain.repository.PagoRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Implementación en memoria del repositorio de Pagos
 *
 * Usada para desarrollo y testing local
 * (profile: local, dev, test)
 */
@Repository
@Profile("test")  // ← SOLO TESTS
public class PagoRepositoryInMemory implements PagoRepository {

    private final Map<String, Pago> pagos = new ConcurrentHashMap<>();

    // =========================
    // CRUD BÁSICO
    // =========================

    @Override
    public Pago guardar(Pago pago) {
        if (pago == null) {
            throw new IllegalArgumentException("El pago no puede ser nulo");
        }
        pagos.put(pago.getId(), pago);
        return pago;
    }

    @Override
    public Optional<Pago> buscarPorId(String id) {
        return Optional.ofNullable(pagos.get(id));
    }

    @Override
    public List<Pago> buscarTodos() {
        return new ArrayList<>(pagos.values());
    }

    // =========================
    // CONSULTAS DE NEGOCIO
    // =========================

    @Override
    public List<Pago> buscarPorPedidoId(String pedidoId) {
        return pagos.values().stream()
                .filter(pago -> pago.getPedidoId().equals(pedidoId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Pago> buscarPorReferenciaPasarela(String referencia) {
        return pagos.values().stream()
                .filter(pago ->
                        pago.getReferenciaPasarela() != null &&
                                pago.getReferenciaPasarela().equals(referencia)
                )
                .findFirst();
    }

    @Override
    public List<Pago> buscarPorEstado(EstadoPago estado) {
        return pagos.values().stream()
                .filter(pago -> pago.getEstado() == estado)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existePorPedidoId(String pedidoId) {
        return pagos.values().stream()
                .anyMatch(pago -> pago.getPedidoId().equals(pedidoId));
    }

    // =========================
    // ELIMINAR / CONTAR
    // =========================

    @Override
    public boolean eliminar(String id) {
        return pagos.remove(id) != null;
    }

    @Override
    public long contar() {
        return pagos.size();
    }

    // =========================
    // MÉTODOS AUXILIARES (TEST)
    // =========================

    public void limpiar() {
        pagos.clear();
    }

    public boolean estaVacio() {
        return pagos.isEmpty();
    }
}

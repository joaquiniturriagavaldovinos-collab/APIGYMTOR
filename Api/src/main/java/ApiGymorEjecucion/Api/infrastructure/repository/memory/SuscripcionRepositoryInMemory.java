package ApiGymorEjecucion.Api.infrastructure.repository.memory;

import ApiGymorEjecucion.Api.domain.model.servicio.Suscripcion;
import ApiGymorEjecucion.Api.domain.repository.SuscripcionRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Implementación en memoria del repositorio de Suscripciones
 *
 * Para desarrollo y testing local (profile: local, dev, test)
 */
@Repository
@Profile("test")  // ← SOLO TESTS
public class SuscripcionRepositoryInMemory implements SuscripcionRepository {

    private final Map<String, Suscripcion> suscripciones = new ConcurrentHashMap<>();

    @Override
    public Suscripcion guardar(Suscripcion suscripcion) {
        if (suscripcion == null) {
            throw new IllegalArgumentException("La suscripción no puede ser nula");
        }
        suscripciones.put(suscripcion.getId(), suscripcion);
        return suscripcion;
    }

    @Override
    public Optional<Suscripcion> buscarPorId(String id) {
        return Optional.ofNullable(suscripciones.get(id));
    }

    @Override
    public boolean eliminar(String id) {
        return suscripciones.remove(id) != null;
    }

    @Override
    public List<Suscripcion> buscarPorClienteId(String clienteId) {
        return suscripciones.values().stream()
                .filter(s -> s.getClienteId().equals(clienteId))
                .toList();
    }

    @Override
    public Optional<Suscripcion> buscarActivaPorCliente(String clienteId) {
        return suscripciones.values().stream()
                .filter(s -> s.getClienteId().equals(clienteId))
                .filter(Suscripcion::isActiva)
                .findFirst();
    }

    @Override
    public List<Suscripcion> buscarActivas() {
        return suscripciones.values().stream()
                .filter(Suscripcion::isActiva)
                .toList();
    }

    @Override
    public List<Suscripcion> buscarVencidas() {
        LocalDateTime ahora = LocalDateTime.now();
        return suscripciones.values().stream()
                .filter(s -> s.getFechaVencimiento().isBefore(ahora))
                .toList();
    }

    @Override
    public boolean existeActivaPorCliente(String clienteId) {
        return suscripciones.values().stream()
                .anyMatch(s -> s.getClienteId().equals(clienteId) && s.isActiva());
    }

    // ===== SOLO PARA TESTING =====

    public long contar() {
        return suscripciones.size();
    }

    public void limpiar() {
        suscripciones.clear();
    }

    public boolean estaVacio() {
        return suscripciones.isEmpty();
    }
}

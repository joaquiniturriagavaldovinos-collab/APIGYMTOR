package ApiGymorEjecucion.Api.infrastructure.repository.memory;

import ApiGymorEjecucion.Api.domain.model.servicio.Suscripcion;
import ApiGymorEjecucion.Api.domain.repository.SuscripcionRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementación en memoria del repositorio de Suscripciones
 * Para desarrollo y testing local
 */
@Repository
@Profile("test")
public class SuscripcionRepositoryInMemory implements SuscripcionRepository {

    private final Map<String, Suscripcion> suscripciones = new ConcurrentHashMap<>();

    // ===== MÉTODOS DE LA INTERFAZ =====

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
    public List<Suscripcion> buscarPorCliente(String clienteId) {  // ← CORREGIDO
        return suscripciones.values().stream()
                .filter(s -> s.getClienteId().equals(clienteId))
                .toList();
    }

    @Override
    public List<Suscripcion> buscarActivas() {
        return suscripciones.values().stream()
                .filter(Suscripcion::isActiva)
                .toList();
    }

    @Override
    public List<Suscripcion> buscarTodas() {  // ← AGREGADO
        return new ArrayList<>(suscripciones.values());
    }

    @Override
    public boolean eliminar(String id) {
        return suscripciones.remove(id) != null;
    }

    @Override
    public long contar() {
        return suscripciones.size();
    }

    // ===== MÉTODOS AUXILIARES (no están en la interfaz) =====

    /**
     * Busca la suscripción activa de un cliente
     */
    public Optional<Suscripcion> buscarActivaPorCliente(String clienteId) {
        return suscripciones.values().stream()
                .filter(s -> s.getClienteId().equals(clienteId))
                .filter(Suscripcion::isActiva)
                .findFirst();
    }

    /**
     * Busca suscripciones vencidas
     */
    public List<Suscripcion> buscarVencidas() {
        LocalDateTime ahora = LocalDateTime.now();
        return suscripciones.values().stream()
                .filter(s -> s.getFechaVencimiento().isBefore(ahora))
                .toList();
    }

    /**
     * Verifica si existe una suscripción activa para un cliente
     */
    public boolean existeActivaPorCliente(String clienteId) {
        return suscripciones.values().stream()
                .anyMatch(s -> s.getClienteId().equals(clienteId) && s.isActiva());
    }

    // ===== MÉTODOS DE UTILIDAD PARA TESTING =====

    /**
     * Limpia todos los datos en memoria
     */
    public void limpiar() {
        suscripciones.clear();
    }

    /**
     * Verifica si el repositorio está vacío
     */
    public boolean estaVacio() {
        return suscripciones.isEmpty();
    }

    /**
     * Obtiene todas las suscripciones como mapa
     */
    public Map<String, Suscripcion> obtenerMapa() {
        return new HashMap<>(suscripciones);
    }
}
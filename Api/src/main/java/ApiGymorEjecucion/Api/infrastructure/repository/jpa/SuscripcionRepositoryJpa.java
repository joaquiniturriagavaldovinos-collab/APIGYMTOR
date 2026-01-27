package ApiGymorEjecucion.Api.infrastructure.repository.jpa;

import ApiGymorEjecucion.Api.domain.model.servicio.Suscripcion;
import ApiGymorEjecucion.Api.domain.repository.SuscripcionRepository;
import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.suscripcion.SuscripcionEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Primary
@Profile("!test")
public class SuscripcionRepositoryJpa implements SuscripcionRepository {

    private final SuscripcionJpaRepository jpaRepository;

    public SuscripcionRepositoryJpa(SuscripcionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Suscripcion guardar(Suscripcion suscripcion) {
        SuscripcionEntity entity = mapearAEntity(suscripcion);
        SuscripcionEntity guardada = jpaRepository.save(entity);
        return mapearADominio(guardada);
    }

    @Override
    public Optional<Suscripcion> buscarPorId(String id) {
        return jpaRepository.findById(id)
                .map(this::mapearADominio);
    }

    // ✅ CORREGIR: Cambiar de buscarPorClienteId a buscarPorCliente
    @Override
    public List<Suscripcion> buscarPorCliente(String clienteId) {
        return jpaRepository.findByClienteId(clienteId).stream()
                .map(this::mapearADominio)
                .collect(Collectors.toList());
    }

    @Override
    public List<Suscripcion> buscarActivas() {
        return jpaRepository.findByActivaTrue().stream()
                .map(this::mapearADominio)
                .collect(Collectors.toList());
    }

    @Override
    public List<Suscripcion> buscarTodas() {
        return jpaRepository.findAll().stream()
                .map(this::mapearADominio)
                .toList();
    }

    @Override
    public boolean eliminar(String id) {
        if (jpaRepository.existsById(id)) {
            jpaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public long contar() {
        return jpaRepository.count();
    }

    // ===== MÉTODOS AUXILIARES (no están en la interfaz pero son útiles) =====

    public Optional<Suscripcion> buscarActivaPorCliente(String clienteId) {
        return jpaRepository.findByClienteIdAndActivaTrue(clienteId)
                .map(this::mapearADominio);
    }

    public List<Suscripcion> buscarVencidas() {
        return jpaRepository
                .findByFechaVencimientoBefore(LocalDateTime.now())
                .stream()
                .map(this::mapearADominio)
                .collect(Collectors.toList());
    }

    public boolean existeActivaPorCliente(String clienteId) {
        return jpaRepository.existsByClienteIdAndActivaTrue(clienteId);
    }

    // ===== MAPPERS =====

    private SuscripcionEntity mapearAEntity(Suscripcion s) {
        return new SuscripcionEntity(
                s.getId(),
                s.getClienteId(),
                s.getPlanId(),
                s.getFechaInicio(),
                s.getFechaVencimiento(),
                s.getSesionesRestantes(),
                s.isActiva(),
                s.isAutorrenovable(),
                s.getFechaContratacion()
        );
    }

    private Suscripcion mapearADominio(SuscripcionEntity e) {
        // Reconstruir suscripción desde persistencia
        Suscripcion suscripcion = Suscripcion.crearDesdePersistencia(
                e.getId(),
                e.getClienteId(),
                e.getPlanId(),
                e.getFechaInicio(),
                e.getFechaVencimiento(),
                e.getSesionesRestantes(),
                e.getFechaContratacion()
        );

        // Aplicar estado de activa/suspendida
        if (!e.isActiva()) {
            suscripcion.suspender();
        }

        // Aplicar configuración de autorenovación
        if (e.isAutorrenovable()) {
            suscripcion.habilitarAutorenovacion();
        }

        return suscripcion;
    }
}
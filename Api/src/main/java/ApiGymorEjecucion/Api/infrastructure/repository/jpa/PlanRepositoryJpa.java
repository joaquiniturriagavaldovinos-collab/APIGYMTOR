package ApiGymorEjecucion.Api.infrastructure.repository.jpa;

import ApiGymorEjecucion.Api.domain.model.servicio.Plan;
import ApiGymorEjecucion.Api.domain.repository.PlanRepository;
import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.plan.PlanEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@Primary  // Prioridad sobre InMemory si ambos están activos
@Profile("!test")  // Se activa en todos los perfiles EXCEPTO test
public class PlanRepositoryJpa implements PlanRepository {

    private final PlanJpaRepository jpaRepository;

    public PlanRepositoryJpa(PlanJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Plan guardar(Plan plan) {
        PlanEntity entity = mapToJpa(plan);
        PlanEntity saved = jpaRepository.save(entity);
        return mapToDomain(saved);
    }

    @Override
    public Optional<Plan> buscarPorId(String id) {
        return jpaRepository.findById(id)
                .map(this::mapToDomain);
    }

    @Override
    public List<Plan> buscarTodos() {
        return jpaRepository.findAll()
                .stream()
                .map(this::mapToDomain)
                .toList();
    }

    @Override
    public List<Plan> buscarActivos() {
        return jpaRepository.findByActivoTrue()
                .stream()
                .map(this::mapToDomain)
                .toList();
    }

    @Override
    public Optional<Plan> buscarPorNombre(String nombre) {
        return jpaRepository.findByNombreIgnoreCase(nombre)
                .map(this::mapToDomain);
    }

    @Override
    public boolean existePorNombre(String nombre) {
        return jpaRepository.existsByNombreIgnoreCase(nombre);
    }

    @Override
    public boolean eliminar(String id) {
        if (!jpaRepository.existsById(id)) {
            return false;
        }
        jpaRepository.deleteById(id);
        return true;
    }

    @Override
    public long contar() {
        return jpaRepository.count();
    }

    /* ===================== MAPPERS ===================== */

    private PlanEntity mapToJpa(Plan plan) {
        PlanEntity entity = new PlanEntity();
        entity.setId(plan.getId());
        entity.setNombre(plan.getNombre());
        entity.setDescripcion(plan.getDescripcion());
        entity.setPrecio(plan.getPrecio());
        entity.setDuracionMeses(plan.getDuracionMeses());
        entity.setSesionesIncluidas(plan.getSesionesIncluidas());
        entity.setActivo(plan.isActivo());
        entity.setFechaCreacion(plan.getFechaCreacion());
        return entity;
    }

    private Plan mapToDomain(PlanEntity entity) {
        return Plan.reconstruir(
                entity.getId(),
                entity.getNombre(),
                entity.getDescripcion(),
                entity.getPrecio(),
                entity.getDuracionMeses(),
                entity.getSesionesIncluidas(),
                entity.isActivo(),
                entity.getFechaCreacion()
        );
    }
}

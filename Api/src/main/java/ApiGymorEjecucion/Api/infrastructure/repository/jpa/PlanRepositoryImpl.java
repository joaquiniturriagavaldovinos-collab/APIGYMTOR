package ApiGymorEjecucion.Api.infrastructure.repository.jpa;

import ApiGymorEjecucion.Api.domain.model.servicio.Plan;
import ApiGymorEjecucion.Api.domain.repository.PlanRepository;
import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.PlanJpaEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PlanRepositoryImpl implements PlanRepository {

    private final PlanJpaRepository jpaRepository;

    public PlanRepositoryImpl(PlanJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Plan guardar(Plan plan) {
        PlanJpaEntity entity = mapToJpa(plan);
        PlanJpaEntity saved = jpaRepository.save(entity);
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

    /* ===================== MAPPERS ===================== */

    private PlanJpaEntity mapToJpa(Plan plan) {
        PlanJpaEntity entity = new PlanJpaEntity();
        entity.setId(plan.getId());
        entity.setNombre(plan.getNombre());
        entity.setDescripcion(plan.getDescripcion());
        entity.setPrecio(plan.getPrecio());
        entity.setDuracionMeses(plan.getDuracionMeses());
        entity.setActivo(plan.isActivo());
        entity.setFechaCreacion(plan.getFechaCreacion());
        return entity;
    }

    private Plan mapToDomain(PlanJpaEntity entity) {
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

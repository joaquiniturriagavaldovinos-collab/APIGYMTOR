package ApiGymorEjecucion.Api.infrastructure.repository.jpa;

import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.PlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanJpaRepository
        extends JpaRepository<PlanEntity, String> {
}

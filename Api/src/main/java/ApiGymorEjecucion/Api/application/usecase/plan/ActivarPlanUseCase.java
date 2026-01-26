package ApiGymorEjecucion.Api.application.usecase.plan;

import ApiGymorEjecucion.Api.domain.model.servicio.Plan;
import ApiGymorEjecucion.Api.domain.repository.PlanRepository;
import org.springframework.stereotype.Service;

@Service
public class ActivarPlanUseCase {

    private final PlanRepository planRepository;

    public ActivarPlanUseCase(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    public void ejecutar(String id) {
        // Validar
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID es requerido");
        }

        // Buscar
        Plan plan = planRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el plan con ID: " + id
                ));

        // Validar que esté desactivado
        if (plan.isActivo()) {
            throw new IllegalStateException("El plan ya está activo");
        }

        // DOMINIO: Activar
        plan.activar();

        // Persistir
        planRepository.guardar(plan);
    }
}
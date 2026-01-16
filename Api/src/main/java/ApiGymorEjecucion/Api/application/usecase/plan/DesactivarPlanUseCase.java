package ApiGymorEjecucion.Api.application.usecase.plan;

import ApiGymorEjecucion.Api.domain.model.servicio.Plan;
import ApiGymorEjecucion.Api.domain.repository.PlanRepository;
import org.springframework.stereotype.Service;

@Service
public class DesactivarPlanUseCase {

    private final PlanRepository planRepository;

    public DesactivarPlanUseCase(PlanRepository planRepository) {
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

        // Validar que esté activo
        if (!plan.isActivo()) {
            throw new IllegalStateException("El plan ya está desactivado");
        }

        // DOMINIO: Desactivar
        plan.desactivar();

        // Persistir
        planRepository.guardar(plan);

        // NOTA: Las suscripciones activas siguen funcionando
        // No se cancelan automáticamente al desactivar el plan
    }
}

package ApiGymorEjecucion.Api.application.usecase.plan;


import ApiGymorEjecucion.Api.application.dto.request.plan.ActualizarPlanRequest;
import ApiGymorEjecucion.Api.application.dto.response.plan.PlanResponse;
import ApiGymorEjecucion.Api.domain.model.servicio.Plan;
import ApiGymorEjecucion.Api.domain.repository.PlanRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ActualizarPlanUseCase {

    private final PlanRepository planRepository;

    public ActualizarPlanUseCase(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    public PlanResponse ejecutar(String id, ActualizarPlanRequest request) {
        // Buscar plan
        Plan plan = planRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el plan con ID: " + id
                ));

        // Actualizar precio
        plan.actualizarPrecio(request.getNuevoPrecio());


        // Persistir
        Plan actualizado = planRepository.guardar(plan);

        // Retornar
        return mapearAResponse(actualizado);
    }

    private PlanResponse mapearAResponse(Plan plan) {
        PlanResponse response = new PlanResponse();
        response.setId(plan.getId());
        response.setNombre(plan.getNombre());
        response.setPrecio(plan.getPrecio());
        response.setPrecioPorMes(plan.calcularPrecioPorMes());
        response.setActivo(plan.isActivo());
        return response;
    }

}
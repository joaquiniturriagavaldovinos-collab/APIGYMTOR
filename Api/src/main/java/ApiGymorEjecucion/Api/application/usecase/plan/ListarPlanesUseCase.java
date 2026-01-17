package ApiGymorEjecucion.Api.application.usecase.plan;


import ApiGymorEjecucion.Api.application.dto.response.plan.PlanResponse;
import ApiGymorEjecucion.Api.domain.model.servicio.Plan;
import ApiGymorEjecucion.Api.domain.repository.PlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListarPlanesUseCase {

    private final PlanRepository planRepository;

    public ListarPlanesUseCase(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    public List<PlanResponse> listarTodos() {
        return planRepository.buscarActivos().stream()
                .map(this::mapearAResponse)
                .toList();
    }


    public PlanResponse buscarPorId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID es requerido");
        }

        Plan plan = planRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el plan con ID: " + id
                ));

        return mapearAResponse(plan);
    }

    private PlanResponse mapearAResponse(Plan plan) {
        PlanResponse response = new PlanResponse();
        response.setId(plan.getId());
        response.setNombre(plan.getNombre());
        response.setDescripcion(plan.getDescripcion());
        response.setPrecio(plan.getPrecio());
        response.setDuracionMeses(plan.getDuracionMeses());
        response.setSesionesIncluidas(plan.getSesionesIncluidas());
        response.setEsSesionesIlimitadas(plan.esSesionesIlimitadas());
        response.setPrecioPorMes(plan.calcularPrecioPorMes());
        response.setActivo(plan.isActivo());
        return response;
    }
}
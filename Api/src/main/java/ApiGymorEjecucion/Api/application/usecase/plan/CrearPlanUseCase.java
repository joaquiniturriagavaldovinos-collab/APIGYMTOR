package ApiGymorEjecucion.Api.application.usecase.plan;

import ApiGymorEjecucion.Api.application.dto.request.plan.CrearPlanRequest;
import ApiGymorEjecucion.Api.application.dto.response.plan.PlanResponse;
import ApiGymorEjecucion.Api.domain.model.servicio.Plan;
import ApiGymorEjecucion.Api.domain.repository.PlanRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class CrearPlanUseCase {

    private final PlanRepository planRepository;

    public CrearPlanUseCase(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    public PlanResponse ejecutar(CrearPlanRequest request) {
        // Validar
        validarRequest(request);

        // DOMINIO: Crear plan
        Plan plan = Plan.crear(
                generarId(),
                request.getNombre(),
                request.getDescripcion(),
                request.getPrecio(),
                request.getDuracionMeses(),
                request.getSesionesIncluidas()
        );

        // Persistir
        Plan guardado = planRepository.guardar(plan);

        // Retornar
        return mapearAResponse(guardado);
    }

    private void validarRequest(CrearPlanRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }
        if (request.getNombre() == null || request.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es requerido");
        }
        if (request.getPrecio() == null || request.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }
        if (request.getDuracionMeses() <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor a cero");
        }
        if (request.getSesionesIncluidas() < 0) {
            throw new IllegalArgumentException("Las sesiones no pueden ser negativas (0 = ilimitadas)");
        }
    }

    private String generarId() {
        return "PLAN-" + System.currentTimeMillis();
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
        response.setFechaCreacion(plan.getFechaCreacion());
        return response;
    }
}
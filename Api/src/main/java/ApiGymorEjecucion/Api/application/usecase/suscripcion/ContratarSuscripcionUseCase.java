package ApiGymorEjecucion.Api.application.usecase.suscripcion;

import ApiGymorEjecucion.Api.application.dto.request.suscripcion.ContratarSuscripcionRequest;
import ApiGymorEjecucion.Api.application.dto.response.suscripcion.SuscripcionResponse;
import ApiGymorEjecucion.Api.domain.model.servicio.Plan;
import ApiGymorEjecucion.Api.domain.model.servicio.Suscripcion;
import ApiGymorEjecucion.Api.domain.repository.PlanRepository;
import ApiGymorEjecucion.Api.domain.repository.SuscripcionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ContratarSuscripcionUseCase {

    private final SuscripcionRepository suscripcionRepository;
    private final PlanRepository planRepository;

    public ContratarSuscripcionUseCase(SuscripcionRepository suscripcionRepository,
                                       PlanRepository planRepository) {
        this.suscripcionRepository = suscripcionRepository;
        this.planRepository = planRepository;
    }

    public SuscripcionResponse ejecutar(ContratarSuscripcionRequest request) {
        // Validar
        validarRequest(request);

        // Buscar plan
        Plan plan = planRepository.buscarPorId(request.getPlanId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el plan con ID: " + request.getPlanId()
                ));

        // Validar que el plan esté activo
        if (!plan.isActivo()) {
            throw new IllegalStateException("El plan no está disponible para contratación");
        }

        // DOMINIO: Crear suscripción
        Suscripcion suscripcion = Suscripcion.crear(
                generarId(),
                request.getClienteId(),
                request.getPlanId(),
                plan.getDuracionMeses(),
                plan.getSesionesIncluidas()
        );

        // Persistir
        Suscripcion guardada = suscripcionRepository.guardar(suscripcion);

        // Retornar
        return mapearAResponse(guardada);
    }

    private void validarRequest(ContratarSuscripcionRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }
        if (request.getClienteId() == null || request.getClienteId().isBlank()) {
            throw new IllegalArgumentException("El ID del cliente es requerido");
        }
        if (request.getPlanId() == null || request.getPlanId().isBlank()) {
            throw new IllegalArgumentException("El ID del plan es requerido");
        }
    }

    private String generarId() {
        return "SUB-" + System.currentTimeMillis();
    }

    private SuscripcionResponse mapearAResponse(Suscripcion suscripcion) {
        SuscripcionResponse response = new SuscripcionResponse();
        response.setId(suscripcion.getId());
        response.setClienteId(suscripcion.getClienteId());
        response.setPlanId(suscripcion.getPlanId());
        response.setFechaInicio(suscripcion.getFechaInicio());
        response.setFechaVencimiento(suscripcion.getFechaVencimiento());
        response.setSesionesRestantes(suscripcion.getSesionesRestantes());
        response.setTieneSesionesIlimitadas(suscripcion.tieneSesionesIlimitadas());
        response.setActiva(suscripcion.isActiva());
        response.setAutorrenovable(suscripcion.isAutorrenovable());
        response.setEstaVigente(suscripcion.estaVigente());
        response.setEstaVencida(suscripcion.estaVencida());
        response.setDiasRestantes(suscripcion.diasRestantes());
        return response;
    }
}
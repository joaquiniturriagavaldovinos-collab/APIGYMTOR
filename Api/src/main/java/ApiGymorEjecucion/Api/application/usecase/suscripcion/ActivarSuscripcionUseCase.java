package ApiGymorEjecucion.Api.application.usecase.suscripcion;

import ApiGymorEjecucion.Api.application.dto.response.suscripcion.SuscripcionResponse;
import ApiGymorEjecucion.Api.domain.model.servicio.Suscripcion;
import ApiGymorEjecucion.Api.domain.repository.SuscripcionRepository;
import org.springframework.stereotype.Service;

/**
 * Caso de Uso: Activar/Reactivar Suscripción
 *
 * Activa una suscripción previamente suspendida.
 */
@Service
public class ActivarSuscripcionUseCase {

    private final SuscripcionRepository suscripcionRepository;

    public ActivarSuscripcionUseCase(SuscripcionRepository suscripcionRepository) {
        this.suscripcionRepository = suscripcionRepository;
    }

    public SuscripcionResponse ejecutar(String suscripcionId) {
        System.out.println("▶️ Activando suscripción: " + suscripcionId);

        // Validar
        if (suscripcionId == null || suscripcionId.isBlank()) {
            throw new IllegalArgumentException("El ID de la suscripción es requerido");
        }

        // Buscar suscripción
        Suscripcion suscripcion = suscripcionRepository.buscarPorId(suscripcionId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró la suscripción con ID: " + suscripcionId
                ));

        // Validar que no esté ya activa
        if (suscripcion.isActiva()) {
            throw new IllegalStateException("La suscripción ya está activa");
        }

        // DOMINIO: Activar
        suscripcion.activar();

        // Persistir
        Suscripcion activada = suscripcionRepository.guardar(suscripcion);

        System.out.println("✅ Suscripción activada exitosamente");

        return mapearAResponse(activada);
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
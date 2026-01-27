package ApiGymorEjecucion.Api.application.usecase.suscripcion;

import ApiGymorEjecucion.Api.application.dto.response.suscripcion.SuscripcionResponse;
import ApiGymorEjecucion.Api.domain.model.servicio.Suscripcion;
import ApiGymorEjecucion.Api.domain.repository.SuscripcionRepository;
import org.springframework.stereotype.Service;

@Service
public class ConsumirSesionUseCase {

    private final SuscripcionRepository suscripcionRepository;

    public ConsumirSesionUseCase(SuscripcionRepository suscripcionRepository) {
        this.suscripcionRepository = suscripcionRepository;
    }

    public SuscripcionResponse ejecutar(String suscripcionId) {
        System.out.println("🏋️ Consumiendo sesión de: " + suscripcionId);

        // Validar
        if (suscripcionId == null || suscripcionId.isBlank()) {
            throw new IllegalArgumentException("El ID de la suscripción es requerido");
        }

        // Buscar suscripción
        Suscripcion suscripcion = suscripcionRepository.buscarPorId(suscripcionId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró la suscripción con ID: " + suscripcionId
                ));

        // Validar que esté vigente
        if (!suscripcion.estaVigente()) {
            throw new IllegalStateException(
                    "La suscripción no está vigente. Estado: " +
                            (suscripcion.estaVencida() ? "VENCIDA" : "INACTIVA")
            );
        }

        // Validar que tenga sesiones disponibles
        if (!suscripcion.tieneSesionesDisponibles()) {
            throw new IllegalStateException("No quedan sesiones disponibles en esta suscripción");
        }

        // DOMINIO: Consumir sesión
        int sesionesAntes = suscripcion.getSesionesRestantes();
        suscripcion.consumirSesion();
        int sesionesDespues = suscripcion.getSesionesRestantes();

        System.out.println("   Sesiones antes: " + sesionesAntes);
        System.out.println("   Sesiones después: " + sesionesDespues);

        // Persistir
        Suscripcion actualizada = suscripcionRepository.guardar(suscripcion);

        System.out.println("✅ Sesión consumida exitosamente");

        // Retornar con mapper COMPLETO
        return mapearAResponse(actualizada);
    }

    // ✅ MAPPER COMPLETO (corregido)
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
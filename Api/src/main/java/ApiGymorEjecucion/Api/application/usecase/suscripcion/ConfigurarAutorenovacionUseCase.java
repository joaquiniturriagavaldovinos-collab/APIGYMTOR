package ApiGymorEjecucion.Api.application.usecase.suscripcion;

import ApiGymorEjecucion.Api.application.dto.response.suscripcion.SuscripcionResponse;
import ApiGymorEjecucion.Api.domain.model.servicio.Suscripcion;
import ApiGymorEjecucion.Api.domain.repository.SuscripcionRepository;
import org.springframework.stereotype.Service;

/**
 * Caso de Uso: Configurar Autorenovación
 */
@Service
public class ConfigurarAutorenovacionUseCase {

    private final SuscripcionRepository suscripcionRepository;

    public ConfigurarAutorenovacionUseCase(SuscripcionRepository suscripcionRepository) {
        this.suscripcionRepository = suscripcionRepository;
    }

    public SuscripcionResponse ejecutar(String suscripcionId, boolean habilitar) {
        System.out.println((habilitar ? "✅ Habilitando" : "❌ Deshabilitando") +
                " autorenovación para: " + suscripcionId);

        // Validar
        if (suscripcionId == null || suscripcionId.isBlank()) {
            throw new IllegalArgumentException("El ID de la suscripción es requerido");
        }

        // Buscar suscripción
        Suscripcion suscripcion = suscripcionRepository.buscarPorId(suscripcionId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró la suscripción con ID: " + suscripcionId
                ));

        // DOMINIO: Configurar autorenovación
        if (habilitar) {
            suscripcion.habilitarAutorenovacion();
        } else {
            suscripcion.deshabilitarAutorenovacion();
        }

        // Persistir
        Suscripcion actualizada = suscripcionRepository.guardar(suscripcion);

        System.out.println("✅ Autorenovación configurada exitosamente");

        return mapearAResponse(actualizada);
    }

    private SuscripcionResponse mapearAResponse(Suscripcion suscripcion) {
        SuscripcionResponse response = new SuscripcionResponse();
        response.setId(suscripcion.getId());
        response.setClienteId(suscripcion.getClienteId());
        response.setPlanId(suscripcion.getPlanId());
        response.setAutorrenovable(suscripcion.isAutorrenovable());
        response.setActiva(suscripcion.isActiva());
        response.setEstaVigente(suscripcion.estaVigente());
        return response;
    }
}
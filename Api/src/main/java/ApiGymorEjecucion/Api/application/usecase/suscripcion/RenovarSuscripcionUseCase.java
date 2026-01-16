package ApiGymorEjecucion.Api.application.usecase.suscripcion;

import ApiGymorEjecucion.Api.application.dto.response.suscripcion.SuscripcionResponse;
import ApiGymorEjecucion.Api.domain.model.servicio.Suscripcion;
import ApiGymorEjecucion.Api.domain.repository.SuscripcionRepository;
import org.springframework.stereotype.Service;

@Service
public class RenovarSuscripcionUseCase {

    private final SuscripcionRepository suscripcionRepository;

    public RenovarSuscripcionUseCase(SuscripcionRepository suscripcionRepository) {
        this.suscripcionRepository = suscripcionRepository;
    }

    public SuscripcionResponse ejecutar(
            String suscripcionId, int duracionMeses) {

        // Validar
        if (suscripcionId == null || suscripcionId.isBlank()) {
            throw new IllegalArgumentException("El ID de la suscripción es requerido");
        }
        if (duracionMeses <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor a cero");
        }

        // Buscar suscripción
        Suscripcion suscripcion = suscripcionRepository.buscarPorId(suscripcionId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró la suscripción con ID: " + suscripcionId
                ));

        // DOMINIO: Renovar
        suscripcion.renovar(duracionMeses);

        // Persistir
        Suscripcion renovada = suscripcionRepository.guardar(suscripcion);

        // Retornar
        return mapearAResponse(renovada);
    }

    private SuscripcionResponse mapearAResponse(Suscripcion suscripcion) {
        SuscripcionResponse response =
                new SuscripcionResponse();

        response.setId(suscripcion.getId());
        response.setClienteId(suscripcion.getClienteId());
        response.setFechaInicio(suscripcion.getFechaInicio());
        response.setFechaVencimiento(suscripcion.getFechaVencimiento());
        response.setActiva(suscripcion.isActiva());
        response.setEstaVigente(suscripcion.estaVigente());
        response.setDiasRestantes(suscripcion.diasRestantes());

        return response;
    }
}

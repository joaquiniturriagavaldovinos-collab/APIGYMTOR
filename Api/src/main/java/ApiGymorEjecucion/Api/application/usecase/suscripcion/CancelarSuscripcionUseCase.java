package ApiGymorEjecucion.Api.application.usecase.suscripcion;

import ApiGymorEjecucion.Api.domain.model.servicio.Suscripcion;
import ApiGymorEjecucion.Api.domain.repository.SuscripcionRepository;
import org.springframework.stereotype.Service;

@Service
public class CancelarSuscripcionUseCase {

    private final SuscripcionRepository suscripcionRepository;

    public CancelarSuscripcionUseCase(SuscripcionRepository suscripcionRepository) {
        this.suscripcionRepository = suscripcionRepository;
    }

    public void ejecutar(String suscripcionId) {
        // Validar
        if (suscripcionId == null || suscripcionId.isBlank()) {
            throw new IllegalArgumentException("El ID de la suscripción es requerido");
        }

        // Buscar suscripción
        Suscripcion suscripcion = suscripcionRepository.buscarPorId(suscripcionId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró la suscripción con ID: " + suscripcionId
                ));

        // Validar que esté activa
        if (!suscripcion.isActiva()) {
            throw new IllegalStateException("La suscripción ya está cancelada");
        }

        // DOMINIO: Cancelar
        suscripcion.cancelar();

        // Persistir
        suscripcionRepository.guardar(suscripcion);

        // Aquí futuras implementaciones:
        // - Procesar reembolso proporcional si aplica
        // - Notificar al cliente
        // - Emitir evento SuscripcionCancelada
    }
}

package ApiGymorEjecucion.Api.application.usecase.suscripcion;

import ApiGymorEjecucion.Api.domain.model.servicio.Suscripcion;
import ApiGymorEjecucion.Api.domain.repository.SuscripcionRepository;
import org.springframework.stereotype.Service;

/**
 * Caso de Uso: Suspender Suscripción
 *
 * Suspende temporalmente una suscripción (diferente de cancelar).
 * La suscripción puede reactivarse posteriormente.
 */
@Service
public class SuspenderSuscripcionUseCase {

    private final SuscripcionRepository suscripcionRepository;

    public SuspenderSuscripcionUseCase(SuscripcionRepository suscripcionRepository) {
        this.suscripcionRepository = suscripcionRepository;
    }

    public void ejecutar(String suscripcionId) {
        System.out.println("⏸️ Suspendiendo suscripción: " + suscripcionId);

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
            throw new IllegalStateException("La suscripción ya está suspendida o cancelada");
        }

        // DOMINIO: Suspender
        suscripcion.suspender();

        // Persistir
        suscripcionRepository.guardar(suscripcion);

        System.out.println("✅ Suscripción suspendida exitosamente");
    }
}
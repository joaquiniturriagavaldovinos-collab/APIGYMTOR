package ApiGymorEjecucion.Api.application.usecase.servicio;

import ApiGymorEjecucion.Api.domain.model.servicio.Servicio;
import ApiGymorEjecucion.Api.domain.repository.ServicioRepository;
import org.springframework.stereotype.Service;

@Service
public class ActivarServicioUseCase {

    private final ServicioRepository servicioRepository;

    public ActivarServicioUseCase(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    public void ejecutar(String id) {
        System.out.println("🔄 Activando servicio: " + id);

        // Validar
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID es requerido");
        }

        // Buscar
        Servicio servicio = servicioRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el servicio con ID: " + id
                ));

        // Validar que esté desactivado
        if (servicio.isActivo()) {
            throw new IllegalStateException("El servicio ya está activo");
        }

        // DOMINIO: Activar
        servicio.activar();

        // Persistir
        servicioRepository.guardar(servicio);

        System.out.println("✅ Servicio activado exitosamente");
    }
}
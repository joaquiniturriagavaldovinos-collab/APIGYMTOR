package ApiGymorEjecucion.Api.application.usecase.servicio;

import ApiGymorEjecucion.Api.domain.model.servicio.Servicio;
import ApiGymorEjecucion.Api.domain.repository.ServicioRepository;
import org.springframework.stereotype.Service;

@Service
public class DesactivarServicioUseCase {

    private final ServicioRepository servicioRepository;

    public DesactivarServicioUseCase(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    public void ejecutar(String id) {
        // Validar
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID es requerido");
        }

        // Buscar
        Servicio servicio = servicioRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el servicio con ID: " + id
                ));

        // Validar que esté activo
        if (!servicio.isActivo()) {
            throw new IllegalStateException("El servicio ya está desactivado");
        }

        // DOMINIO: Desactivar
        servicio.desactivar();

        // Persistir
        servicioRepository.guardar(servicio);
    }
}
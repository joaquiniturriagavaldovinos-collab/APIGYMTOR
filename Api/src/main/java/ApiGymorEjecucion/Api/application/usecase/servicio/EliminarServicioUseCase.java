package ApiGymorEjecucion.Api.application.usecase.servicio;

import ApiGymorEjecucion.Api.domain.repository.ServicioRepository;
import org.springframework.stereotype.Service;

/**
 * Caso de Uso: Eliminar Servicio (Hard Delete)
 *
 * ⚠️ IMPORTANTE: Este es un borrado físico de la base de datos.
 * Para uso administrativo únicamente.
 */
@Service
public class EliminarServicioUseCase {

    private final ServicioRepository servicioRepository;

    public EliminarServicioUseCase(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    public void ejecutar(String id) {
        System.out.println("\n========================================");
        System.out.println("🗑️ ELIMINANDO SERVICIO (HARD DELETE)");
        System.out.println("========================================");
        System.out.println("Servicio ID: " + id);

        // Validar
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID es requerido");
        }


        // Verificar que existe
        boolean existe = servicioRepository.buscarPorId(id).isPresent();
        if (!existe) {
            throw new IllegalArgumentException(
                    "No se encontró el servicio con ID: " + id
            );
        }

        // Eliminar
        boolean eliminado = servicioRepository.eliminar(id);

        if (eliminado) {
            System.out.println("✅ Servicio eliminado permanentemente");
        } else {
            System.out.println("❌ No se pudo eliminar el servicio");
            throw new IllegalStateException("Error al eliminar el servicio");
        }

        System.out.println("========================================\n");
    }
}
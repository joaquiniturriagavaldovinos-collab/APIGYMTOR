package ApiGymorEjecucion.Api.application.usecase.servicio;

import ApiGymorEjecucion.Api.application.dto.request.servicio.ActualizarServicioRequest;
import ApiGymorEjecucion.Api.application.dto.response.servicio.ServicioResponse;
import ApiGymorEjecucion.Api.domain.model.servicio.ModalidadClase;
import ApiGymorEjecucion.Api.domain.model.servicio.Servicio;
import ApiGymorEjecucion.Api.domain.repository.ServicioRepository;
import org.springframework.stereotype.Service;

@Service
public class ActualizarServicioUseCase {

    private final ServicioRepository servicioRepository;

    public ActualizarServicioUseCase(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    public ServicioResponse ejecutar(String id, ActualizarServicioRequest request) {

        System.out.println("\n========================================");
        System.out.println("🔧 ACTUALIZANDO SERVICIO");
        System.out.println("========================================");
        System.out.println("Servicio ID: " + id);

        // Validar
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID es requerido");
        }

        // Buscar servicio
        Servicio servicio = servicioRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el servicio con ID: " + id
                ));

        System.out.println("Servicio encontrado: " + servicio.getNombre());

        // Actualizar campos que vengan en el request (actualización parcial)
        int camposActualizados = 0;

        if (request.getNombre() != null) {
            System.out.println("📝 Actualizando nombre: " + request.getNombre());
            servicio.actualizarNombre(request.getNombre());
            camposActualizados++;
        }

        if (request.getDescripcion() != null) {
            System.out.println("📝 Actualizando descripción");
            servicio.actualizarDescripcion(request.getDescripcion());
            camposActualizados++;
        }

        if (request.getModalidad() != null) {
            System.out.println("📝 Actualizando modalidad: " + request.getModalidad());
            ModalidadClase modalidad = ModalidadClase.valueOf(request.getModalidad());
            servicio.actualizarModalidad(modalidad);
            camposActualizados++;
        }

        if (request.getPrecioSesion() != null) {
            System.out.println("💰 Actualizando precio: " + request.getPrecioSesion());
            servicio.actualizarPrecio(request.getPrecioSesion());
            camposActualizados++;
        }

        if (request.getDuracionMinutos() != null) {
            System.out.println("⏱️ Actualizando duración: " + request.getDuracionMinutos() + " minutos");
            servicio.actualizarDuracion(request.getDuracionMinutos());
            camposActualizados++;
        }

        if (request.getCapacidadMaxima() != null) {
            System.out.println("👥 Actualizando capacidad: " + request.getCapacidadMaxima() + " personas");
            servicio.actualizarCapacidad(request.getCapacidadMaxima());
            camposActualizados++;
        }

        if (camposActualizados == 0) {
            System.out.println("⚠️ No se proporcionaron campos para actualizar");
            throw new IllegalArgumentException("Debe proporcionar al menos un campo para actualizar");
        }

        System.out.println("✅ Campos actualizados: " + camposActualizados);

        // Persistir
        System.out.println("💾 Guardando cambios...");
        Servicio actualizado = servicioRepository.guardar(servicio);

        System.out.println("✅ Servicio actualizado exitosamente");
        System.out.println("========================================\n");

        // Retornar
        return mapearAResponse(actualizado);
    }

    private ServicioResponse mapearAResponse(Servicio servicio) {
        ServicioResponse response = new ServicioResponse();

        response.setId(servicio.getId());
        response.setNombre(servicio.getNombre());
        response.setDescripcion(servicio.getDescripcion());
        response.setModalidad(servicio.getModalidad().name());
        response.setModalidadDescripcion(servicio.getModalidad().getDescripcion());
        response.setPrecioSesion(servicio.getPrecioSesion());
        response.setDuracionMinutos(servicio.getDuracionMinutos());
        response.setCapacidadMaxima(servicio.getCapacidadMaxima());
        response.setActivo(servicio.isActivo());
        response.setFechaCreacion(servicio.getFechaCreacion());

        return response;
    }
}
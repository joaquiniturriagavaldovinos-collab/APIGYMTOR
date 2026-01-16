package ApiGymorEjecucion.Api.application.usecase.servicio;

import ApiGymorEjecucion.Api.application.dto.request.servicio.CrearServicioRequest;
import ApiGymorEjecucion.Api.application.dto.response.servicio.ServicioResponse;
import ApiGymorEjecucion.Api.domain.model.servicio.ModalidadClase;
import ApiGymorEjecucion.Api.domain.model.servicio.Servicio;
import ApiGymorEjecucion.Api.domain.repository.ServicioRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class CrearServicioUseCase {

    private final ServicioRepository servicioRepository;

    public CrearServicioUseCase(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    public ServicioResponse ejecutar(CrearServicioRequest request) {
        // Validar
        validarRequest(request);

        // Convertir modalidad
        ModalidadClase modalidad = ModalidadClase.valueOf(request.getModalidad());

        // DOMINIO: Crear servicio
        Servicio servicio = Servicio.crear(
                generarId(),
                request.getNombre(),
                request.getDescripcion(),
                modalidad,
                request.getPrecioSesion(),
                request.getDuracionMinutos(),
                request.getCapacidadMaxima()
        );

        // Persistir
        Servicio guardado = servicioRepository.guardar(servicio);

        // Retornar
        return mapearAResponse(guardado);
    }

    private void validarRequest(CrearServicioRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }
        if (request.getNombre() == null || request.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es requerido");
        }
        if (request.getModalidad() == null || request.getModalidad().isBlank()) {
            throw new IllegalArgumentException("La modalidad es requerida");
        }
        if (request.getPrecioSesion() == null || request.getPrecioSesion().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }
        if (request.getDuracionMinutos() <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor a cero");
        }
        if (request.getCapacidadMaxima() <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor a cero");
        }
    }

    private String generarId() {
        return "SRV-" + System.currentTimeMillis();
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
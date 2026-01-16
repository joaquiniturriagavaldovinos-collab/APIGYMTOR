package ApiGymorEjecucion.Api.application.usecase.servicio;


import ApiGymorEjecucion.Api.application.dto.request.servicio.ActualizarServicioRequest;
import ApiGymorEjecucion.Api.application.dto.response.servicio.ServicioResponse;
import ApiGymorEjecucion.Api.domain.model.servicio.Servicio;
import ApiGymorEjecucion.Api.domain.repository.ServicioRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ActualizarServicioUseCase {

    private final ServicioRepository servicioRepository;

    public ActualizarServicioUseCase(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    public ServicioResponse ejecutar(
            String id, ActualizarServicioRequest request) {

        // Validar
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID es requerido");
        }

        // Buscar servicio
        Servicio servicio = servicioRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el servicio con ID: " + id
                ));

        // Actualizar precio si viene en request
        if (request.getNuevoPrecio() != null) {
            servicio.actualizarPrecio(request.getNuevoPrecio());
        }

        // Persistir
        Servicio actualizado = servicioRepository.guardar(servicio);

        // Retornar
        return mapearAResponse(actualizado);
    }

    private ServicioResponse mapearAResponse(Servicio servicio) {
        ServicioResponse response =
                new ServicioResponse();

        response.setId(servicio.getId());
        response.setNombre(servicio.getNombre());
        response.setPrecioSesion(servicio.getPrecioSesion());
        response.setActivo(servicio.isActivo());

        return response;
    }
}
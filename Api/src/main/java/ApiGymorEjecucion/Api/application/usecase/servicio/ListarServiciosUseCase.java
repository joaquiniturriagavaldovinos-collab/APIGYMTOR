package ApiGymorEjecucion.Api.application.usecase.servicio;

import ApiGymorEjecucion.Api.application.dto.response.servicio.ServicioResponse;
import ApiGymorEjecucion.Api.domain.model.servicio.ModalidadClase;
import ApiGymorEjecucion.Api.domain.model.servicio.Servicio;
import ApiGymorEjecucion.Api.domain.repository.ServicioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListarServiciosUseCase {

    private final ServicioRepository servicioRepository;

    public ListarServiciosUseCase(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    public List<ServicioResponse> listarTodos() {
        return servicioRepository.buscarTodos().stream()
                .filter(Servicio::isActivo)
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    public List<ServicioResponse> listarPorModalidad(String modalidad) {
        if (modalidad == null || modalidad.isBlank()) {
            throw new IllegalArgumentException("La modalidad es requerida");
        }

        ModalidadClase modalidadClase;
        try {
            modalidadClase = ModalidadClase.valueOf(modalidad);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Modalidad inválida: " + modalidad);
        }

        return servicioRepository.buscarPorModalidad(modalidadClase).stream()
                .filter(Servicio::isActivo)
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    private ServicioResponse mapearAResponse(Servicio servicio) {
        ServicioResponse response =
                new ServicioResponse();

        response.setId(servicio.getId());
        response.setNombre(servicio.getNombre());
        response.setDescripcion(servicio.getDescripcion());
        response.setModalidad(servicio.getModalidad().name());
        response.setModalidadDescripcion(servicio.getModalidad().getDescripcion());
        response.setPrecioSesion(servicio.getPrecioSesion());
        response.setDuracionMinutos(servicio.getDuracionMinutos());
        response.setCapacidadMaxima(servicio.getCapacidadMaxima());
        response.setActivo(servicio.isActivo());

        return response;
    }
}
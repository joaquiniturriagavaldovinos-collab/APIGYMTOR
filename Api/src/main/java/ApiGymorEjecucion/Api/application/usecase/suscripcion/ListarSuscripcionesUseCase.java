package ApiGymorEjecucion.Api.application.usecase.suscripcion;

import ApiGymorEjecucion.Api.application.dto.response.suscripcion.SuscripcionResponse;
import ApiGymorEjecucion.Api.domain.model.servicio.Suscripcion;
import ApiGymorEjecucion.Api.domain.repository.SuscripcionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarSuscripcionesUseCase {

    private final SuscripcionRepository suscripcionRepository;

    public ListarSuscripcionesUseCase(SuscripcionRepository suscripcionRepository) {
        this.suscripcionRepository = suscripcionRepository;
    }

    /**
     * Lista todas las suscripciones
     */
    public List<SuscripcionResponse> listarTodas() {
        return suscripcionRepository.buscarTodas().stream()
                .map(this::mapearAResponse)
                .toList();
    }

    /**
     * Lista suscripciones de un cliente específico
     */
    public List<SuscripcionResponse> listarPorCliente(String clienteId) {
        if (clienteId == null || clienteId.isBlank()) {
            throw new IllegalArgumentException("El ID del cliente es requerido");
        }

        return suscripcionRepository.buscarPorCliente(clienteId).stream()
                .map(this::mapearAResponse)
                .toList();
    }

    /**
     * Lista solo suscripciones activas
     */
    public List<SuscripcionResponse> listarActivas() {
        return suscripcionRepository.buscarActivas().stream()
                .map(this::mapearAResponse)
                .toList();
    }

    /**
     * Lista suscripciones vigentes de un cliente
     */
    public List<SuscripcionResponse> listarVigentesPorCliente(String clienteId) {
        if (clienteId == null || clienteId.isBlank()) {
            throw new IllegalArgumentException("El ID del cliente es requerido");
        }

        return suscripcionRepository.buscarPorCliente(clienteId).stream()
                .filter(Suscripcion::estaVigente)
                .map(this::mapearAResponse)
                .toList();
    }

    /**
     * Busca una suscripción por ID
     */
    public SuscripcionResponse buscarPorId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID es requerido");
        }

        Suscripcion suscripcion = suscripcionRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró la suscripción con ID: " + id
                ));

        return mapearAResponse(suscripcion);
    }

    private SuscripcionResponse mapearAResponse(Suscripcion suscripcion) {
        SuscripcionResponse response = new SuscripcionResponse();
        response.setId(suscripcion.getId());
        response.setClienteId(suscripcion.getClienteId());
        response.setPlanId(suscripcion.getPlanId());
        response.setFechaInicio(suscripcion.getFechaInicio());
        response.setFechaVencimiento(suscripcion.getFechaVencimiento());
        response.setSesionesRestantes(suscripcion.getSesionesRestantes());
        response.setTieneSesionesIlimitadas(suscripcion.tieneSesionesIlimitadas());
        response.setActiva(suscripcion.isActiva());
        response.setAutorrenovable(suscripcion.isAutorrenovable());
        response.setEstaVigente(suscripcion.estaVigente());
        response.setEstaVencida(suscripcion.estaVencida());
        response.setDiasRestantes(suscripcion.diasRestantes());
        return response;
    }
}
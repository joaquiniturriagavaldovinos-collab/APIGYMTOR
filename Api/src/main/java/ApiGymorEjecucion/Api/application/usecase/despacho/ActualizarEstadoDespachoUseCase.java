package ApiGymorEjecucion.Api.application.usecase.despacho;

import ApiGymorEjecucion.Api.application.dto.request.despacho.ActualizarEstadoRequest;
import ApiGymorEjecucion.Api.application.dto.response.despacho.DespachoResponse;
import ApiGymorEjecucion.Api.domain.model.Despacho.Despacho;
import ApiGymorEjecucion.Api.domain.repository.DespachoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Caso de Uso: Actualizar Estado de Despacho
 *
 * Este caso de uso se ejecuta cuando:
 * - El transportista notifica cambios de estado (integración API)
 * - El operador actualiza manualmente el estado
 */
@Service
public class ActualizarEstadoDespachoUseCase {

    private final DespachoRepository despachoRepository;

    public ActualizarEstadoDespachoUseCase(DespachoRepository despachoRepository) {
        this.despachoRepository = despachoRepository;
    }

    public DespachoResponse ejecutar(
            String despachoId, ActualizarEstadoRequest request) {

        // Validar
        validarRequest(request);

        // Buscar despacho
        Despacho despacho = despachoRepository.buscarPorId(despachoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el despacho con ID: " + despachoId
                ));

        // Actualizar según tipo de actualización
        switch (request.getTipoActualizacion()) {
            case "ENTREGA_CONFIRMADA":
                despacho.confirmarEntrega();
                break;

            case "FECHA_ESTIMADA":
                if (request.getFechaEntregaEstimada() != null) {
                    despacho.establecerFechaEntregaEstimada(request.getFechaEntregaEstimada());
                }
                break;

            case "OBSERVACIONES":
                if (request.getObservaciones() != null) {
                    despacho.setObservaciones(request.getObservaciones());
                }
                break;

            default:
                throw new IllegalArgumentException(
                        "Tipo de actualización no soportado: " + request.getTipoActualizacion()
                );
        }

        // Persistir
        Despacho actualizado = despachoRepository.guardar(despacho);

        // Retornar
        return mapearAResponse(actualizado);
    }

    private void validarRequest(ActualizarEstadoRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }
        if (request.getTipoActualizacion() == null || request.getTipoActualizacion().isBlank()) {
            throw new IllegalArgumentException("El tipo de actualización es requerido");
        }
    }

    private DespachoResponse mapearAResponse(Despacho despacho) {
        DespachoResponse response =
                new DespachoResponse();

        response.setId(despacho.getId());
        response.setPedidoId(despacho.getPedidoId());

        if (despacho.getGuiaDespacho() != null) {
            response.setNumeroGuia(despacho.getGuiaDespacho().getNumero());
        }

        response.setFechaEntregaReal(despacho.getFechaEntregaReal());
        response.setEstaEntregado(despacho.estaEntregado());

        return response;
    }
}
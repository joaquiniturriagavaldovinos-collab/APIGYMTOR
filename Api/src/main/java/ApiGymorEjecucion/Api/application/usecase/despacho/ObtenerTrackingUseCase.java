package ApiGymorEjecucion.Api.application.usecase.despacho;



import ApiGymorEjecucion.Api.application.dto.response.despacho.TrackingResponse;
import ApiGymorEjecucion.Api.domain.model.Despacho.Despacho;
import ApiGymorEjecucion.Api.domain.repository.DespachoRepository;
import ApiGymorEjecucion.Api.infrastructure.external.shipping.TransportistaApiClient;
import org.springframework.stereotype.Service;

/**
 * Caso de Uso: Obtener Tracking en Tiempo Real
 *
 * Consulta el estado actual del envío en la API del transportista
 */
@Service
public class ObtenerTrackingUseCase {

    private final DespachoRepository despachoRepository;
    private final TransportistaApiClient transportistaClient;

    public ObtenerTrackingUseCase(DespachoRepository despachoRepository,
                                  TransportistaApiClient transportistaClient) {
        this.despachoRepository = despachoRepository;
        this.transportistaClient = transportistaClient;
    }

    public TrackingResponse ejecutar(String pedidoId) {
        // Validar
        if (pedidoId == null || pedidoId.isBlank()) {
            throw new IllegalArgumentException("El ID del pedido es requerido");
        }

        // Buscar despacho
        Despacho despacho = despachoRepository.buscarPorPedidoId(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró despacho para el pedido: " + pedidoId
                ));

        // Validar que tenga guía
        if (despacho.getGuiaDespacho() == null) {
            throw new IllegalStateException("El despacho no tiene guía asignada");
        }

        // Consultar API del transportista
        TransportistaApiClient.EstadoEnvioResponse estadoActual =
                transportistaClient.consultarEstado(despacho.getGuiaDespacho().getNumero());

        // Mapear a response
        TrackingResponse response = new TrackingResponse();
        response.setPedidoId(pedidoId);
        response.setNumeroGuia(despacho.getGuiaDespacho().getNumero());
        response.setUrlTracking(despacho.getGuiaDespacho().getUrlTracking());
        response.setEstadoActual(estadoActual.getEstado());
        response.setDescripcionEstado(estadoActual.getDescripcion());
        response.setFechaUltimaActualizacion(estadoActual.getFechaEntregaReal());
        response.setEstaEntregado(despacho.estaEntregado());

        return response;
    }
}
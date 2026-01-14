package ApiGymorEjecucion.Api.application.usecase.despacho;


import ApiGymorEjecucion.Api.application.dto.response.despacho.DespachoResponse;
import ApiGymorEjecucion.Api.domain.model.Despacho.Despacho;
import ApiGymorEjecucion.Api.domain.repository.DespachoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ConsultarDespachoUseCase {

    private final DespachoRepository despachoRepository;

    public ConsultarDespachoUseCase(DespachoRepository despachoRepository) {
        this.despachoRepository = despachoRepository;
    }

    /**
     * Buscar despacho por ID
     */
    public DespachoResponse buscarPorId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID del despacho es requerido");
        }

        Despacho despacho = despachoRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el despacho con ID: " + id
                ));

        return mapearAResponse(despacho);
    }

    /**
     * Buscar despacho por pedido
     */
    public DespachoResponse buscarPorPedido(String pedidoId) {
        if (pedidoId == null || pedidoId.isBlank()) {
            throw new IllegalArgumentException("El ID del pedido es requerido");
        }

        Despacho despacho = despachoRepository.buscarPorPedidoId(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró despacho para el pedido: " + pedidoId
                ));

        return mapearAResponse(despacho);
    }

    /**
     * Buscar despacho por número de guía
     */
    public DespachoResponse buscarPorGuia(String numeroGuia) {
        if (numeroGuia == null || numeroGuia.isBlank()) {
            throw new IllegalArgumentException("El número de guía es requerido");
        }

        Despacho despacho = despachoRepository.buscarPorGuia(numeroGuia)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró despacho con guía: " + numeroGuia
                ));

        return mapearAResponse(despacho);
    }

    private DespachoResponse mapearAResponse(Despacho despacho) {
        DespachoResponse response = new DespachoResponse();
        response.setId(despacho.getId());
        response.setPedidoId(despacho.getPedidoId());

        if (despacho.getGuiaDespacho() != null) {
            response.setNumeroGuia(despacho.getGuiaDespacho().getNumero());
            response.setUrlTracking(despacho.getGuiaDespacho().getUrlTracking());
            response.setFechaEmisionGuia(despacho.getGuiaDespacho().getFechaEmision());
        }

        if (despacho.getTransportista() != null) {
            response.setTransportista(despacho.getTransportista().getNombre());
            response.setCodigoTransportista(despacho.getTransportista().getCodigo());
            response.setTelefonoTransportista(despacho.getTransportista().getTelefono());
        }

        if (despacho.getDireccionEntrega() != null) {
            response.setDireccionCompleta(despacho.getDireccionEntrega().getDireccionCompleta());
        }

        response.setFechaDespacho(despacho.getFechaDespacho());
        response.setFechaEntregaEstimada(despacho.getFechaEntregaEstimada());
        response.setFechaEntregaReal(despacho.getFechaEntregaReal());
        response.setObservaciones(despacho.getObservaciones());
        response.setEstaDespachado(despacho.estaDespachado());
        response.setEstaEntregado(despacho.estaEntregado());

        return response;
    }
}
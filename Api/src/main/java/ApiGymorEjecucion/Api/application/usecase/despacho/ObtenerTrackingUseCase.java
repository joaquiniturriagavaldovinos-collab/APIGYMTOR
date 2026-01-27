package ApiGymorEjecucion.Api.application.usecase.despacho;

import ApiGymorEjecucion.Api.application.dto.response.despacho.TrackingResponse;
import ApiGymorEjecucion.Api.domain.model.Despacho.Despacho;
import ApiGymorEjecucion.Api.domain.model.Despacho.EstadoDespacho;
import ApiGymorEjecucion.Api.domain.repository.DespachoRepository;
import org.springframework.stereotype.Service;

@Service
public class ObtenerTrackingUseCase {

    private final DespachoRepository despachoRepository;

    public ObtenerTrackingUseCase(DespachoRepository despachoRepository) {
        this.despachoRepository = despachoRepository;
    }

    public TrackingResponse ejecutar(String pedidoId) {
        System.out.println("🔍 Buscando tracking para pedido: " + pedidoId);

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

        // ✅ Mapear desde el despacho real (NO desde API externa)
        TrackingResponse response = new TrackingResponse();

        response.setPedidoId(pedidoId);
        response.setNumeroGuia(despacho.getGuiaDespacho().getNumero());
        response.setUrlTracking(despacho.getGuiaDespacho().getUrlTracking());

        // ✅ CRÍTICO: Usar el estado REAL del despacho
        response.setEstadoActual(despacho.getEstado().name());
        response.setDescripcionEstado(obtenerDescripcionEstado(despacho.getEstado()));

        // ✅ Fecha de última actualización
        response.setFechaUltimaActualizacion(
                despacho.getFechaEntregaReal() != null
                        ? despacho.getFechaEntregaReal()
                        : despacho.getFechaDespacho()
        );

        response.setEstaEntregado(despacho.estaEntregado());

        // ✅ AGREGAR: Observaciones
        response.setObservaciones(despacho.getObservaciones());

        System.out.println("✅ Tracking obtenido:");
        System.out.println("   Estado: " + response.getEstadoActual());
        System.out.println("   Entregado: " + response.isEstaEntregado());
        System.out.println("   Observaciones: " + response.getObservaciones());

        return response;
    }

    private String obtenerDescripcionEstado(EstadoDespacho estado) {
        switch (estado) {
            case PENDIENTE:
                return "El pedido está siendo preparado";
            case EN_TRANSITO:
                return "El paquete está en camino";
            case ENTREGADO:
                return "El paquete ha sido entregado exitosamente";
            default:
                return "Estado desconocido";
        }
    }
}
package ApiGymorEjecucion.Api.application.mapper;


import ApiGymorEjecucion.Api.application.dto.response.pago.PagoResponse;
import ApiGymorEjecucion.Api.domain.model.Pago.Pago;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para conversión entre entidades de Pago y DTOs
 */
public class PagoMapper {

    /**
     * Convierte un Pago de dominio a PagoResponse
     */
    public static PagoResponse toResponse(Pago pago) {
        if (pago == null) return null;

        PagoResponse response = new PagoResponse();

        // Identificadores
        response.setId(pago.getId());
        response.setPedidoId(pago.getPedidoId());

        // Monto y método
        response.setMonto(pago.getMonto());
        response.setMetodoPago(pago.getMetodoPago().name());
        response.setMetodoPagoDescripcion(pago.getMetodoPago().getDescripcion());

        // Estado
        response.setEstado(pago.getEstado().name());
        response.setEstadoDescripcion(pago.getEstado().getDescripcion());
        response.setEsExitoso(pago.esExitoso());
        response.setEstaFinalizado(pago.estaFinalizado());

        // Referencias externas
        response.setReferenciaPasarela(pago.getReferenciaPasarela());
        response.setCodigoAutorizacion(pago.getCodigoAutorizacion());
        response.setMotivoRechazo(pago.getMotivoRechazo());

        // Fechas
        response.setFechaCreacion(pago.getFechaCreacion());
        response.setFechaProcesamiento(pago.getFechaProcesamiento());
        response.setFechaConfirmacion(pago.getFechaConfirmacion());

        return response;
    }

    /**
     * Convierte una lista de Pagos a lista de PagoResponse
     */
    public static List<PagoResponse> toResponseList(List<Pago> pagos) {
        if (pagos == null) return List.of();

        return pagos.stream()
                .map(PagoMapper::toResponse)
                .collect(Collectors.toList());
    }
}
package ApiGymorEjecucion.Api.application.usecase.pago;

import ApiGymorEjecucion.Api.application.dto.response.pago.PagoResponse;
import ApiGymorEjecucion.Api.domain.model.Pago.Pago;
import ApiGymorEjecucion.Api.domain.repository.PagoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsultarPagosPorPedidoUseCase {

    private final PagoRepository pagoRepository;

    public ConsultarPagosPorPedidoUseCase(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    public List<PagoResponse> ejecutar(String pedidoId) {
        // Validar
        if (pedidoId == null || pedidoId.isBlank()) {
            throw new IllegalArgumentException("El ID del pedido es requerido");
        }

        // Buscar todos los pagos del pedido
        List<Pago> pagos = pagoRepository.buscarPorPedidoId(pedidoId);

        // Mapear a response
        return pagos.stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    private PagoResponse mapearAResponse(Pago pago) {
        PagoResponse response = new PagoResponse();
        response.setId(pago.getId());
        response.setPedidoId(pago.getPedidoId());
        response.setMonto(pago.getMonto());
        response.setMetodoPago(pago.getMetodoPago().name());
        response.setMetodoPagoDescripcion(pago.getMetodoPago().getDescripcion());
        response.setEstado(pago.getEstado().name());
        response.setEstadoDescripcion(pago.getEstado().getDescripcion());
        response.setReferenciaPasarela(pago.getReferenciaPasarela());
        response.setCodigoAutorizacion(pago.getCodigoAutorizacion());
        response.setMotivoRechazo(pago.getMotivoRechazo());
        response.setFechaCreacion(pago.getFechaCreacion());
        response.setFechaProcesamiento(pago.getFechaProcesamiento());
        response.setFechaConfirmacion(pago.getFechaConfirmacion());
        response.setEsExitoso(pago.esExitoso());
        response.setEstaFinalizado(pago.estaFinalizado());
        return response;
    }


}
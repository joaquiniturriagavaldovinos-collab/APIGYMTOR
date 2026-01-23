package ApiGymorEjecucion.Api.application.usecase.pago;

import ApiGymorEjecucion.Api.application.dto.response.pago.PagoResponse;
import ApiGymorEjecucion.Api.domain.model.Pago.MetodoPago;
import ApiGymorEjecucion.Api.domain.model.Pago.Pago;
import ApiGymorEjecucion.Api.domain.repository.PagoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class IniciarPagoUseCase {

    private final PagoRepository pagoRepository;

    public IniciarPagoUseCase(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    public PagoResponse ejecutar(String pedidoId, BigDecimal monto, MetodoPago metodo) {
        // Crear pago en estado PROCESANDO
        Pago pago = Pago.crear(
                UUID.randomUUID().toString(),
                pedidoId,
                monto,
                metodo
        );
        pago.iniciarProcesamiento("referencia-inicial"); // opcional, según tu flujo

        // Guardar en DB
        Pago saved = pagoRepository.guardar(pago);

        // Retornar DTO
        return mapToResponse(saved);
    }

    private PagoResponse mapToResponse(Pago pago) {
        PagoResponse response = new PagoResponse();
        response.setId(pago.getId());
        response.setPedidoId(pago.getPedidoId());
        response.setMonto(pago.getMonto());
        response.setMetodoPago(pago.getMetodoPago().name());
        response.setEstado(pago.getEstado().name());
        response.setFechaCreacion(pago.getFechaCreacion());
        return response;
    }
}

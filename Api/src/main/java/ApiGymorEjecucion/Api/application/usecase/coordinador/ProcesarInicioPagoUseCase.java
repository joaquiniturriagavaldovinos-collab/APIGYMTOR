package ApiGymorEjecucion.Api.application.usecase.coordinador;

import ApiGymorEjecucion.Api.application.dto.response.pago.PagoResponse;
import ApiGymorEjecucion.Api.application.usecase.pago.IniciarPagoUseCase;
import ApiGymorEjecucion.Api.application.usecase.pedido.IniciarPagoPedidoUseCase;
import ApiGymorEjecucion.Api.domain.model.Pago.MetodoPago;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProcesarInicioPagoUseCase {

    private final IniciarPagoPedidoUseCase iniciarPagoPedido;
    private final IniciarPagoUseCase iniciarPagoPago;

    public ProcesarInicioPagoUseCase(
            IniciarPagoPedidoUseCase iniciarPagoPedido,
            IniciarPagoUseCase iniciarPagoPago
    ) {
        this.iniciarPagoPedido = iniciarPagoPedido;
        this.iniciarPagoPago = iniciarPagoPago;
    }

    public PagoResponse ejecutar(String pedidoId, BigDecimal monto, String metodoPagoStr) {
        iniciarPagoPedido.ejecutar(pedidoId);

        MetodoPago metodoPago = MetodoPago.fromString(metodoPagoStr);

        return iniciarPagoPago.ejecutar(pedidoId, monto, metodoPago);
    }
}

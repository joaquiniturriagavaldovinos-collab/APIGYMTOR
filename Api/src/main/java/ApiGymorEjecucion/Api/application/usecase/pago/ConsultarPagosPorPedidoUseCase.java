package ApiGymorEjecucion.Api.application.usecase.pago;

import ApiGymorEjecucion.Api.application.dto.response.pago.PagoResponse;
import ApiGymorEjecucion.Api.application.mapper.PagoMapper;
import ApiGymorEjecucion.Api.domain.model.Pago.Pago;
import ApiGymorEjecucion.Api.domain.repository.PagoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultarPagosPorPedidoUseCase {

    private final PagoRepository pagoRepository;

    public ConsultarPagosPorPedidoUseCase(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    public List<PagoResponse> ejecutar(String pedidoId) {
        List<Pago> pagos = pagoRepository.buscarPorPedidoId(pedidoId);


        pagos.forEach(pago -> {
            System.out.println("🔍 Pago ID: " + pago.getId());
            System.out.println("   Código Auth: " + pago.getCodigoAutorizacion());
        });

        return PagoMapper.toResponseList(pagos);
    }
}
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
        // Validar
        if (pedidoId == null || pedidoId.isBlank()) {
            throw new IllegalArgumentException("El ID del pedido es requerido");
        }

        // Buscar todos los pagos del pedido
        List<Pago> pagos = pagoRepository.buscarPorPedidoId(pedidoId);

        // ✅ Mapear usando PagoMapper
        return PagoMapper.toResponseList(pagos);
    }
}
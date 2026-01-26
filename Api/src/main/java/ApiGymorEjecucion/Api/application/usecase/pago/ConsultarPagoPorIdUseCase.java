package ApiGymorEjecucion.Api.application.usecase.pago;

import ApiGymorEjecucion.Api.application.dto.response.pago.PagoResponse;
import ApiGymorEjecucion.Api.application.mapper.PagoMapper;
import ApiGymorEjecucion.Api.domain.model.Pago.Pago;
import ApiGymorEjecucion.Api.domain.repository.PagoRepository;
import org.springframework.stereotype.Service;

@Service
public class ConsultarPagoPorIdUseCase {

    private final PagoRepository pagoRepository;

    public ConsultarPagoPorIdUseCase(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    public PagoResponse ejecutar(String pagoId) {
        if (pagoId == null || pagoId.isBlank()) {
            throw new IllegalArgumentException("El ID del pago es requerido");
        }

        Pago pago = pagoRepository.buscarPorId(pagoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el pago con ID: " + pagoId
                ));

        return PagoMapper.toResponse(pago);
    }
}
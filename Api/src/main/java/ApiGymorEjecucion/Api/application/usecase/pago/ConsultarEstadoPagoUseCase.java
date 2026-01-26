package ApiGymorEjecucion.Api.application.usecase.pago;

import ApiGymorEjecucion.Api.application.dto.response.pago.EstadoPagoSimpleResponse;
import ApiGymorEjecucion.Api.domain.model.Pago.EstadoPago;
import ApiGymorEjecucion.Api.domain.model.Pago.Pago;
import ApiGymorEjecucion.Api.domain.repository.PagoRepository;
import org.springframework.stereotype.Service;

/**
 * Retorna solo el estado del pago sin traer toda la información
 * Útil para polling desde el frontend
 */
@Service
public class ConsultarEstadoPagoUseCase {

    private final PagoRepository pagoRepository;

    public ConsultarEstadoPagoUseCase(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    public EstadoPagoSimpleResponse ejecutar(String pagoId) {
        Pago pago = pagoRepository.buscarPorId(pagoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el pago con ID: " + pagoId
                ));

        return new EstadoPagoSimpleResponse(
                pago.getId(),
                pago.getEstado().name(),
                pago.getEstado().getDescripcion(),
                pago.esExitoso(),
                pago.estaFinalizado()
        );
    }
}
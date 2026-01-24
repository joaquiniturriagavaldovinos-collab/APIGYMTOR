package ApiGymorEjecucion.Api.application.usecase.pago;

import ApiGymorEjecucion.Api.application.dto.request.pago.ReembolsoRequest;
import ApiGymorEjecucion.Api.application.dto.response.pago.PagoResponse;
import ApiGymorEjecucion.Api.application.mapper.PagoMapper;
import ApiGymorEjecucion.Api.domain.model.Pago.Pago;
import ApiGymorEjecucion.Api.domain.repository.PagoRepository;
import ApiGymorEjecucion.Api.infrastructure.external.payment.PagoGatewayClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ReembolsarPagoUseCase {

    private final PagoRepository pagoRepository;
    private final PagoGatewayClient pagoGatewayClient;

    public ReembolsarPagoUseCase(
            PagoRepository pagoRepository,
            PagoGatewayClient pagoGatewayClient) {
        this.pagoRepository = pagoRepository;
        this.pagoGatewayClient = pagoGatewayClient;
    }

    public PagoResponse ejecutar(String pagoId, ReembolsoRequest request) {

        // Validar
        validarRequest(request);

        // Buscar pago
        Pago pago = pagoRepository.buscarPorId(pagoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el pago con ID: " + pagoId
                ));

        // Validar que esté en estado EXITOSO
        if (!pago.esExitoso()) {
            throw new IllegalStateException(
                    "Solo se pueden reembolsar pagos exitosos. Estado actual: " + pago.getEstado()
            );
        }

        // Validar monto
        if (request.getMonto().compareTo(pago.getMonto()) > 0) {
            throw new IllegalArgumentException(
                    "El monto a reembolsar no puede ser mayor al monto del pago"
            );
        }

        // Integración con pasarela de pago
        PagoGatewayClient.ReembolsoResponse reembolsoResponse =
                pagoGatewayClient.procesarReembolso(
                        pago.getReferenciaPasarela(),
                        request.getMonto()
                );

        // DOMINIO: Marcar como reembolsado
        pago.reembolsar();

        // Persistir
        Pago actualizado = pagoRepository.guardar(pago);

        // ✅ Retornar usando PagoMapper
        return PagoMapper.toResponse(actualizado);
    }

    private void validarRequest(ReembolsoRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }
        if (request.getMonto() == null || request.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }
    }

}
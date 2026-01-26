package ApiGymorEjecucion.Api.application.usecase.pago;



import ApiGymorEjecucion.Api.application.dto.response.pago.PagoResponse;
import ApiGymorEjecucion.Api.application.mapper.PagoMapper;
import ApiGymorEjecucion.Api.domain.model.Pago.EstadoPago;
import ApiGymorEjecucion.Api.domain.model.Pago.MetodoPago;
import ApiGymorEjecucion.Api.domain.model.Pago.Pago;
import ApiGymorEjecucion.Api.domain.repository.PagoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Permite reintentar un pago que fue rechazado
 */
@Service
public class ReintentarPagoUseCase {

    private final PagoRepository pagoRepository;

    public ReintentarPagoUseCase(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    @Transactional
    public PagoResponse ejecutar(String pagoIdOriginal) {

        // 1. Buscar pago original
        Pago pagoOriginal = pagoRepository.buscarPorId(pagoIdOriginal)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el pago con ID: " + pagoIdOriginal
                ));

        // 2. Validar que sea un pago fallido
        if (pagoOriginal.getEstado() != EstadoPago.RECHAZADO) {
            throw new IllegalStateException(
                    "Solo se pueden reintentar pagos rechazados. Estado actual: "
                            + pagoOriginal.getEstado()
            );
        }

        // 3. Crear nuevo intento de pago
        Pago nuevoPago = Pago.crear(
                UUID.randomUUID().toString(),
                pagoOriginal.getPedidoId(),
                pagoOriginal.getMonto(),
                pagoOriginal.getMetodoPago()
        );

        // 4. Si requiere pasarela, iniciar procesamiento
        if (nuevoPago.requierePasarelaExterna()) {
            String referencia = generarReferenciaPasarela();
            nuevoPago.iniciarProcesamiento(referencia);
        }

        // 5. Guardar nuevo pago
        Pago pagoGuardado = pagoRepository.guardar(nuevoPago);

        return PagoMapper.toResponse(pagoGuardado);
    }

    private String generarReferenciaPasarela() {
        return "REF-RETRY-" + System.currentTimeMillis() + "-" +
                UUID.randomUUID().toString().substring(0, 8);
    }
}
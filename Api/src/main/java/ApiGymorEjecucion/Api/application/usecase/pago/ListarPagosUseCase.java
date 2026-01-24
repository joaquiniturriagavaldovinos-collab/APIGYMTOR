package ApiGymorEjecucion.Api.application.usecase.pago;

import ApiGymorEjecucion.Api.application.dto.response.pago.PagoResponse;
import ApiGymorEjecucion.Api.application.mapper.PagoMapper;
import ApiGymorEjecucion.Api.domain.model.Pago.EstadoPago;
import ApiGymorEjecucion.Api.domain.repository.PagoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarPagosUseCase {

    private final PagoRepository pagoRepository;

    public ListarPagosUseCase(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    /**
     * Lista todos los pagos
     */
    public List<PagoResponse> listarTodos() {
        return PagoMapper.toResponseList(pagoRepository.buscarTodos());
    }

    /**
     * Lista pagos por estado
     */
    public List<PagoResponse> listarPorEstado(String estado) {
        if (estado == null || estado.isBlank()) {
            throw new IllegalArgumentException("El estado es requerido");
        }

        EstadoPago estadoPago;
        try {
            estadoPago = EstadoPago.valueOf(estado);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado de pago inválido: " + estado);
        }

        return PagoMapper.toResponseList(
                pagoRepository.buscarPorEstado(estadoPago)
        );
    }

    /**
     * Lista solo pagos exitosos
     */
    public List<PagoResponse> listarExitosos() {
        return PagoMapper.toResponseList(
                pagoRepository.buscarPorEstado(EstadoPago.EXITOSO)
        );
    }

    /**
     * Lista pagos rechazados (para análisis)
     */
    public List<PagoResponse> listarRechazados() {
        return PagoMapper.toResponseList(
                pagoRepository.buscarPorEstado(EstadoPago.RECHAZADO)
        );
    }
}
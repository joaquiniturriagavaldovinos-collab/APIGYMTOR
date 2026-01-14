package ApiGymorEjecucion.Api.application.usecase.pago;

import ApiGymorEjecucion.Api.application.dto.response.pago.PagoResponse;
import ApiGymorEjecucion.Api.domain.model.Pago.EstadoPago;
import ApiGymorEjecucion.Api.domain.model.Pago.Pago;
import ApiGymorEjecucion.Api.domain.repository.PagoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        return pagoRepository.buscarTodos().stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
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

        return pagoRepository.buscarPorEstado(estadoPago).stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lista solo pagos exitosos
     */
    public List<PagoResponse> listarExitosos() {
        return pagoRepository.buscarPorEstado(EstadoPago.EXITOSO).stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lista pagos rechazados (para análisis)
     */
    public List<PagoResponse> listarRechazados() {
        return pagoRepository.buscarPorEstado(EstadoPago.RECHAZADO).stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    private PagoResponse mapearAResponse(Pago pago) {
        PagoResponse response =
                new PagoResponse();

        response.setId(pago.getId());
        response.setPedidoId(pago.getPedidoId());
        response.setMonto(pago.getMonto());
        response.setMetodoPago(pago.getMetodoPago().name());
        response.setEstado(pago.getEstado().name());
        response.setEstadoDescripcion(pago.getEstado().getDescripcion());
        response.setFechaCreacion(pago.getFechaCreacion());
        response.setEsExitoso(pago.esExitoso());

        return response;
    }
}

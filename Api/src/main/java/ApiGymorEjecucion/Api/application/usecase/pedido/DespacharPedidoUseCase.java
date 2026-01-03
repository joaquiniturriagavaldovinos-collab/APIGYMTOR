package ApiGymorEjecucion.Api.application.usecase.pedido;


import ApiGymorEjecucion.Api.application.dto.request.DespacharPedidoRequest;
import ApiGymorEjecucion.Api.application.dto.response.PedidoResponse;
import ApiGymorEjecucion.Api.application.mapper.PedidoMapper;
import ApiGymorEjecucion.Api.domain.exception.PedidoNoEncontradoException;
import ApiGymorEjecucion.Api.domain.model.pedido.Pedido;
import ApiGymorEjecucion.Api.domain.repository.PedidoRepository;
import org.springframework.stereotype.Service;

/**
 * CU5: Despachar Pedido
 *
 * Transición: PREPARING -> DISPATCHED
 * Genera guía de despacho y entrega al transportista.
 */
@Service
public class DespacharPedidoUseCase {

    private final PedidoRepository pedidoRepository;

    public DespacharPedidoUseCase(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    /**
     * Despacha un pedido preparado
     *
     * @param request Datos de despacho (guía, transportista)
     * @return Pedido actualizado en estado DISPATCHED
     */
    public PedidoResponse ejecutar(DespacharPedidoRequest request) {
        // Validar request
        validarRequest(request);

        // Buscar pedido
        Pedido pedido = pedidoRepository.buscarPorId(request.getPedidoId())
                .orElseThrow(() -> new PedidoNoEncontradoException(request.getPedidoId()));

        // Validar que pueda despacharse
        if (!pedido.puedeDespacharse()) {
            throw new IllegalStateException(
                    String.format("El pedido en estado %s no puede despacharse",
                            pedido.getEstado().getDescripcion())
            );
        }

        // El dominio valida la transición
        pedido.despachar(request.getGuiaDespacho());

        // Persistir cambio
        Pedido pedidoActualizado = pedidoRepository.guardar(pedido);

        // Aquí se integraría con:
        // - API del transportista (registrar envío)
        // - Sistema de notificaciones (avisar al cliente)
        // - Sistema de tracking

        return PedidoMapper.toResponse(pedidoActualizado);
    }

    private void validarRequest(DespacharPedidoRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }
        if (request.getPedidoId() == null || request.getPedidoId().isBlank()) {
            throw new IllegalArgumentException("El ID del pedido es requerido");
        }
        if (request.getGuiaDespacho() == null || request.getGuiaDespacho().isBlank()) {
            throw new IllegalArgumentException("La guía de despacho es requerida");
        }
    }
}
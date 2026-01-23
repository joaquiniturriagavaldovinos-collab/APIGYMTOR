package ApiGymorEjecucion.Api.application.usecase.pedido;

import ApiGymorEjecucion.Api.application.dto.request.pedido.DespacharPedidoRequest;
import ApiGymorEjecucion.Api.application.dto.response.pedido.PedidoResponse;
import ApiGymorEjecucion.Api.application.mapper.PedidoMapper;
import ApiGymorEjecucion.Api.domain.exception.PedidoNoEncontradoException;
import ApiGymorEjecucion.Api.domain.model.Despacho.*;
import ApiGymorEjecucion.Api.domain.model.pedido.Pedido;
import ApiGymorEjecucion.Api.domain.repository.DespachoRepository;
import ApiGymorEjecucion.Api.domain.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * CU5: Despachar Pedido
 *
 * Transición: PREPARING -> DISPATCHED
 * Crea el despacho y asigna guía/transportista
 */
@Service
public class DespacharPedidoUseCase {

    private final PedidoRepository pedidoRepository;
    private final DespachoRepository despachoRepository;

    public DespacharPedidoUseCase(
            PedidoRepository pedidoRepository,
            DespachoRepository despachoRepository
    ) {
        this.pedidoRepository = pedidoRepository;
        this.despachoRepository = despachoRepository;
    }

    @Transactional
    public PedidoResponse ejecutar(DespacharPedidoRequest request) {
        // Validar
        validarRequest(request);

        // Buscar pedido
        Pedido pedido = pedidoRepository.buscarPorId(request.getPedidoId())
                .orElseThrow(() -> new PedidoNoEncontradoException(request.getPedidoId()));

        // Crear guía de despacho
        GuiaDespacho guia = GuiaDespacho.crear(
                request.getNumeroGuia(),
                request.getUrlTracking()
        );

        // Despachar pedido (transición de estado)
        pedido.despachar(guia.getNumero());

        // Crear transportista
        Transportista transportista = Transportista.crear(
                request.getNombreTransportista(),
                request.getCodigoTransportista(),
                request.getTelefonoTransportista()
        );

        // Crear dirección de entrega
        DireccionEntrega direccion = new DireccionEntrega(
                request.getDireccionEntrega()
        );

        // Crear despacho
        Despacho despacho = Despacho.crear(
                "DESP-" + System.currentTimeMillis(),
                pedido.getId()
        );

        // Despachar con datos del transportista
        despacho.despachar(guia, transportista);

        // Guardar ambos
        pedidoRepository.guardar(pedido);
        despachoRepository.guardar(despacho);

        return PedidoMapper.toResponse(pedido);
    }

    private void validarRequest(DespacharPedidoRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }
        if (request.getPedidoId() == null || request.getPedidoId().isBlank()) {
            throw new IllegalArgumentException("El ID del pedido es requerido");
        }
        if (request.getNumeroGuia() == null || request.getNumeroGuia().isBlank()) {
            throw new IllegalArgumentException("El número de guía es requerido");
        }
        if (request.getNombreTransportista() == null || request.getNombreTransportista().isBlank()) {
            throw new IllegalArgumentException("El nombre del transportista es requerido");
        }
        if (request.getCodigoTransportista() == null || request.getCodigoTransportista().isBlank()) {
            throw new IllegalArgumentException("El código del transportista es requerido");
        }
        if (request.getDireccionEntrega() == null || request.getDireccionEntrega().isBlank()) {
            throw new IllegalArgumentException("La dirección de entrega es requerida");
        }
    }
}
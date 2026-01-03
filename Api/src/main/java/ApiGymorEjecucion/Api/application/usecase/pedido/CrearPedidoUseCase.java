package ApiGymorEjecucion.Api.application.usecase.pedido;



import ApiGymorEjecucion.Api.application.dto.request.CrearPedidoRequest;
import ApiGymorEjecucion.Api.application.dto.response.PedidoResponse;
import ApiGymorEjecucion.Api.application.mapper.PedidoMapper;
import ApiGymorEjecucion.Api.domain.model.pedido.ItemPedido;
import ApiGymorEjecucion.Api.domain.model.pedido.Pedido;
import ApiGymorEjecucion.Api.domain.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CU1: Crear Pedido
 *
 * Crea un pedido en estado CREATED sin validar pago.
 * Genera snapshot de precios al momento de creación.
 */
@Service
public class CrearPedidoUseCase {

    private final PedidoRepository pedidoRepository;

    public CrearPedidoUseCase(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    /**
     * Ejecuta la creación del pedido
     *
     * @param request Datos del pedido a crear
     * @return Pedido creado con estado CREATED
     */
    public PedidoResponse ejecutar(CrearPedidoRequest request) {
        // Validar request
        validarRequest(request);

        // Verificar que no exista pedido con ese ID
        if (pedidoRepository.existe(request.getPedidoId())) {
            throw new IllegalArgumentException(
                    "Ya existe un pedido con el ID: " + request.getPedidoId()
            );
        }

        // Convertir items del request a dominio
        List<ItemPedido> items = PedidoMapper.toItemsDomain(request.getItems());

        // Crear pedido en dominio (estado CREATED automático)
        Pedido pedido = Pedido.crear(
                request.getPedidoId(),
                request.getClienteId(),
                items
        );

        // Persistir
        Pedido pedidoGuardado = pedidoRepository.guardar(pedido);

        // Retornar response
        return PedidoMapper.toResponse(pedidoGuardado);
    }

    private void validarRequest(CrearPedidoRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }
        if (request.getPedidoId() == null || request.getPedidoId().isBlank()) {
            throw new IllegalArgumentException("El ID del pedido es requerido");
        }
        if (request.getClienteId() == null || request.getClienteId().isBlank()) {
            throw new IllegalArgumentException("El ID del cliente es requerido");
        }
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("El pedido debe tener al menos un item");
        }
    }
}
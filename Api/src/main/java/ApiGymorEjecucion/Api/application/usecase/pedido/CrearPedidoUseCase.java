package ApiGymorEjecucion.Api.application.usecase.pedido;



import ApiGymorEjecucion.Api.application.dto.request.pedido.CrearPedidoRequest;
import ApiGymorEjecucion.Api.application.dto.request.pedido.ItemPedidoRequest;
import ApiGymorEjecucion.Api.application.dto.response.pedido.PedidoResponse;
import ApiGymorEjecucion.Api.application.mapper.PedidoMapper;
import ApiGymorEjecucion.Api.domain.model.pedido.ItemPedido;
import ApiGymorEjecucion.Api.domain.model.pedido.Pedido;
import ApiGymorEjecucion.Api.domain.repository.PedidoRepository;
import ApiGymorEjecucion.Api.domain.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CU1: Crear Pedido
 *
 * Crea un pedido en estado CREATED sin validar pago.
 * Genera snapshot de precios al momento de creación.
 */
@Service
public class CrearPedidoUseCase {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;

    public CrearPedidoUseCase(PedidoRepository pedidoRepository,
                              ProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
    }


    /**
     * Ejecuta la creación del pedido
     *
     * @param request Datos del pedido a crear
     * @return Pedido creado con estado CREATED
     */
    public PedidoResponse ejecutar(CrearPedidoRequest request) {

        // 1. Generar ID si no viene
        String pedidoId = request.getPedidoId();
        if (pedidoId == null || pedidoId.isBlank()) {
            pedidoId = "PED-" + System.currentTimeMillis();
        }

        // 3. Verificar existencia
        if (pedidoRepository.existe(pedidoId)) {
            throw new IllegalArgumentException(
                    "Ya existe un pedido con el ID: " + pedidoId
            );
        }

        // 4. Mapear items a dominio
        List<ItemPedido> items = PedidoMapper.toItemsDomain(
                request.getItems(),
                productoRepository
        );

        // 5. Crear pedido (el dominio calcula el total)
        Pedido pedido = Pedido.crear(
                pedidoId,
                request.getClienteId(),
                items
        );

        // 6. Persistir
        Pedido pedidoGuardado = pedidoRepository.guardar(pedido);

        // 7. Retornar response
        return PedidoMapper.toResponse(pedidoGuardado);
    }


    private void validarRequest(CrearPedidoRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }

        // Solo valida lo que realmente es obligatorio
        if (request.getClienteId() == null || request.getClienteId().isBlank()) {
            throw new IllegalArgumentException("El ID del cliente es requerido");
        }

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("El pedido debe tener al menos un ítem");
        }

        // Validar cada ítem
        for (ItemPedidoRequest item : request.getItems()) {
            if (item.getProductoId() == null || item.getProductoId().isBlank()) {
                throw new IllegalArgumentException("El ID del producto es requerido");
            }
            if (item.getCantidad() <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
            }
            if (item.getPrecioUnitario() == null ||
                    item.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("El precio unitario debe ser mayor a 0");
            }
        }
    }
}
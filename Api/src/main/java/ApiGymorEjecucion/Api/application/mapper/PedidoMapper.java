package ApiGymorEjecucion.Api.application.mapper;



import ApiGymorEjecucion.Api.application.dto.request.pedido.ItemPedidoRequest;
import ApiGymorEjecucion.Api.application.dto.response.pedido.ItemPedidoResponse;
import ApiGymorEjecucion.Api.application.dto.response.pedido.PedidoResponse;
import ApiGymorEjecucion.Api.domain.model.pedido.ItemPedido;
import ApiGymorEjecucion.Api.domain.model.pedido.Pedido;
import ApiGymorEjecucion.Api.domain.model.pedido.TipoItem;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para conversión entre entidades de dominio y DTOs
 */
public class PedidoMapper {

    /**
     * Convierte una lista de ItemPedidoRequest a ItemPedido de dominio
     */
    public static List<ItemPedido> toItemsDomain(List<ItemPedidoRequest> itemsRequest) {
        if (itemsRequest == null) {
            throw new IllegalArgumentException("La lista de items no puede ser nula");
        }

        return itemsRequest.stream()
                .map(PedidoMapper::toItemDomain)
                .collect(Collectors.toList());
    }

    /**
     * Convierte un ItemPedidoRequest a ItemPedido de dominio
     */
    public static ItemPedido toItemDomain(ItemPedidoRequest request) {
        TipoItem tipo = TipoItem.valueOf(request.getTipo());

        return ItemPedido.crear(
                request.getProductoId(),
                request.getNombre(),
                tipo,
                request.getCantidad(),
                request.getPrecioUnitario()
        );
    }

    /**
     * Convierte un Pedido de dominio a PedidoResponse
     */
    public static PedidoResponse toResponse(Pedido pedido) {
        if (pedido == null) {
            return null;
        }

        PedidoResponse response = new PedidoResponse();
        response.setId(pedido.getId());
        response.setClienteId(pedido.getClienteId());
        response.setEstado(pedido.getEstado().name());
        response.setEstadoDescripcion(pedido.getEstado().getDescripcion());
        response.setTotal(pedido.calcularTotal());
        response.setFechaCreacion(pedido.getFechaCreacion());
        response.setFechaActualizacion(pedido.getFechaActualizacion());
        response.setReferenciaPago(pedido.getReferenciaPago());
        response.setGuiaDespacho(pedido.getGuiaDespacho());

        // Mapear items
        List<ItemPedidoResponse> itemsResponse = pedido.getItems().stream()
                .map(PedidoMapper::toItemResponse)
                .collect(Collectors.toList());
        response.setItems(itemsResponse);

        return response;
    }

    /**
     * Convierte un ItemPedido de dominio a ItemPedidoResponse
     */
    public static ItemPedidoResponse toItemResponse(ItemPedido item) {
        ItemPedidoResponse response = new ItemPedidoResponse();
        response.setProductoId(item.getProductoId());
        response.setNombre(item.getNombre());
        response.setTipo(item.getTipo().name());
        response.setCantidad(item.getCantidad());
        response.setPrecioUnitario(item.getPrecioUnitario());
        response.setSubtotal(item.getSubtotal());
        return response;
    }

    /**
     * Convierte una lista de Pedidos a lista de PedidoResponse
     */
    public static List<PedidoResponse> toResponseList(List<Pedido> pedidos) {
        if (pedidos == null) {
            return List.of();
        }

        return pedidos.stream()
                .map(PedidoMapper::toResponse)
                .collect(Collectors.toList());
    }
}
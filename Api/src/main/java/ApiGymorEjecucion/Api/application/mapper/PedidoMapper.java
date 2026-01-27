package ApiGymorEjecucion.Api.application.mapper;

import ApiGymorEjecucion.Api.application.dto.request.pedido.ItemPedidoRequest;
import ApiGymorEjecucion.Api.application.dto.response.pedido.ItemPedidoResponse;
import ApiGymorEjecucion.Api.application.dto.response.pedido.PedidoResponse;
import ApiGymorEjecucion.Api.domain.model.pedido.ItemPedido;
import ApiGymorEjecucion.Api.domain.model.pedido.Pedido;
import ApiGymorEjecucion.Api.domain.model.pedido.TipoItem;
import ApiGymorEjecucion.Api.domain.model.producto.Producto;
import ApiGymorEjecucion.Api.domain.repository.ProductoRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para conversión entre entidades de dominio y DTOs
 */
public class PedidoMapper {

    /**
     * Convierte una lista de ItemPedidoRequest a ItemPedido de dominio
     * usando ProductoRepository para obtener datos completos de cada producto.
     */
    public static List<ItemPedido> toItemsDomain(List<ItemPedidoRequest> itemsRequest, ProductoRepository productoRepository) {
        if (itemsRequest == null || itemsRequest.isEmpty()) {
            throw new IllegalArgumentException("La lista de items no puede ser nula o vacía");
        }

        return itemsRequest.stream()
                .map(item -> toItemDomain(item, productoRepository))
                .collect(Collectors.toList());
    }

    /**
     * Convierte un ItemPedidoRequest a ItemPedido de dominio
     */
    public static ItemPedido toItemDomain(ItemPedidoRequest request, ProductoRepository productoRepository) {
        if (request == null) {
            throw new IllegalArgumentException("ItemPedidoRequest no puede ser nulo");
        }

        // Traer producto desde BD
        Producto producto = productoRepository.obtenerPorId(request.getProductoId());
        if (producto == null) {
            throw new IllegalArgumentException("Producto no encontrado: " + request.getProductoId());
        }

        // Mapear a dominio usando datos del producto
        return ItemPedido.crear(
                producto.getId(),
                producto.getNombre(),
                TipoItem.valueOf(producto.getTipo().name()), // convierte TipoProducto a TipoItem
                request.getCantidad(),
                producto.getPrecio()
        );
    }

    /**
     * Convierte un Pedido de dominio a PedidoResponse
     */
    public static PedidoResponse toResponse(Pedido pedido) {
        if (pedido == null) return null;

        PedidoResponse response = new PedidoResponse();

        // Identificadores
        response.setId(pedido.getId());
        response.setClienteId(pedido.getClienteId());

        // Estado
        response.setEstado(pedido.getEstado().name());
        response.setEstadoDescripcion(pedido.getEstado().getDescripcion());
        response.setEstaPagado(pedido.estaPagado()); //
        response.setEsFinal(pedido.esFinal());

        // Items
        List<ItemPedidoResponse> itemsResponse = pedido.getItems().stream()
                .map(PedidoMapper::toItemResponse)
                .toList();
        response.setItems(itemsResponse);
        response.setCantidadItems(pedido.getItems().size());

        // Montos
        response.setTotal(pedido.calcularTotal());

        // Fechas
        response.setFechaCreacion(pedido.getFechaCreacion());
        response.setFechaActualizacion(pedido.getFechaActualizacion());

        // Referencias
        response.setReferenciaPago(pedido.getReferenciaPago());
        response.setGuiaDespacho(pedido.getGuiaDespacho());

        response.setDespachoId(pedido.getDespachoId());  // ← AGREGAR


        return response;
    }
    /**
     * Convierte un ItemPedido de dominio a ItemPedidoResponse
     */
    public static ItemPedidoResponse toItemResponse(ItemPedido item) {
        if (item == null) return null;

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
        if (pedidos == null) return List.of();

        return pedidos.stream()
                .map(PedidoMapper::toResponse)
                .collect(Collectors.toList());
    }
}

package ApiGymorEjecucion.Api.application.usecase.dashboard;

import ApiGymorEjecucion.Api.application.dto.response.dashboard.ProductoTopResponse;
import ApiGymorEjecucion.Api.domain.model.pedido.EstadoPedido;
import ApiGymorEjecucion.Api.domain.model.pedido.ItemPedido;
import ApiGymorEjecucion.Api.domain.model.pedido.Pedido;
import ApiGymorEjecucion.Api.domain.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ObtenerTopProductosUseCase {

    private final PedidoRepository pedidoRepository;

    public ObtenerTopProductosUseCase(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<ProductoTopResponse> ejecutar(int limit) {
        // Validar
        if (limit <= 0) {
            limit = 10;
        }

        // Obtener pedidos completados
        List<Pedido> pedidosCompletados = pedidoRepository.buscarPorEstado(EstadoPedido.DELIVERED);

        // Agrupar por producto
        Map<String, ProductoStats> statsMap = new HashMap<>();

        for (Pedido pedido : pedidosCompletados) {
            for (ItemPedido item : pedido.getItems()) {
                ProductoStats stats = statsMap.getOrDefault(
                        item.getProductoId(),
                        new ProductoStats(item.getProductoId(), item.getNombre())
                );

                stats.incrementarCantidad(item.getCantidad());
                stats.sumarVentas(item.getSubtotal());

                statsMap.put(item.getProductoId(), stats);
            }
        }

        // Ordenar por cantidad vendida y tomar top N
        final int finalLimit = limit;
        return statsMap.values().stream()
                .sorted((a, b) -> Integer.compare(b.cantidadVendida, a.cantidadVendida))
                .limit(finalLimit)
                .map(stats -> {
                    ProductoTopResponse response = new ProductoTopResponse();
                    response.setProductoId(stats.productoId);
                    response.setNombre(stats.nombre);
                    response.setCantidadVendida(stats.cantidadVendida);
                    response.setTotalVentas(stats.totalVentas);
                    return response;
                })
                .collect(Collectors.toList());
    }

    // Clase auxiliar interna
    private static class ProductoStats {
        String productoId;
        String nombre;
        int cantidadVendida;
        BigDecimal totalVentas;

        ProductoStats(String productoId, String nombre) {
            this.productoId = productoId;
            this.nombre = nombre;
            this.cantidadVendida = 0;
            this.totalVentas = BigDecimal.ZERO;
        }

        void incrementarCantidad(int cantidad) {
            this.cantidadVendida += cantidad;
        }

        void sumarVentas(BigDecimal monto) {
            this.totalVentas = this.totalVentas.add(monto);
        }
    }
}

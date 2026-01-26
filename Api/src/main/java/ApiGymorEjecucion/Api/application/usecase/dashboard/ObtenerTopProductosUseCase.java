package ApiGymorEjecucion.Api.application.usecase.dashboard;

import ApiGymorEjecucion.Api.application.dto.response.dashboard.ProductoTopResponse;
import ApiGymorEjecucion.Api.domain.model.pedido.EstadoPedido;
import ApiGymorEjecucion.Api.domain.model.pedido.ItemPedido;
import ApiGymorEjecucion.Api.domain.model.pedido.Pedido;
import ApiGymorEjecucion.Api.domain.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ObtenerTopProductosUseCase {

    private final PedidoRepository pedidoRepository;

    public ObtenerTopProductosUseCase(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductoTopResponse> ejecutar(int limite) {

        // Validar límite
        if (limite <= 0 || limite > 100) {
            limite = 10; // Default
        }

        // Obtener todos los pedidos pagados
        List<Pedido> pedidosPagados = pedidoRepository.buscarTodos().stream()
                .filter(Pedido::estaPagado)
                .toList();

        // Agrupar productos por ID y contar ventas
        Map<String, ProductoStats> statsMap = new HashMap<>();

        for (Pedido pedido : pedidosPagados) {
            for (ItemPedido item : pedido.getItems()) {
                String productoId = item.getProductoId();

                statsMap.computeIfAbsent(productoId, k -> new ProductoStats(
                        productoId,
                        item.getNombre()
                ));

                ProductoStats stats = statsMap.get(productoId);
                stats.agregarVenta(item.getCantidad(), item.getSubtotal());
            }
        }

        // Convertir a lista y ordenar por cantidad vendida
        List<ProductoTopResponse> topProductos = statsMap.values().stream()
                .sorted((a, b) -> Integer.compare(b.cantidadVendida, a.cantidadVendida))
                .limit(limite)
                .map(stats -> {
                    ProductoTopResponse response = new ProductoTopResponse();
                    response.setProductoId(stats.productoId);
                    response.setNombre(stats.nombre);
                    response.setCantidadVendida(stats.cantidadVendida);
                    response.setTotalVentas(stats.totalVentas);
                    return response;
                })
                .toList();

        return topProductos;
    }

    // Clase interna para acumular estadísticas
    private static class ProductoStats {
        String productoId;
        String nombre;
        int cantidadVendida = 0;
        java.math.BigDecimal totalVentas = java.math.BigDecimal.ZERO;

        ProductoStats(String productoId, String nombre) {
            this.productoId = productoId;
            this.nombre = nombre;
        }

        void agregarVenta(int cantidad, java.math.BigDecimal monto) {
            this.cantidadVendida += cantidad;
            this.totalVentas = this.totalVentas.add(monto);
        }
    }
}
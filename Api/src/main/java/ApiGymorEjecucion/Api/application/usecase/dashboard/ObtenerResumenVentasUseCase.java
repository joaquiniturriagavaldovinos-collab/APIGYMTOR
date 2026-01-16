package ApiGymorEjecucion.Api.application.usecase.dashboard;

import ApiGymorEjecucion.Api.domain.model.pedido.EstadoPedido;
import ApiGymorEjecucion.Api.domain.model.pedido.Pedido;
import ApiGymorEjecucion.Api.domain.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Query: Obtener Resumen de Ventas
 *
 * NOTA: Este NO es un caso de uso de dominio puro.
 * Es una proyección/query para reportes.
 */
@Service
public class ObtenerResumenVentasUseCase {

    private final PedidoRepository pedidoRepository;

    public ObtenerResumenVentasUseCase(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public ResumenVentasResponse ejecutar(LocalDate desde, LocalDate hasta) {
        // Validar
        if (desde == null) {
            desde = LocalDate.now().minusMonths(1);
        }
        if (hasta == null) {
            hasta = LocalDate.now();
        }

        LocalDateTime fechaDesde = desde.atStartOfDay();
        LocalDateTime fechaHasta = hasta.atTime(23, 59, 59);

        // Obtener todos los pedidos
        List<Pedido> todosPedidos = pedidoRepository.buscarTodos();

        // Filtrar por rango de fechas
        List<Pedido> pedidosEnRango = todosPedidos.stream()
                .filter(p -> p.getFechaCreacion().isAfter(fechaDesde) &&
                        p.getFechaCreacion().isBefore(fechaHasta))
                .collect(Collectors.toList());

        // Calcular métricas
        long totalPedidos = pedidosEnRango.size();

        long pedidosCompletados = pedidosEnRango.stream()
                .filter(p -> p.getEstado() == EstadoPedido.DELIVERED)
                .count();

        long pedidosPendientes = pedidosEnRango.stream()
                .filter(p -> !p.estaFinalizado())
                .count();

        long pedidosCancelados = pedidosEnRango.stream()
                .filter(p -> p.getEstado() == EstadoPedido.CANCELLED)
                .count();

        BigDecimal ventasTotales = pedidosEnRango.stream()
                .filter(p -> p.getEstado() == EstadoPedido.DELIVERED)
                .map(Pedido::calcularTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Ventas del día actual
        LocalDate hoy = LocalDate.now();
        BigDecimal ventasDelDia = todosPedidos.stream()
                .filter(p -> p.getFechaCreacion().toLocalDate().equals(hoy))
                .filter(p -> p.getEstado() == EstadoPedido.DELIVERED)
                .map(Pedido::calcularTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Ticket promedio
        BigDecimal ticketPromedio = pedidosCompletados > 0
                ? ventasTotales.divide(BigDecimal.valueOf(pedidosCompletados), 2, java.math.RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        // Construir response
        ResumenVentasResponse response = new ResumenVentasResponse();
        response.setFechaDesde(desde);
        response.setFechaHasta(hasta);
        response.setTotalPedidos(totalPedidos);
        response.setPedidosCompletados(pedidosCompletados);
        response.setPedidosPendientes(pedidosPendientes);
        response.setPedidosCancelados(pedidosCancelados);
        response.setVentasTotales(ventasTotales);
        response.setVentasDelDia(ventasDelDia);
        response.setTicketPromedio(ticketPromedio);

        return response;
    }

    // DTO
    public static class ResumenVentasResponse {
        private LocalDate fechaDesde;
        private LocalDate fechaHasta;
        private long totalPedidos;
        private long pedidosCompletados;
        private long pedidosPendientes;
        private long pedidosCancelados;
        private BigDecimal ventasTotales;
        private BigDecimal ventasDelDia;
        private BigDecimal ticketPromedio;

        // Getters y Setters
        public LocalDate getFechaDesde() { return fechaDesde; }
        public void setFechaDesde(LocalDate fechaDesde) { this.fechaDesde = fechaDesde; }

        public LocalDate getFechaHasta() { return fechaHasta; }
        public void setFechaHasta(LocalDate fechaHasta) { this.fechaHasta = fechaHasta; }

        public long getTotalPedidos() { return totalPedidos; }
        public void setTotalPedidos(long totalPedidos) { this.totalPedidos = totalPedidos; }

        public long getPedidosCompletados() { return pedidosCompletados; }
        public void setPedidosCompletados(long pedidosCompletados) {
            this.pedidosCompletados = pedidosCompletados;
        }

        public long getPedidosPendientes() { return pedidosPendientes; }
        public void setPedidosPendientes(long pedidosPendientes) {
            this.pedidosPendientes = pedidosPendientes;
        }

        public long getPedidosCancelados() { return pedidosCancelados; }
        public void setPedidosCancelados(long pedidosCancelados) {
            this.pedidosCancelados = pedidosCancelados;
        }

        public BigDecimal getVentasTotales() { return ventasTotales; }
        public void setVentasTotales(BigDecimal ventasTotales) { this.ventasTotales = ventasTotales; }

        public BigDecimal getVentasDelDia() { return ventasDelDia; }
        public void setVentasDelDia(BigDecimal ventasDelDia) { this.ventasDelDia = ventasDelDia; }

        public BigDecimal getTicketPromedio() { return ticketPromedio; }
        public void setTicketPromedio(BigDecimal ticketPromedio) { this.ticketPromedio = ticketPromedio; }
    }
}
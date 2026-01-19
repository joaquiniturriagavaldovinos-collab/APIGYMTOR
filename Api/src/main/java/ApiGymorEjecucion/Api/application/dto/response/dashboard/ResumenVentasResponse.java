package ApiGymorEjecucion.Api.application.dto.response.dashboard;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ResumenVentasResponse {
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
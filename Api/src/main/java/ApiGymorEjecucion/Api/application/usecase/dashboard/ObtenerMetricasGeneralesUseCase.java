package ApiGymorEjecucion.Api.application.usecase.dashboard;

import ApiGymorEjecucion.Api.application.dto.response.dashboard.MetricasGeneralesResponse;
import ApiGymorEjecucion.Api.domain.model.Pago.EstadoPago;
import ApiGymorEjecucion.Api.domain.model.pedido.EstadoPedido;
import ApiGymorEjecucion.Api.domain.repository.ClienteRepository;
import ApiGymorEjecucion.Api.domain.repository.PagoRepository;
import ApiGymorEjecucion.Api.domain.repository.PedidoRepository;
import ApiGymorEjecucion.Api.domain.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ObtenerMetricasGeneralesUseCase {

    private final PedidoRepository pedidoRepository;
    private final PagoRepository pagoRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;

    public ObtenerMetricasGeneralesUseCase(
            PedidoRepository pedidoRepository,
            PagoRepository pagoRepository,
            ClienteRepository clienteRepository,
            ProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.pagoRepository = pagoRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
    }

    @Transactional(readOnly = true)
    public MetricasGeneralesResponse ejecutar() {

        // Contar totales
        long totalPedidos = pedidoRepository.contar();
        long totalClientes = clienteRepository.contar();
        long totalProductos = productoRepository.contar();

        // Pedidos por estado
        long pedidosPendientes = pedidoRepository.buscarPorEstado(EstadoPedido.PAYMENT_PENDING).size();
        long pedidosEnProceso = pedidoRepository.buscarPorEstado(EstadoPedido.PREPARING).size()
                + pedidoRepository.buscarPorEstado(EstadoPedido.DISPATCHED).size();
        long pedidosEntregados = pedidoRepository.buscarPorEstado(EstadoPedido.DELIVERED).size();

        // Calcular ingresos totales (solo pagos exitosos)
        BigDecimal ingresosTotales = pagoRepository.buscarPorEstado(EstadoPago.EXITOSO)
                .stream()
                .map(pago -> pago.getMonto())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Construir response
        MetricasGeneralesResponse response = new MetricasGeneralesResponse();
        response.setTotalPedidos(totalPedidos);
        response.setTotalClientes(totalClientes);
        response.setTotalProductos(totalProductos);
        response.setPedidosPendientes(pedidosPendientes);
        response.setPedidosEnProceso(pedidosEnProceso);
        response.setPedidosEntregados(pedidosEntregados);
        response.setIngresosTotales(ingresosTotales);

        return response;
    }
}
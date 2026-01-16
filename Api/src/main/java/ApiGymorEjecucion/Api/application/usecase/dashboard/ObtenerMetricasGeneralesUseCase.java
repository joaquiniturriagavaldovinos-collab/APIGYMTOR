package ApiGymorEjecucion.Api.application.usecase.dashboard;


import ApiGymorEjecucion.Api.application.dto.response.dashboard.MetricasGeneralesResponse;
import ApiGymorEjecucion.Api.domain.model.pedido.EstadoPedido;
import ApiGymorEjecucion.Api.domain.repository.ClienteRepository;
import ApiGymorEjecucion.Api.domain.repository.PedidoRepository;
import ApiGymorEjecucion.Api.domain.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ObtenerMetricasGeneralesUseCase {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;

    public ObtenerMetricasGeneralesUseCase(PedidoRepository pedidoRepository,
                                           ClienteRepository clienteRepository,
                                           ProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
    }

    public MetricasGeneralesResponse ejecutar() {
        MetricasGeneralesResponse response = new MetricasGeneralesResponse();

        // Pedidos
        response.setTotalPedidos(pedidoRepository.contar());
        response.setPedidosCompletados(pedidoRepository.buscarPorEstado(EstadoPedido.DELIVERED).size());
        response.setPedidosPendientes(
                pedidoRepository.buscarPorEstado(EstadoPedido.PAID).size() +
                        pedidoRepository.buscarPorEstado(EstadoPedido.PREPARING).size() +
                        pedidoRepository.buscarPorEstado(EstadoPedido.DISPATCHED).size()
        );

        // Clientes
        response.setTotalClientes(clienteRepository.contar());
        response.setClientesActivos(clienteRepository.buscarActivos().size());

        // Clientes nuevos del mes
        LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
        long clientesNuevos = clienteRepository.buscarActivos().stream()
                .filter(c -> c.getFechaRegistro().toLocalDate().isAfter(inicioMes))
                .count();
        response.setClientesNuevosMes(clientesNuevos);

        // Productos
        response.setTotalProductos(productoRepository.contar());
        response.setProductosConStock(productoRepository.buscarConStock().size());

        // Productos con bajo stock (menos de 10 unidades)
        long productosBajoStock = productoRepository.buscarTodos().stream()
                .filter(p -> p.getStockDisponible() > 0 && p.getStockDisponible() < 10)
                .count();
        response.setProductosBajoStock(productosBajoStock);

        return response;
    }
}
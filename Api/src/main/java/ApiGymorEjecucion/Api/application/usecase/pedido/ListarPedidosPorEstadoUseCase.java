package ApiGymorEjecucion.Api.application.usecase.pedido;



import ApiGymorEjecucion.Api.application.dto.response.pedido.PedidoResponse;
import ApiGymorEjecucion.Api.application.mapper.PedidoMapper;
import ApiGymorEjecucion.Api.domain.model.pedido.EstadoPedido;
import ApiGymorEjecucion.Api.domain.model.pedido.Pedido;
import ApiGymorEjecucion.Api.domain.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListarPedidosPorEstadoUseCase {

    private final PedidoRepository pedidoRepository;

    public ListarPedidosPorEstadoUseCase(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> ejecutar(String estadoStr) {
        if (estadoStr == null || estadoStr.isBlank()) {
            throw new IllegalArgumentException("El estado es requerido");
        }

        EstadoPedido estado;
        try {
            estado = EstadoPedido.valueOf(estadoStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Estado inválido: " + estadoStr +
                            ". Estados válidos: " + java.util.Arrays.toString(EstadoPedido.values())
            );
        }

        List<Pedido> pedidos = pedidoRepository.buscarPorEstado(estado);

        return pedidos.stream()
                .map(PedidoMapper::toResponse)
                .toList();
    }
}
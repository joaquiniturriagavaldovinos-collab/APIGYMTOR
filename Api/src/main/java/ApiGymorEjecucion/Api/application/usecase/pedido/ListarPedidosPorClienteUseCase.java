package ApiGymorEjecucion.Api.application.usecase.pedido;


import ApiGymorEjecucion.Api.application.dto.response.pedido.PedidoResponse;
import ApiGymorEjecucion.Api.application.mapper.PedidoMapper;
import ApiGymorEjecucion.Api.domain.model.pedido.Pedido;
import ApiGymorEjecucion.Api.domain.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListarPedidosPorClienteUseCase {

    private final PedidoRepository pedidoRepository;

    public ListarPedidosPorClienteUseCase(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }
    @Transactional(readOnly = true)
    public List<PedidoResponse> ejecutar(String clienteId) {
        if (clienteId == null || clienteId.isBlank()) {
            throw new IllegalArgumentException("El ID del cliente es requerido");
        }

        List<Pedido> pedidos = pedidoRepository.buscarPorCliente(clienteId);

        return pedidos.stream()
                .map(PedidoMapper::toResponse)
                .toList();
    }
}
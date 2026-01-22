package ApiGymorEjecucion.Api.application.usecase.cliente;


import ApiGymorEjecucion.Api.domain.model.Cliente.Cliente;
import ApiGymorEjecucion.Api.domain.repository.ClienteRepository;
import org.springframework.stereotype.Service;

@Service
public class ActivarClienteUseCase {

    private final ClienteRepository clienteRepository;

    public ActivarClienteUseCase(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public void ejecutar(String id) {

        Cliente cliente = clienteRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el cliente con ID: " + id
                ));

        if (cliente.isActivo()) {
            return; // idempotente
        }

        cliente.activar();
        clienteRepository.guardar(cliente);
    }
}

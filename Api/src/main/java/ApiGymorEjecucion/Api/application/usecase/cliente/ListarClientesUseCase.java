package ApiGymorEjecucion.Api.application.usecase.cliente;



import ApiGymorEjecucion.Api.domain.model.Cliente.Cliente;
import ApiGymorEjecucion.Api.domain.model.Cliente.TipoCliente;
import ApiGymorEjecucion.Api.domain.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListarClientesUseCase {

    private final ClienteRepository clienteRepository;

    public ListarClientesUseCase(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    /**
     * Lista todos los clientes activos
     */
    public List<ClienteListResponse> listarTodos() {
        return clienteRepository.buscarActivos().stream()
                .map(this::mapearAListResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lista clientes por tipo (MINORISTA/MAYORISTA)
     */
    public List<ClienteListResponse> listarPorTipo(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("El tipo es requerido");
        }

        TipoCliente tipoCliente;
        try {
            tipoCliente = TipoCliente.valueOf(tipo);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de cliente inválido: " + tipo);
        }

        return clienteRepository.buscarPorTipo(tipoCliente).stream()
                .filter(Cliente::isActivo)
                .map(this::mapearAListResponse)
                .collect(Collectors.toList());
    }

    /**
     * Buscar clientes por texto (nombre, email, rut)
     */
    public List<ClienteListResponse> buscarPorTexto(String texto) {
        if (texto == null || texto.isBlank()) {
            return listarTodos();
        }

        String textoBusqueda = texto.toLowerCase();

        return clienteRepository.buscarActivos().stream()
                .filter(cliente ->
                        cliente.getNombre().toLowerCase().contains(textoBusqueda) ||
                                cliente.getApellido().toLowerCase().contains(textoBusqueda) ||
                                cliente.getEmail().toLowerCase().contains(textoBusqueda) ||
                                cliente.getRut().contains(textoBusqueda)
                )
                .map(this::mapearAListResponse)
                .collect(Collectors.toList());
    }

    private ClienteListResponse mapearAListResponse(Cliente cliente) {
        ClienteListResponse response = new ClienteListResponse();
        response.setId(cliente.getId());
        response.setNombreCompleto(cliente.getNombreCompleto());
        response.setEmail(cliente.getEmail());
        response.setRut(cliente.getRut());
        response.setTipo(cliente.getTipo().name());
        response.setTipoDescripcion(cliente.getTipo().getDescripcion());
        response.setActivo(cliente.isActivo());
        return response;
    }

    // DTO simplificado para listados

}
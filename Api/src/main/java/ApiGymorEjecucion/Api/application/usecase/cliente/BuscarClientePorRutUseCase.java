package ApiGymorEjecucion.Api.application.usecase.cliente;


import ApiGymorEjecucion.Api.application.dto.response.cliente.ClienteResponse;
import ApiGymorEjecucion.Api.domain.model.Cliente.Cliente;
import ApiGymorEjecucion.Api.domain.repository.ClienteRepository;
import org.springframework.stereotype.Service;

@Service
public class BuscarClientePorRutUseCase {

    private final ClienteRepository clienteRepository;

    public BuscarClientePorRutUseCase(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public ClienteResponse ejecutar(String rut) {
        // Validar
        if (rut == null || rut.isBlank()) {
            throw new IllegalArgumentException("El RUT es requerido");
        }

        // Buscar
        Cliente cliente = clienteRepository.buscarPorRut(rut)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el cliente con RUT: " + rut
                ));

        // Reutilizar mapper del caso de uso anterior
        return mapearAResponse(cliente);
    }

    private ClienteResponse mapearAResponse(Cliente cliente) {
        ClienteResponse response =
                new ClienteResponse();

        response.setId(cliente.getId());
        response.setNombre(cliente.getNombre());
        response.setApellido(cliente.getApellido());
        response.setEmail(cliente.getEmail());
        response.setTelefono(cliente.getTelefono());
        response.setRut(cliente.getRut());
        response.setTipo(cliente.getTipo().name());
        response.setTipoDescripcion(cliente.getTipo().getDescripcion());
        response.setActivo(cliente.isActivo());
        response.setFechaRegistro(cliente.getFechaRegistro());

        if (cliente.getDireccionPrincipal() != null) {
            response.setDireccionPrincipal(
                    cliente.getDireccionPrincipal().getDireccionCompleta()
            );
        }

        response.setCantidadDirecciones(cliente.getDirecciones().size());

        return response;
    }
}
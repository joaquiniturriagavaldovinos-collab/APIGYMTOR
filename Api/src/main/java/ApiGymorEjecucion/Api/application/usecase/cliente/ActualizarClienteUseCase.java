package ApiGymorEjecucion.Api.application.usecase.cliente;



import ApiGymorEjecucion.Api.application.dto.request.cliente.ActualizarClienteRequest;
import ApiGymorEjecucion.Api.application.dto.response.cliente.ClienteResponse;
import ApiGymorEjecucion.Api.domain.model.Cliente.Cliente;
import ApiGymorEjecucion.Api.domain.repository.ClienteRepository;
import org.springframework.stereotype.Service;

@Service
public class ActualizarClienteUseCase {

    private final ClienteRepository clienteRepository;

    public ActualizarClienteUseCase(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public ClienteResponse ejecutar(String id, ActualizarClienteRequest request) {

        validarRequest(request);

        Cliente cliente = clienteRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el cliente con ID: " + id
                ));

        cliente.actualizarDatosPersonales(
                request.getNombre(),
                request.getApellido(),
                request.getTelefono()
        );

        Cliente actualizado = clienteRepository.guardar(cliente);

        return mapearAResponse(actualizado);
    }


    private void validarRequest(ActualizarClienteRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }
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
        response.setActivo(cliente.isActivo());

        return response;
    }


}
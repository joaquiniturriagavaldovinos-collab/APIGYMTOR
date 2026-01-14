package ApiGymorEjecucion.Api.application.usecase.cliente;


import ApiGymorEjecucion.Api.application.dto.request.cliente.DireccionRequest;
import ApiGymorEjecucion.Api.application.dto.response.cliente.ClienteResponse;
import ApiGymorEjecucion.Api.domain.model.Cliente.Cliente;
import ApiGymorEjecucion.Api.domain.model.Cliente.DireccionEntrega;
import ApiGymorEjecucion.Api.domain.repository.ClienteRepository;
import org.springframework.stereotype.Service;

@Service
public class AgregarDireccionClienteUseCase {

    private final ClienteRepository clienteRepository;

    public AgregarDireccionClienteUseCase(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public ClienteResponse ejecutar(
            String clienteId, DireccionRequest request) {

        // Validar
        validarRequest(request);

        // Buscar cliente
        Cliente cliente = clienteRepository.buscarPorId(clienteId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el cliente con ID: " + clienteId
                ));

        // Crear dirección (Value Object)
        DireccionEntrega direccion = DireccionEntrega.crear(
                request.getCalle(),
                request.getNumero(),
                request.getComuna(),
                request.getCiudad(),
                request.getRegion(),
                request.getCodigoPostal(),
                request.getReferencia()
        );

        // DOMINIO: Agregar dirección
        cliente.agregarDireccion(direccion);

        // Si es marcada como principal
        if (request.isEsPrincipal()) {
            cliente.establecerDireccionPrincipal(direccion);
        }

        // Persistir
        Cliente actualizado = clienteRepository.guardar(cliente);

        // Retornar
        return mapearAResponse(actualizado);
    }

    private void validarRequest(DireccionRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La dirección no puede ser nula");
        }
        // DireccionEntrega.crear() valida los campos requeridos
    }

    private ClienteResponse mapearAResponse(Cliente cliente) {
        ClienteResponse response =
                new ClienteResponse();

        response.setId(cliente.getId());
        response.setNombreCompleto(cliente.getNombreCompleto());
        response.setEmail(cliente.getEmail());
        response.setRut(cliente.getRut());
        response.setCantidadDirecciones(cliente.getDirecciones().size());

        if (cliente.getDireccionPrincipal() != null) {
            response.setDireccionPrincipal(
                    cliente.getDireccionPrincipal().getDireccionCompleta()
            );
        }

        return response;
    }
}
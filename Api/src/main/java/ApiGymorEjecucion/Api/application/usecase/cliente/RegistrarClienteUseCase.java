package ApiGymorEjecucion.Api.application.usecase.cliente;

import ApiGymorEjecucion.Api.application.dto.request.cliente.DireccionRequest;
import ApiGymorEjecucion.Api.application.dto.request.cliente.RegistrarClienteRequest;
import ApiGymorEjecucion.Api.application.dto.response.cliente.ClienteResponse;
import ApiGymorEjecucion.Api.domain.model.Cliente.Cliente;
import ApiGymorEjecucion.Api.domain.model.Cliente.DireccionEntrega;
import ApiGymorEjecucion.Api.domain.model.Cliente.TipoCliente;
import ApiGymorEjecucion.Api.domain.repository.ClienteRepository;
import org.springframework.stereotype.Service;

/**
 * Caso de Uso: Registrar Cliente
 *
 * Registra un nuevo cliente en el sistema (minorista o mayorista)
 */
@Service
public class RegistrarClienteUseCase {

    private final ClienteRepository clienteRepository;

    public RegistrarClienteUseCase(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    /**
     * Registra un nuevo cliente
     *
     * @param request Datos del cliente a registrar
     * @return Cliente registrado
     */
    public ClienteResponse ejecutar(RegistrarClienteRequest request) {



        // Validar request
        validarRequest(request);

        // Verificar que no exista cliente con ese RUT
        if (clienteRepository.existePorRut(request.getRut())) {
            throw new IllegalArgumentException(
                    "Ya existe un cliente registrado con el RUT: " + request.getRut()
            );
        }

        // Verificar que no exista cliente con ese email
        if (clienteRepository.existePorEmail(request.getEmail())) {
            throw new IllegalArgumentException(
                    "Ya existe un cliente registrado con el email: " + request.getEmail()
            );
        }

        // Crear cliente en dominio
        TipoCliente tipo = TipoCliente.valueOf(request.getTipo());
        
        Cliente cliente = Cliente.crear(
                null,
                request.getNombre(),
                request.getApellido(),
                request.getEmail(),
                request.getTelefono(),
                request.getRut(),
                tipo
        );

        // Agregar dirección si viene en el request
        if (request.getDireccion() != null) {
            DireccionEntrega direccion =
                    crearDireccionDesdeRequest(request.getDireccion());
            cliente.agregarDireccion(direccion);
        }


        // Persistir
        Cliente clienteGuardado = clienteRepository.guardar(cliente);

        // Retornar response
        return mapearAResponse(clienteGuardado);
    }

    private void validarRequest(RegistrarClienteRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }
        if (request.getNombre() == null || request.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es requerido");
        }
        if (request.getApellido() == null || request.getApellido().isBlank()) {
            throw new IllegalArgumentException("El apellido es requerido");
        }



        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("El email es requerido");
        }
        if (request.getRut() == null || request.getRut().isBlank()) {
            throw new IllegalArgumentException("El RUT es requerido");
        }
        if (request.getTipo() == null || request.getTipo().isBlank()) {
            throw new IllegalArgumentException("El tipo de cliente es requerido");
        }
        // Validar formato de email básico
        if (!request.getEmail().contains("@")) {
            throw new IllegalArgumentException("El email no tiene un formato válido");
        }
    }


    private DireccionEntrega crearDireccionDesdeRequest(DireccionRequest request) {
        return DireccionEntrega.crear(
                request.getCalle(),
                request.getNumero(),
                request.getComuna(),
                request.getCiudad(),
                request.getRegion(),
                request.getCodigoPostal(),
                request.getReferencia()
        );
    }


    private ClienteResponse mapearAResponse(Cliente cliente) {
        ClienteResponse response = new ClienteResponse();
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

        return response;
    }


}
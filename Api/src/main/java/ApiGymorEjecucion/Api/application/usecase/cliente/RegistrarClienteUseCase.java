package ApiGymorEjecucion.Api.application.usecase.cliente;

import ApiGymorEjecucion.Api.application.dto.request.cliente.DireccionRequest;
import ApiGymorEjecucion.Api.application.dto.request.cliente.RegistrarClienteRequest;
import ApiGymorEjecucion.Api.application.dto.response.cliente.ClienteResponse;
import ApiGymorEjecucion.Api.domain.model.Cliente.Cliente;
import ApiGymorEjecucion.Api.domain.model.Cliente.DireccionEntrega;
import ApiGymorEjecucion.Api.domain.model.Cliente.TipoCliente;
import ApiGymorEjecucion.Api.domain.model.usuario.Usuario;
import ApiGymorEjecucion.Api.domain.model.usuario.Rol;
import ApiGymorEjecucion.Api.domain.repository.ClienteRepository;
import ApiGymorEjecucion.Api.domain.repository.UsuarioRepository;
+import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegistrarClienteUseCase {

    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
+    private final BCryptPasswordEncoder passwordEncoder;

    public RegistrarClienteUseCase(
            ClienteRepository clienteRepository,
            UsuarioRepository usuarioRepository,
+    ) {
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
+        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public ClienteResponse ejecutar(RegistrarClienteRequest request) {

        validarRequest(request);

        if (clienteRepository.existePorRut(request.getRut())) {
            throw new IllegalArgumentException(
                    "Ya existe un cliente registrado con el RUT: " + request.getRut()
            );
        }

        if (clienteRepository.existePorEmail(request.getEmail())) {
            throw new IllegalArgumentException(
                    "Ya existe un cliente registrado con el email: " + request.getEmail()
            );
        }

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

        if (request.getDireccion() != null) {
            DireccionEntrega direccion =
                    crearDireccionDesdeRequest(request.getDireccion());
            cliente.agregarDireccion(direccion);
        }

        // Guardar cliente
        Cliente clienteGuardado = clienteRepository.guardar(cliente);

Usuario usuario = Usuario.crear(
        null,
        request.getEmail(),
        request.getNombre(),
        request.getApellido(),
        passwordEncoder.encode(request.getPassword())
);

Rol rolCliente = Rol.crear(
        java.util.UUID.randomUUID().toString(),
        "CLIENTE",
        "Rol para clientes del sistema"
);

usuario.agregarRol(rolCliente);

usuarioRepository.guardar(usuario);

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
        if (!request.getEmail().contains("@")) {
            throw new IllegalArgumentException("El email no tiene un formato válido");
        }
        if (request.getRut() == null || request.getRut().isBlank()) {
            throw new IllegalArgumentException("El RUT es requerido");
        }
        if (request.getTipo() == null || request.getTipo().isBlank()) {
            throw new IllegalArgumentException("El tipo de cliente es requerido");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("La contraseña es requerida");
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
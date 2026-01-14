package ApiGymorEjecucion.Api.presentation.controller;


import ApiGymorEjecucion.Api.application.dto.request.cliente.RegistrarClienteRequest;
import ApiGymorEjecucion.Api.application.dto.response.cliente.ClienteResponse;
import ApiGymorEjecucion.Api.application.usecase.cliente.RegistrarClienteUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST para operaciones de Clientes
 */
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final RegistrarClienteUseCase registrarClienteUseCase;

    public ClienteController(RegistrarClienteUseCase registrarClienteUseCase) {
        this.registrarClienteUseCase = registrarClienteUseCase;
    }

    /**
     * Registrar un nuevo cliente
     * POST /api/clientes
     */
    @PostMapping
    public ResponseEntity<ClienteResponse> registrarCliente(
            @RequestBody RegistrarClienteRequest request) {

        ClienteResponse response = registrarClienteUseCase.ejecutar(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Buscar cliente por ID
     * GET /api/clientes/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarPorId(@PathVariable String id) {
        // TODO: Implementar ConsultarClienteUseCase
        return ResponseEntity.ok().build();
    }

    /**
     * Buscar cliente por RUT
     * GET /api/clientes/rut/{rut}
     */
    @GetMapping("/rut/{rut}")
    public ResponseEntity<ClienteResponse> buscarPorRut(@PathVariable String rut) {
        // TODO: Implementar BuscarClientePorRutUseCase
        return ResponseEntity.ok().build();
    }

    /**
     * Listar todos los clientes
     * GET /api/clientes
     */
    @GetMapping
    public ResponseEntity<?> listarClientes(
            @RequestParam(required = false) String tipo) {

        // TODO: Implementar ListarClientesUseCase
        // Si tipo != null, filtrar por tipo (MINORISTA/MAYORISTA)
        return ResponseEntity.ok().build();
    }

    /**
     * Actualizar información de cliente
     * PUT /api/clientes/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> actualizarCliente(
            @PathVariable String id,
            @RequestBody ActualizarClienteRequest request) {

        // TODO: Implementar ActualizarClienteUseCase
        return ResponseEntity.ok().build();
    }

    /**
     * Agregar dirección a cliente
     * POST /api/clientes/{id}/direcciones
     */
    @PostMapping("/{id}/direcciones")
    public ResponseEntity<ClienteResponse> agregarDireccion(
            @PathVariable String id,
            @RequestBody DireccionRequest direccion) {

        // TODO: Implementar AgregarDireccionUseCase
        return ResponseEntity.ok().build();
    }

    /**
     * Desactivar cliente
     * DELETE /api/clientes/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarCliente(@PathVariable String id) {
        // TODO: Implementar DesactivarClienteUseCase
        // Soft delete - marca como inactivo
        return ResponseEntity.noContent().build();
    }

    // DTOs internos
    public static class ActualizarClienteRequest {
        private String nombre;
        private String apellido;
        private String telefono;
        // getters/setters
    }

    public static class DireccionRequest {
        private String calle;
        private String numero;
        private String comuna;
        private String ciudad;
        private String region;
        private String codigoPostal;
        private String referencia;
        // getters/setters
    }
}
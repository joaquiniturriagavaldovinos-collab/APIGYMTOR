package ApiGymorEjecucion.Api.presentation.controller;

import ApiGymorEjecucion.Api.application.dto.request.cliente.DireccionRequest;
import ApiGymorEjecucion.Api.application.dto.request.cliente.RegistrarClienteRequest;
import ApiGymorEjecucion.Api.application.dto.response.cliente.ClienteResponse;
import ApiGymorEjecucion.Api.application.usecase.cliente.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import ApiGymorEjecucion.Api.application.usecase.cliente.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para gestión de Clientes
 *
 * Endpoints:
 * - POST   /api/v1/clientes                    → Registrar cliente
 * - GET    /api/v1/clientes                    → Listar clientes activos
 * - GET    /api/v1/clientes/{id}               → Buscar por ID
 * - GET    /api/v1/clientes/rut/{rut}          → Buscar por RUT
 * - GET    /api/v1/clientes/buscar?texto=...   → Buscar por texto
 * - GET    /api/v1/clientes/tipo/{tipo}        → Listar por tipo
 * - PUT    /api/v1/clientes/{id}               → Actualizar cliente
 * - POST   /api/v1/clientes/{id}/direcciones   → Agregar dirección
 * - DELETE /api/v1/clientes/{id}               → Desactivar cliente (soft delete)
 */
@RestController
@RequestMapping("/api/v1/clientes")
@CrossOrigin(origins = "*") // En producción, especifica dominios permitidos
public class ClienteController {

    private final RegistrarClienteUseCase registrarClienteUseCase;
    private final BuscarClientePorIdUseCase buscarClientePorIdUseCase;
    private final BuscarClientePorRutUseCase buscarClientePorRutUseCase;
    private final ListarClientesUseCase listarClientesUseCase;
    private final ActualizarClienteUseCase actualizarClienteUseCase;
    private final AgregarDireccionClienteUseCase agregarDireccionClienteUseCase;
    private final DesactivarClienteUseCase desactivarClienteUseCase;

    public ClienteController(
            RegistrarClienteUseCase registrarClienteUseCase,
            BuscarClientePorIdUseCase buscarClientePorIdUseCase,
            BuscarClientePorRutUseCase buscarClientePorRutUseCase,
            ListarClientesUseCase listarClientesUseCase,
            ActualizarClienteUseCase actualizarClienteUseCase,
            AgregarDireccionClienteUseCase agregarDireccionClienteUseCase,
            DesactivarClienteUseCase desactivarClienteUseCase) {

        this.registrarClienteUseCase = registrarClienteUseCase;
        this.buscarClientePorIdUseCase = buscarClientePorIdUseCase;
        this.buscarClientePorRutUseCase = buscarClientePorRutUseCase;
        this.listarClientesUseCase = listarClientesUseCase;
        this.actualizarClienteUseCase = actualizarClienteUseCase;
        this.agregarDireccionClienteUseCase = agregarDireccionClienteUseCase;
        this.desactivarClienteUseCase = desactivarClienteUseCase;
    }

    /**
     * POST /api/v1/clientes
     * Registra un nuevo cliente en el sistema
     *
     * @param request Datos del cliente
     * @return Cliente registrado (201 CREATED)
     */
    @PostMapping
    public ResponseEntity<ClienteResponse> registrarCliente(
            @Valid @RequestBody RegistrarClienteRequest request) {

        ClienteResponse response = registrarClienteUseCase.ejecutar(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * GET /api/v1/clientes/{id}
     * Busca un cliente por su ID
     *
     * @param id ID del cliente
     * @return Cliente encontrado (200 OK)
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarPorId(
            @PathVariable String id) {

        ClienteResponse response = buscarClientePorIdUseCase.ejecutar(id);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/clientes/rut/{rut}
     * Busca un cliente por su RUT
     *
     * @param rut RUT del cliente (sin puntos ni guión)
     * @return Cliente encontrado (200 OK)
     */
    @GetMapping("/rut/{rut}")
    public ResponseEntity<ClienteResponse> buscarPorRut(
            @PathVariable String rut) {

        ClienteResponse response = buscarClientePorRutUseCase.ejecutar(rut);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/clientes
     * Lista todos los clientes activos
     *
     * @return Lista de clientes (200 OK)
     */
    @GetMapping
    public ResponseEntity<List<ListarClientesUseCase.ClienteListResponse>> listarClientes() {
        List<ListarClientesUseCase.ClienteListResponse> clientes =
                listarClientesUseCase.listarTodos();
        return ResponseEntity.ok(clientes);
    }

    /**
     * GET /api/v1/clientes/buscar?texto={texto}
     * Busca clientes por texto (nombre, apellido, email, rut)
     *
     * @param texto Texto a buscar
     * @return Lista de clientes que coinciden (200 OK)
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<ListarClientesUseCase.ClienteListResponse>> buscarPorTexto(
            @RequestParam(required = false) String texto) {

        List<ListarClientesUseCase.ClienteListResponse> clientes =
                listarClientesUseCase.buscarPorTexto(texto);
        return ResponseEntity.ok(clientes);
    }

    /**
     * GET /api/v1/clientes/tipo/{tipo}
     * Lista clientes por tipo (MINORISTA o MAYORISTA)
     *
     * @param tipo Tipo de cliente
     * @return Lista de clientes del tipo especificado (200 OK)
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<ListarClientesUseCase.ClienteListResponse>> listarPorTipo(
            @PathVariable String tipo) {

        List<ListarClientesUseCase.ClienteListResponse> clientes =
                listarClientesUseCase.listarPorTipo(tipo);
        return ResponseEntity.ok(clientes);
    }

    /**
     * PUT /api/v1/clientes/{id}
     * Actualiza la información de un cliente
     *
     * @param id ID del cliente
     * @param request Datos a actualizar
     * @return Cliente actualizado (200 OK)
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> actualizarCliente(
            @PathVariable String id,
            @Valid @RequestBody ActualizarClienteUseCase.ActualizarClienteRequest request) {

        ClienteResponse response = actualizarClienteUseCase.ejecutar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/v1/clientes/{id}/direcciones
     * Agrega una nueva dirección al cliente
     *
     * @param id ID del cliente
     * @param request Datos de la dirección
     * @return Cliente actualizado con la nueva dirección (201 CREATED)
     */
    @PostMapping("/{id}/direcciones")
    public ResponseEntity<ClienteResponse> agregarDireccion(
            @PathVariable String id,
            @Valid @RequestBody DireccionRequest request) {

        ClienteResponse response =
                agregarDireccionClienteUseCase.ejecutar(id, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * DELETE /api/v1/clientes/{id}
     * Desactiva un cliente (soft delete)
     *
     * No elimina el registro de la BD, solo lo marca como inactivo
     * para mantener integridad referencial con pedidos históricos.
     *
     * @param id ID del cliente
     * @return 204 NO CONTENT
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarCliente(@PathVariable String id) {
        desactivarClienteUseCase.ejecutar(id);
        return ResponseEntity.noContent().build();
    }
}
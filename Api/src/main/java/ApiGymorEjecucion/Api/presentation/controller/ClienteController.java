package ApiGymorEjecucion.Api.presentation.controller;

import ApiGymorEjecucion.Api.application.dto.request.cliente.*;
import ApiGymorEjecucion.Api.application.dto.response.cliente.ClienteResponse;
import ApiGymorEjecucion.Api.application.usecase.cliente.*;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(
        name = "Clientes",
        description = "Operaciones relacionadas con la gestión de clientes (registro, consulta, actualización y desactivación)"
)
@RestController
@RequestMapping("/api/v1/clientes")
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

    // -------------------------------------------------
    // REGISTRAR CLIENTE
    // -------------------------------------------------
    @Operation(
            summary = "Registrar cliente",
            description = "Registra un nuevo cliente en el sistema"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Cliente registrado exitosamente",
                    content = @Content(schema = @Schema(implementation = ClienteResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ClienteResponse> registrarCliente(
            @Valid @RequestBody RegistrarClienteRequest request) {

        ClienteResponse response = registrarClienteUseCase.ejecutar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // -------------------------------------------------
    // BUSCAR POR ID
    // -------------------------------------------------
    @Operation(
            summary = "Buscar cliente por ID",
            description = "Obtiene un cliente a partir de su identificador único"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                    content = @Content(schema = @Schema(implementation = ClienteResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarPorId(
            @Parameter(description = "ID del cliente", example = "c123")
            @PathVariable String id) {

        ClienteResponse response = buscarClientePorIdUseCase.ejecutar(id);
        return ResponseEntity.ok(response);
    }

    // -------------------------------------------------
    // BUSCAR POR RUT
    // -------------------------------------------------
    @Operation(
            summary = "Buscar cliente por RUT",
            description = "Obtiene un cliente utilizando su RUT (sin puntos ni guión)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                    content = @Content(schema = @Schema(implementation = ClienteResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content)
    })
    @GetMapping("/rut/{rut}")
    public ResponseEntity<ClienteResponse> buscarPorRut(
            @Parameter(description = "RUT del cliente", example = "12345678K")
            @PathVariable String rut) {

        ClienteResponse response = buscarClientePorRutUseCase.ejecutar(rut);
        return ResponseEntity.ok(response);
    }

    // -------------------------------------------------
    // LISTAR CLIENTES
    // -------------------------------------------------
    @Operation(
            summary = "Listar clientes",
            description = "Obtiene todos los clientes activos"
    )
    @ApiResponse(responseCode = "200", description = "Listado de clientes")
    @GetMapping
    public ResponseEntity<List<ClienteListResponse>> listarClientes() {

        List<ClienteListResponse> clientes = listarClientesUseCase.listarTodos();
        return ResponseEntity.ok(clientes);
    }

    // -------------------------------------------------
    // BUSCAR POR TEXTO
    // -------------------------------------------------
    @Operation(
            summary = "Buscar clientes por texto",
            description = "Busca clientes por nombre, apellido, email o RUT"
    )
    @ApiResponse(responseCode = "200", description = "Resultados de búsqueda")
    @GetMapping("/buscar")
    public ResponseEntity<List<ClienteListResponse>> buscarPorTexto(
            @Parameter(description = "Texto a buscar", example = "juan")
            @RequestParam(required = false) String texto) {

        List<ClienteListResponse> clientes =
                listarClientesUseCase.buscarPorTexto(texto);
        return ResponseEntity.ok(clientes);
    }

    // -------------------------------------------------
    // LISTAR POR TIPO
    // -------------------------------------------------
    @Operation(
            summary = "Listar clientes por tipo",
            description = "Obtiene clientes filtrados por tipo (MINORISTA / MAYORISTA)"
    )
    @ApiResponse(responseCode = "200", description = "Listado de clientes por tipo")
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<ClienteListResponse>> listarPorTipo(
            @Parameter(description = "Tipo de cliente", example = "MINORISTA")
            @PathVariable String tipo) {

        List<ClienteListResponse> clientes =
                listarClientesUseCase.listarPorTipo(tipo);
        return ResponseEntity.ok(clientes);
    }

    // -------------------------------------------------
    // ACTUALIZAR CLIENTE
    // -------------------------------------------------
    @Operation(
            summary = "Actualizar cliente",
            description = "Actualiza los datos de un cliente existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente actualizado",
                    content = @Content(schema = @Schema(implementation = ClienteResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> actualizarCliente(
            @Parameter(description = "ID del cliente", example = "c123")
            @PathVariable String id,
            @Valid @RequestBody ActualizarClienteRequest request) {

        ClienteResponse response = actualizarClienteUseCase.ejecutar(id, request);
        return ResponseEntity.ok(response);
    }

    // -------------------------------------------------
    // AGREGAR DIRECCIÓN
    // -------------------------------------------------
    @Operation(
            summary = "Agregar dirección a cliente",
            description = "Agrega una nueva dirección asociada a un cliente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Dirección agregada",
                    content = @Content(schema = @Schema(implementation = ClienteResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content)
    })
    @PostMapping("/{id}/direcciones")
    public ResponseEntity<ClienteResponse> agregarDireccion(
            @Parameter(description = "ID del cliente", example = "c123")
            @PathVariable String id,
            @Valid @RequestBody DireccionRequest request) {

        ClienteResponse response =
                agregarDireccionClienteUseCase.ejecutar(id, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // -------------------------------------------------
    // DESACTIVAR CLIENTE
    // -------------------------------------------------
    @Operation(
            summary = "Desactivar cliente",
            description = "Desactiva un cliente (soft delete, no elimina físicamente)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cliente desactivado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarCliente(
            @Parameter(description = "ID del cliente", example = "c123")
            @PathVariable String id) {

        desactivarClienteUseCase.ejecutar(id);
        return ResponseEntity.noContent().build();
    }
}

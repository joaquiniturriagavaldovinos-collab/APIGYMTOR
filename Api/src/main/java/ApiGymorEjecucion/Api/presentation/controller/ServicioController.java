package ApiGymorEjecucion.Api.presentation.controller;

import ApiGymorEjecucion.Api.application.dto.request.servicio.ActualizarServicioRequest;
import ApiGymorEjecucion.Api.application.dto.request.servicio.CrearServicioRequest;
import ApiGymorEjecucion.Api.application.dto.response.servicio.ServicioResponse;
import ApiGymorEjecucion.Api.application.usecase.servicio.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para operaciones de Servicios (Clases de entrenamiento)
 */
@Tag(
        name = "Servicios",
        description = "Gestión de servicios y clases de entrenamiento"
)
@RestController
@RequestMapping("/api/servicios")
public class ServicioController {

    private final CrearServicioUseCase crearServicioUseCase;
    private final ActualizarServicioUseCase actualizarServicioUseCase;
    private final DesactivarServicioUseCase desactivarServicioUseCase;
    private final ListarServiciosUseCase listarServiciosUseCase;

    public ServicioController(
            CrearServicioUseCase crearServicioUseCase,
            ActualizarServicioUseCase actualizarServicioUseCase,
            DesactivarServicioUseCase desactivarServicioUseCase,
            ListarServiciosUseCase listarServiciosUseCase
    ) {
        this.crearServicioUseCase = crearServicioUseCase;
        this.actualizarServicioUseCase = actualizarServicioUseCase;
        this.desactivarServicioUseCase = desactivarServicioUseCase;
        this.listarServiciosUseCase = listarServiciosUseCase;
    }

    // -------------------------------------------------
    // CREAR SERVICIO
    // -------------------------------------------------
    @Operation(
            summary = "Crear servicio",
            description = "Crea un nuevo servicio o clase de entrenamiento"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Servicio creado correctamente",
                    content = @Content(schema = @Schema(implementation = ServicioResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ServicioResponse> crear(
            @RequestBody CrearServicioRequest request
    ) {
        ServicioResponse response = crearServicioUseCase.ejecutar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // -------------------------------------------------
    // ACTUALIZAR PRECIO
    // -------------------------------------------------
    @Operation(
            summary = "Actualizar precio del servicio",
            description = "Actualiza el precio de un servicio existente"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Precio actualizado correctamente",
                    content = @Content(schema = @Schema(implementation = ServicioResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Servicio no encontrado", content = @Content)
    })
    @PutMapping("/{id}/precio")
    public ResponseEntity<ServicioResponse> actualizarPrecio(
            @Parameter(description = "ID del servicio", example = "serv_001")
            @PathVariable String id,
            @RequestBody ActualizarServicioRequest request
    ) {
        return ResponseEntity.ok(
                actualizarServicioUseCase.ejecutar(id, request)
        );
    }

    // -------------------------------------------------
    // DESACTIVAR SERVICIO
    // -------------------------------------------------
    @Operation(
            summary = "Desactivar servicio",
            description = "Desactiva un servicio (soft delete)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Servicio desactivado correctamente"),
            @ApiResponse(responseCode = "404", description = "Servicio no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(
            @Parameter(description = "ID del servicio", example = "serv_001")
            @PathVariable String id
    ) {
        desactivarServicioUseCase.ejecutar(id);
        return ResponseEntity.noContent().build();
    }

    // -------------------------------------------------
    // LISTAR SERVICIOS
    // -------------------------------------------------
    @Operation(
            summary = "Listar servicios",
            description = "Obtiene todos los servicios activos"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado de servicios",
                    content = @Content(schema = @Schema(implementation = ServicioResponse.class))
            )
    })
    @GetMapping
    public ResponseEntity<List<ServicioResponse>> listarTodos() {
        return ResponseEntity.ok(
                listarServiciosUseCase.listarTodos()
        );
    }

    // -------------------------------------------------
    // LISTAR POR MODALIDAD
    // -------------------------------------------------
    @Operation(
            summary = "Listar servicios por modalidad",
            description = "Obtiene servicios filtrados por modalidad (PRESENCIAL / ONLINE)"
    )
    @GetMapping("/modalidad/{modalidad}")
    public ResponseEntity<List<ServicioResponse>> listarPorModalidad(
            @Parameter(description = "Modalidad del servicio", example = "PRESENCIAL")
            @PathVariable String modalidad
    ) {
        return ResponseEntity.ok(
                listarServiciosUseCase.listarPorModalidad(modalidad)
        );
    }
}

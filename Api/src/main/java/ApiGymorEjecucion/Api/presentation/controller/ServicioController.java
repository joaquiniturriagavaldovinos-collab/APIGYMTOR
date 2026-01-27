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
    private final ActivarServicioUseCase activarServicioUseCase;  // ← NUEVO
    private final EliminarServicioUseCase eliminarServicioUseCase;  // ← NUEVO
    private final ListarServiciosUseCase listarServiciosUseCase;

    public ServicioController(
            CrearServicioUseCase crearServicioUseCase,
            ActualizarServicioUseCase actualizarServicioUseCase,
            DesactivarServicioUseCase desactivarServicioUseCase,
            ActivarServicioUseCase activarServicioUseCase,  // ← NUEVO
            EliminarServicioUseCase eliminarServicioUseCase,  // ← NUEVO
            ListarServiciosUseCase listarServiciosUseCase
    ) {
        this.crearServicioUseCase = crearServicioUseCase;
        this.actualizarServicioUseCase = actualizarServicioUseCase;
        this.desactivarServicioUseCase = desactivarServicioUseCase;
        this.activarServicioUseCase = activarServicioUseCase;  // ← NUEVO
        this.eliminarServicioUseCase = eliminarServicioUseCase;  // ← NUEVO
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
// ACTUALIZAR SERVICIO (General - no solo precio)
// -------------------------------------------------
    @Operation(
            summary = "Actualizar servicio",
            description = "Actualiza uno o varios campos de un servicio existente (actualización parcial)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Servicio actualizado correctamente",
                    content = @Content(schema = @Schema(implementation = ServicioResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Servicio no encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    @PutMapping("/{id}")  // ← Cambiar de "/{id}/precio" a "/{id}"
    public ResponseEntity<ServicioResponse> actualizar(
            @Parameter(description = "ID del servicio", example = "SRV-001")
            @PathVariable String id,
            @RequestBody ActualizarServicioRequest request
    ) {
        return ResponseEntity.ok(
                actualizarServicioUseCase.ejecutar(id, request)
        );
    }
    // -------------------------------------------------
    // DESACTIVAR SERVICIO (Soft Delete)
    // -------------------------------------------------
    @Operation(
            summary = "Desactivar servicio",
            description = "Desactiva un servicio (soft delete) - El servicio se mantiene en BD pero no está disponible"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Servicio desactivado correctamente"),
            @ApiResponse(responseCode = "404", description = "Servicio no encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "El servicio ya está desactivado", content = @Content)
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
    // ACTIVAR SERVICIO
    // -------------------------------------------------
    @Operation(
            summary = "Activar servicio",
            description = "Reactiva un servicio previamente desactivado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Servicio activado correctamente"),
            @ApiResponse(responseCode = "404", description = "Servicio no encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "El servicio ya está activo", content = @Content)
    })
    @PatchMapping("/{id}/activar")
    public ResponseEntity<Void> activar(
            @Parameter(description = "ID del servicio", example = "serv_001")
            @PathVariable String id
    ) {
        activarServicioUseCase.ejecutar(id);
        return ResponseEntity.noContent().build();
    }

    // -------------------------------------------------
    // ELIMINAR SERVICIO PERMANENTEMENTE (Hard Delete)
    // -------------------------------------------------
    @Operation(
            summary = "Eliminar servicio permanentemente",
            description = "⚠️ PELIGROSO: Elimina el servicio físicamente de la base de datos. Esta acción NO se puede deshacer. Solo para uso administrativo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Servicio eliminado permanentemente"),
            @ApiResponse(responseCode = "404", description = "Servicio no encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "No se puede eliminar (tiene reservas activas)", content = @Content)
    })
    @DeleteMapping("/{id}/permanente")
    public ResponseEntity<Void> eliminarPermanentemente(
            @Parameter(description = "ID del servicio", example = "serv_001")
            @PathVariable String id
    ) {
        eliminarServicioUseCase.ejecutar(id);
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
            description = "Obtiene servicios filtrados por modalidad (PRESENCIAL / ONLINE / HIBRIDO)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado de servicios por modalidad",
                    content = @Content(schema = @Schema(implementation = ServicioResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Modalidad inválida", content = @Content)
    })
    @GetMapping("/modalidad/{modalidad}")
    public ResponseEntity<List<ServicioResponse>> listarPorModalidad(
            @Parameter(
                    description = "Modalidad del servicio",
                    example = "PRESENCIAL",
                    schema = @Schema(allowableValues = {"PRESENCIAL", "ONLINE", "HIBRIDO"})
            )
            @PathVariable String modalidad
    ) {
        return ResponseEntity.ok(
                listarServiciosUseCase.listarPorModalidad(modalidad)
        );
    }
}
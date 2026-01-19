package ApiGymorEjecucion.Api.presentation.controller;

import ApiGymorEjecucion.Api.application.dto.request.plan.ActualizarPlanRequest;
import ApiGymorEjecucion.Api.application.dto.request.plan.CrearPlanRequest;
import ApiGymorEjecucion.Api.application.dto.response.plan.PlanResponse;
import ApiGymorEjecucion.Api.application.usecase.plan.*;

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
        name = "Planes",
        description = "Gestión de planes de suscripción del sistema"
)
@RestController
@RequestMapping("/api/planes")
public class PlanController {

    private final CrearPlanUseCase crearPlanUseCase;
    private final ActualizarPlanUseCase actualizarPlanUseCase;
    private final DesactivarPlanUseCase desactivarPlanUseCase;
    private final ListarPlanesUseCase listarPlanesUseCase;

    public PlanController(
            CrearPlanUseCase crearPlanUseCase,
            ActualizarPlanUseCase actualizarPlanUseCase,
            DesactivarPlanUseCase desactivarPlanUseCase,
            ListarPlanesUseCase listarPlanesUseCase
    ) {
        this.crearPlanUseCase = crearPlanUseCase;
        this.actualizarPlanUseCase = actualizarPlanUseCase;
        this.desactivarPlanUseCase = desactivarPlanUseCase;
        this.listarPlanesUseCase = listarPlanesUseCase;
    }

    // -------------------------------------------------
    // CREAR PLAN
    // -------------------------------------------------
    @Operation(
            summary = "Crear plan de suscripción",
            description = "Crea un nuevo plan disponible para contratación"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Plan creado correctamente",
                    content = @Content(schema = @Schema(implementation = PlanResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Datos del plan inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<PlanResponse> crearPlan(
            @RequestBody CrearPlanRequest request
    ) {
        PlanResponse response = crearPlanUseCase.ejecutar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // -------------------------------------------------
    // ACTUALIZAR PRECIO DEL PLAN
    // -------------------------------------------------
    @Operation(
            summary = "Actualizar plan",
            description = "Actualiza el precio u otros atributos de un plan"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Plan actualizado",
                    content = @Content(schema = @Schema(implementation = PlanResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Plan no encontrado", content = @Content)
    })
    @PutMapping("/{id}/precio")
    public ResponseEntity<PlanResponse> actualizarPrecio(
            @Parameter(description = "ID del plan", example = "plan_basic")
            @PathVariable String id,
            @RequestBody ActualizarPlanRequest request
    ) {
        PlanResponse response = actualizarPlanUseCase.ejecutar(id, request);
        return ResponseEntity.ok(response);
    }

    // -------------------------------------------------
    // DESACTIVAR PLAN
    // -------------------------------------------------
    @Operation(
            summary = "Desactivar plan",
            description = "Desactiva un plan para que no pueda ser contratado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Plan desactivado correctamente"),
            @ApiResponse(responseCode = "404", description = "Plan no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarPlan(
            @Parameter(description = "ID del plan", example = "plan_basic")
            @PathVariable String id
    ) {
        desactivarPlanUseCase.ejecutar(id);
        return ResponseEntity.noContent().build();
    }

    // -------------------------------------------------
    // LISTAR PLANES ACTIVOS
    // -------------------------------------------------
    @Operation(
            summary = "Listar planes activos",
            description = "Obtiene todos los planes disponibles para los clientes"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado de planes",
                    content = @Content(schema = @Schema(implementation = PlanResponse.class))
            )
    })
    @GetMapping
    public ResponseEntity<List<PlanResponse>> listarPlanes() {
        return ResponseEntity.ok(
                listarPlanesUseCase.listarTodos()
        );
    }

    // -------------------------------------------------
    // OBTENER PLAN POR ID
    // -------------------------------------------------
    @Operation(
            summary = "Obtener plan por ID",
            description = "Obtiene el detalle de un plan específico"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Plan encontrado",
                    content = @Content(schema = @Schema(implementation = PlanResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Plan no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<PlanResponse> obtenerPorId(
            @Parameter(description = "ID del plan", example = "plan_basic")
            @PathVariable String id
    ) {
        return ResponseEntity.ok(
                listarPlanesUseCase.buscarPorId(id)
        );
    }
}

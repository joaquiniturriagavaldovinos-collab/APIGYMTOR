package ApiGymorEjecucion.Api.presentation.controller;

import ApiGymorEjecucion.Api.application.dto.request.suscripcion.ContratarSuscripcionRequest;
import ApiGymorEjecucion.Api.application.dto.response.suscripcion.SuscripcionResponse;
import ApiGymorEjecucion.Api.application.usecase.suscripcion.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(
        name = "Suscripciones",
        description = "Gestión completa de suscripciones de clientes"
)
@RestController
@RequestMapping("/api/suscripciones")
public class SuscripcionController {

    private final ContratarSuscripcionUseCase contratarSuscripcionUseCase;
    private final CancelarSuscripcionUseCase cancelarSuscripcionUseCase;
    private final SuspenderSuscripcionUseCase suspenderSuscripcionUseCase;
    private final ActivarSuscripcionUseCase activarSuscripcionUseCase;
    private final ConsumirSesionUseCase consumirSesionUseCase;
    private final RenovarSuscripcionUseCase renovarSuscripcionUseCase;
    private final ConfigurarAutorenovacionUseCase configurarAutorenovacionUseCase;
    private final ListarSuscripcionesUseCase listarSuscripcionesUseCase;

    public SuscripcionController(
            ContratarSuscripcionUseCase contratarSuscripcionUseCase,
            CancelarSuscripcionUseCase cancelarSuscripcionUseCase,
            SuspenderSuscripcionUseCase suspenderSuscripcionUseCase,
            ActivarSuscripcionUseCase activarSuscripcionUseCase,
            ConsumirSesionUseCase consumirSesionUseCase,
            RenovarSuscripcionUseCase renovarSuscripcionUseCase,
            ConfigurarAutorenovacionUseCase configurarAutorenovacionUseCase,
            ListarSuscripcionesUseCase listarSuscripcionesUseCase
    ) {
        this.contratarSuscripcionUseCase = contratarSuscripcionUseCase;
        this.cancelarSuscripcionUseCase = cancelarSuscripcionUseCase;
        this.suspenderSuscripcionUseCase = suspenderSuscripcionUseCase;
        this.activarSuscripcionUseCase = activarSuscripcionUseCase;
        this.consumirSesionUseCase = consumirSesionUseCase;
        this.renovarSuscripcionUseCase = renovarSuscripcionUseCase;
        this.configurarAutorenovacionUseCase = configurarAutorenovacionUseCase;
        this.listarSuscripcionesUseCase = listarSuscripcionesUseCase;
    }

    // -------------------------------------------------
    // CONTRATAR SUSCRIPCIÓN
    // -------------------------------------------------
    @Operation(
            summary = "Contratar suscripción",
            description = "Contrata una nueva suscripción para un cliente"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Suscripción creada correctamente",
                    content = @Content(schema = @Schema(implementation = SuscripcionResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<SuscripcionResponse> contratar(
            @RequestBody ContratarSuscripcionRequest request
    ) {
        SuscripcionResponse response = contratarSuscripcionUseCase.ejecutar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // -------------------------------------------------
    // LISTAR TODAS LAS SUSCRIPCIONES
    // -------------------------------------------------
    @Operation(
            summary = "Listar todas las suscripciones",
            description = "Obtiene todas las suscripciones del sistema"
    )
    @GetMapping
    public ResponseEntity<List<SuscripcionResponse>> listarTodas() {
        return ResponseEntity.ok(listarSuscripcionesUseCase.listarTodas());
    }

    // -------------------------------------------------
    // LISTAR SUSCRIPCIONES ACTIVAS
    // -------------------------------------------------
    @Operation(
            summary = "Listar suscripciones activas",
            description = "Obtiene solo las suscripciones activas"
    )
    @GetMapping("/activas")
    public ResponseEntity<List<SuscripcionResponse>> listarActivas() {
        return ResponseEntity.ok(listarSuscripcionesUseCase.listarActivas());
    }

    // -------------------------------------------------
    // LISTAR POR CLIENTE
    // -------------------------------------------------
    @Operation(
            summary = "Listar suscripciones por cliente",
            description = "Obtiene todas las suscripciones de un cliente específico"
    )
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<SuscripcionResponse>> listarPorCliente(
            @Parameter(description = "ID del cliente", example = "CLI-123")
            @PathVariable String clienteId
    ) {
        return ResponseEntity.ok(listarSuscripcionesUseCase.listarPorCliente(clienteId));
    }

    // -------------------------------------------------
    // LISTAR VIGENTES POR CLIENTE
    // -------------------------------------------------
    @Operation(
            summary = "Listar suscripciones vigentes de un cliente",
            description = "Obtiene solo las suscripciones activas y no vencidas de un cliente"
    )
    @GetMapping("/cliente/{clienteId}/vigentes")
    public ResponseEntity<List<SuscripcionResponse>> listarVigentesPorCliente(
            @Parameter(description = "ID del cliente", example = "CLI-123")
            @PathVariable String clienteId
    ) {
        return ResponseEntity.ok(listarSuscripcionesUseCase.listarVigentesPorCliente(clienteId));
    }

    // -------------------------------------------------
    // CONSULTAR SUSCRIPCIÓN POR ID
    // -------------------------------------------------
    @Operation(
            summary = "Consultar suscripción por ID",
            description = "Obtiene el detalle completo de una suscripción"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Suscripción encontrada",
                    content = @Content(schema = @Schema(implementation = SuscripcionResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Suscripción no encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<SuscripcionResponse> consultarPorId(
            @Parameter(description = "ID de la suscripción", example = "SUB-123")
            @PathVariable String id
    ) {
        return ResponseEntity.ok(listarSuscripcionesUseCase.buscarPorId(id));
    }

    // -------------------------------------------------
    // SUSPENDER SUSCRIPCIÓN
    // -------------------------------------------------
    @Operation(
            summary = "Suspender suscripción",
            description = "Suspende temporalmente una suscripción (puede reactivarse)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Suscripción suspendida"),
            @ApiResponse(responseCode = "404", description = "Suscripción no encontrada", content = @Content),
            @ApiResponse(responseCode = "400", description = "La suscripción ya está suspendida", content = @Content)
    })
    @PatchMapping("/{id}/suspender")
    public ResponseEntity<Void> suspender(
            @Parameter(description = "ID de la suscripción", example = "SUB-123")
            @PathVariable String id
    ) {
        suspenderSuscripcionUseCase.ejecutar(id);
        return ResponseEntity.noContent().build();
    }

    // -------------------------------------------------
    // ACTIVAR/REACTIVAR SUSCRIPCIÓN
    // -------------------------------------------------
    @Operation(
            summary = "Activar/reactivar suscripción",
            description = "Activa una suscripción previamente suspendida"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Suscripción activada",
                    content = @Content(schema = @Schema(implementation = SuscripcionResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Suscripción no encontrada", content = @Content),
            @ApiResponse(responseCode = "400", description = "La suscripción está vencida o ya está activa", content = @Content)
    })
    @PatchMapping("/{id}/activar")
    public ResponseEntity<SuscripcionResponse> activar(
            @Parameter(description = "ID de la suscripción", example = "SUB-123")
            @PathVariable String id
    ) {
        return ResponseEntity.ok(activarSuscripcionUseCase.ejecutar(id));
    }

    // -------------------------------------------------
    // CANCELAR SUSCRIPCIÓN
    // -------------------------------------------------
    @Operation(
            summary = "Cancelar suscripción",
            description = "Cancela definitivamente una suscripción (no se puede reactivar)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Suscripción cancelada"),
            @ApiResponse(responseCode = "404", description = "Suscripción no encontrada", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(
            @Parameter(description = "ID de la suscripción", example = "SUB-123")
            @PathVariable String id
    ) {
        cancelarSuscripcionUseCase.ejecutar(id);
        return ResponseEntity.noContent().build();
    }

    // -------------------------------------------------
    // CONSUMIR SESIÓN
    // -------------------------------------------------
    @Operation(
            summary = "Consumir sesión",
            description = "Registra el consumo de una sesión de la suscripción"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Sesión consumida",
                    content = @Content(schema = @Schema(implementation = SuscripcionResponse.class))
            ),
            @ApiResponse(responseCode = "409", description = "No hay sesiones disponibles", content = @Content)
    })
    @PostMapping("/{id}/consumir-sesion")
    public ResponseEntity<SuscripcionResponse> consumirSesion(
            @Parameter(description = "ID de la suscripción", example = "SUB-123")
            @PathVariable String id
    ) {
        return ResponseEntity.ok(consumirSesionUseCase.ejecutar(id));
    }

    // -------------------------------------------------
    // RENOVAR SUSCRIPCIÓN
    // -------------------------------------------------
    @Operation(
            summary = "Renovar suscripción",
            description = "Extiende la vigencia de una suscripción por X meses"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Suscripción renovada",
                    content = @Content(schema = @Schema(implementation = SuscripcionResponse.class))
            )
    })
    @PostMapping("/{id}/renovar")
    public ResponseEntity<SuscripcionResponse> renovar(
            @Parameter(description = "ID de la suscripción", example = "SUB-123")
            @PathVariable String id,
            @Parameter(description = "Duración en meses", example = "3")
            @RequestParam int duracionMeses
    ) {
        return ResponseEntity.ok(renovarSuscripcionUseCase.ejecutar(id, duracionMeses));
    }

    // -------------------------------------------------
    // CONFIGURAR AUTORENOVACIÓN
    // -------------------------------------------------
    @Operation(
            summary = "Configurar autorenovación",
            description = "Habilita o deshabilita la renovación automática de la suscripción"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Autorenovación configurada",
                    content = @Content(schema = @Schema(implementation = SuscripcionResponse.class))
            )
    })
    @PatchMapping("/{id}/autorenovacion")
    public ResponseEntity<SuscripcionResponse> configurarAutorenovacion(
            @Parameter(description = "ID de la suscripción", example = "SUB-123")
            @PathVariable String id,
            @Parameter(description = "true para habilitar, false para deshabilitar", example = "true")
            @RequestParam boolean habilitar
    ) {
        return ResponseEntity.ok(configurarAutorenovacionUseCase.ejecutar(id, habilitar));
    }
}
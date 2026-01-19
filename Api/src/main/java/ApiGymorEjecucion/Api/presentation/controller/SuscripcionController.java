package ApiGymorEjecucion.Api.presentation.controller;

import ApiGymorEjecucion.Api.application.dto.request.suscripcion.ContratarSuscripcionRequest;
import ApiGymorEjecucion.Api.application.dto.response.suscripcion.SuscripcionResponse;
import ApiGymorEjecucion.Api.application.usecase.suscripcion.CancelarSuscripcionUseCase;
import ApiGymorEjecucion.Api.application.usecase.suscripcion.ConsumirSesionUseCase;
import ApiGymorEjecucion.Api.application.usecase.suscripcion.ContratarSuscripcionUseCase;
import ApiGymorEjecucion.Api.application.usecase.suscripcion.RenovarSuscripcionUseCase;
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

import java.time.LocalDateTime;
import java.util.List;

@Tag(
        name = "Suscripciones",
        description = "Gestión de suscripciones de clientes y consumo de sesiones"
)
@RestController
@RequestMapping("/api/suscripciones")
public class SuscripcionController {

    private final ContratarSuscripcionUseCase contratarSuscripcionUseCase;
    private final CancelarSuscripcionUseCase cancelarSuscripcionUseCase;
    private final ConsumirSesionUseCase consumirSesionUseCase;
    private final RenovarSuscripcionUseCase renovarSuscripcionUseCase;

    public SuscripcionController(
            ContratarSuscripcionUseCase contratarSuscripcionUseCase,
            CancelarSuscripcionUseCase cancelarSuscripcionUseCase,
            ConsumirSesionUseCase consumirSesionUseCase,
            RenovarSuscripcionUseCase renovarSuscripcionUseCase
    ) {
        this.contratarSuscripcionUseCase = contratarSuscripcionUseCase;
        this.cancelarSuscripcionUseCase = cancelarSuscripcionUseCase;
        this.consumirSesionUseCase = consumirSesionUseCase;
        this.renovarSuscripcionUseCase = renovarSuscripcionUseCase;
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
        SuscripcionResponse response =
                contratarSuscripcionUseCase.ejecutar(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // -------------------------------------------------
    // CANCELAR SUSCRIPCIÓN
    // -------------------------------------------------
    @Operation(
            summary = "Cancelar suscripción",
            description = "Cancela una suscripción activa (soft cancel)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Suscripción cancelada"),
            @ApiResponse(responseCode = "404", description = "Suscripción no encontrada", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(
            @Parameter(description = "ID de la suscripción", example = "sub_001")
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
            description = "Consume una sesión de una suscripción activa"
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
            @Parameter(description = "ID de la suscripción", example = "sub_001")
            @PathVariable String id
    ) {
        return ResponseEntity.ok(
                consumirSesionUseCase.ejecutar(id)
        );
    }

    // -------------------------------------------------
    // RENOVAR SUSCRIPCIÓN
    // -------------------------------------------------
    @Operation(
            summary = "Renovar suscripción",
            description = "Renueva una suscripción por una cantidad de meses"
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
            @Parameter(description = "ID de la suscripción", example = "sub_001")
            @PathVariable String id,
            @Parameter(description = "Duración en meses", example = "3")
            @RequestParam int duracionMeses
    ) {
        return ResponseEntity.ok(
                renovarSuscripcionUseCase.ejecutar(id, duracionMeses)
        );
    }
}

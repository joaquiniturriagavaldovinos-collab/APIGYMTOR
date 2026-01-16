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

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller REST para operaciones de Suscripciones
 */
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

    // =========================
    // 📌 CONTRATAR SUSCRIPCIÓN
    // =========================
    @PostMapping
    public ResponseEntity<SuscripcionResponse> contratar(
            @RequestBody ContratarSuscripcionRequest request
    ) {
        SuscripcionResponse response =
                contratarSuscripcionUseCase.ejecutar(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // =========================
    // 📌 CANCELAR SUSCRIPCIÓN
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(
            @PathVariable String id
    ) {
        cancelarSuscripcionUseCase.ejecutar(id);
        return ResponseEntity.noContent().build();
    }

    // =========================
    // 📌 CONSUMIR SESIÓN
    // =========================
    @PostMapping("/{id}/consumir-sesion")
    public ResponseEntity<SuscripcionResponse> consumirSesion(
            @PathVariable String id
    ) {
        return ResponseEntity.ok(
                consumirSesionUseCase.ejecutar(id)
        );
    }

    // =========================
    // 📌 RENOVAR SUSCRIPCIÓN
    // =========================
    @PostMapping("/{id}/renovar")
    public ResponseEntity<SuscripcionResponse> renovar(
            @PathVariable String id,
            @RequestParam int duracionMeses
    ) {
        return ResponseEntity.ok(
                renovarSuscripcionUseCase.ejecutar(id, duracionMeses)
        );
    }
}

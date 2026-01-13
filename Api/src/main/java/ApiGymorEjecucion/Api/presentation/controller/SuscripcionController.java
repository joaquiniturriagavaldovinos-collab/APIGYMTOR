package ApiGymorEjecucion.Api.presentation.controller;

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

    /**
     * Contratar un plan (crear suscripción)
     * POST /api/suscripciones
     */
    @PostMapping
    public ResponseEntity<SuscripcionResponse> contratarPlan(
            @RequestBody ContratarPlanRequest request) {

        // TODO: Implementar ContratarPlanUseCase
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    /**
     * Consultar suscripciones de un cliente
     * GET /api/suscripciones/cliente/{clienteId}
     */
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<SuscripcionResponse>> buscarPorCliente(
            @PathVariable String clienteId) {

        // TODO: Implementar ConsultarSuscripcionesPorClienteUseCase
        return ResponseEntity.ok().build();
    }

    /**
     * Consultar suscripción por ID
     * GET /api/suscripciones/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<SuscripcionResponse> buscarPorId(@PathVariable String id) {
        // TODO: Implementar ConsultarSuscripcionUseCase
        return ResponseEntity.ok().build();
    }

    /**
     * Consumir una sesión
     * POST /api/suscripciones/{id}/consumir-sesion
     */
    @PostMapping("/{id}/consumir-sesion")
    public ResponseEntity<SuscripcionResponse> consumirSesion(@PathVariable String id) {
        // TODO: Implementar ConsumirSesionUseCase
        return ResponseEntity.ok().build();
    }

    /**
     * Renovar suscripción
     * POST /api/suscripciones/{id}/renovar
     */
    @PostMapping("/{id}/renovar")
    public ResponseEntity<SuscripcionResponse> renovarSuscripcion(
            @PathVariable String id,
            @RequestParam int duracionMeses) {

        // TODO: Implementar RenovarSuscripcionUseCase
        return ResponseEntity.ok().build();
    }

    /**
     * Cancelar suscripción
     * POST /api/suscripciones/{id}/cancelar
     */
    @PostMapping("/{id}/cancelar")
    public ResponseEntity<SuscripcionResponse> cancelarSuscripcion(@PathVariable String id) {
        // TODO: Implementar CancelarSuscripcionUseCase
        return ResponseEntity.ok().build();
    }

    /**
     * Habilitar autorenovación
     * POST /api/suscripciones/{id}/autorenovar
     */
    @PostMapping("/{id}/autorenovar")
    public ResponseEntity<SuscripcionResponse> habilitarAutorenovacion(
            @PathVariable String id,
            @RequestParam boolean habilitar) {

        // TODO: Implementar ConfigurarAutorenovacionUseCase
        return ResponseEntity.ok().build();
    }

    // DTOs
    public static class ContratarPlanRequest {
        private String clienteId;
        private String planId;
        // getters/setters
    }

    public static class SuscripcionResponse {
        private String id;
        private String clienteId;
        private String planId;
        private LocalDateTime fechaInicio;
        private LocalDateTime fechaVencimiento;
        private int sesionesRestantes;
        private boolean activa;
        private boolean autorrenovable;
        private boolean estaVigente;
        private long diasRestantes;
        // getters/setters
    }
}

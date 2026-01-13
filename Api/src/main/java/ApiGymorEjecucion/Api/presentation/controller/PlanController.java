package ApiGymorEjecucion.Api.presentation.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controller REST para operaciones de Planes de Suscripción
 */
@RestController
@RequestMapping("/api/planes")
public class PlanController {

    /**
     * Crear un nuevo plan
     * POST /api/planes
     */
    @PostMapping
    public ResponseEntity<PlanResponse> crearPlan(
            @RequestBody CrearPlanRequest request) {

        // TODO: Implementar CrearPlanUseCase
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    /**
     * Listar todos los planes activos
     * GET /api/planes
     */
    @GetMapping
    public ResponseEntity<List<PlanResponse>> listarPlanes() {
        // TODO: Implementar ListarPlanesUseCase
        return ResponseEntity.ok().build();
    }

    /**
     * Buscar plan por ID
     * GET /api/planes/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlanResponse> buscarPorId(@PathVariable String id) {
        // TODO: Implementar ConsultarPlanUseCase
        return ResponseEntity.ok().build();
    }

    /**
     * Actualizar precio de plan
     * PUT /api/planes/{id}/precio
     */
    @PutMapping("/{id}/precio")
    public ResponseEntity<PlanResponse> actualizarPrecio(
            @PathVariable String id,
            @RequestBody ActualizarPrecioPlanRequest request) {

        // TODO: Implementar ActualizarPrecioPlanUseCase
        return ResponseEntity.ok().build();
    }

    /**
     * Desactivar plan
     * DELETE /api/planes/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarPlan(@PathVariable String id) {
        // TODO: Implementar DesactivarPlanUseCase
        return ResponseEntity.noContent().build();
    }

    // DTOs
    public static class CrearPlanRequest {
        private String nombre;
        private String descripcion;
        private BigDecimal precio;
        private int duracionMeses;
        private int sesionesIncluidas; // 0 = ilimitadas
        // getters/setters
    }

    public static class PlanResponse {
        private String id;
        private String nombre;
        private String descripcion;
        private BigDecimal precio;
        private int duracionMeses;
        private int sesionesIncluidas;
        private boolean activo;
        private BigDecimal precioPorMes;
        // getters/setters
    }

    public static class ActualizarPrecioPlanRequest {
        private BigDecimal nuevoPrecio;
        // getters/setters
    }
}

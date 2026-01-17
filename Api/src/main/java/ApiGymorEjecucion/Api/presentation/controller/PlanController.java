package ApiGymorEjecucion.Api.presentation.controller;


import ApiGymorEjecucion.Api.application.dto.request.plan.ActualizarPlanRequest;
import ApiGymorEjecucion.Api.application.dto.request.plan.CrearPlanRequest;
import ApiGymorEjecucion.Api.application.dto.response.plan.PlanResponse;
import ApiGymorEjecucion.Api.application.usecase.plan.ActualizarPlanUseCase;
import ApiGymorEjecucion.Api.application.usecase.plan.CrearPlanUseCase;
import ApiGymorEjecucion.Api.application.usecase.plan.DesactivarPlanUseCase;
import ApiGymorEjecucion.Api.application.usecase.plan.ListarPlanesUseCase;
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

    // 1 Crear Plan
    @PostMapping
    public ResponseEntity<PlanResponse> crearPlan(
            @RequestBody CrearPlanRequest request
    ) {
        PlanResponse response = crearPlanUseCase.ejecutar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 2 Actualizar precio del plan
    @PutMapping("/{id}/precio")
    public ResponseEntity<PlanResponse> actualizarPrecio(
            @PathVariable String id,
            @RequestBody ActualizarPlanRequest request
    ) {
        PlanResponse response = actualizarPlanUseCase.ejecutar(id, request);
        return ResponseEntity.ok(response);
    }

    // 3 Desactivar plan
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarPlan(
            @PathVariable String id
    ) {
        desactivarPlanUseCase.ejecutar(id);
        return ResponseEntity.noContent().build();
    }

    // 4 Listar planes activos
    @GetMapping
    public ResponseEntity<List<PlanResponse>> listarPlanes() {
        return ResponseEntity.ok(
                listarPlanesUseCase.listarTodos()
        );
    }

    // 5 Obtener plan por ID
    @GetMapping("/{id}")
    public ResponseEntity<PlanResponse> obtenerPorId(
            @PathVariable String id
    ) {
        return ResponseEntity.ok(
                listarPlanesUseCase.buscarPorId(id)
        );
    }
}

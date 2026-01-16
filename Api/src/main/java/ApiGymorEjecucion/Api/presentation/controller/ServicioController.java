package ApiGymorEjecucion.Api.presentation.controller;


import ApiGymorEjecucion.Api.application.dto.request.servicio.ActualizarServicioRequest;
import ApiGymorEjecucion.Api.application.dto.request.servicio.CrearServicioRequest;
import ApiGymorEjecucion.Api.application.dto.response.servicio.ServicioResponse;
import ApiGymorEjecucion.Api.application.usecase.servicio.ActualizarServicioUseCase;
import ApiGymorEjecucion.Api.application.usecase.servicio.CrearServicioUseCase;
import ApiGymorEjecucion.Api.application.usecase.servicio.DesactivarServicioUseCase;
import ApiGymorEjecucion.Api.application.usecase.servicio.ListarServiciosUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controller REST para operaciones de Servicios (Clases de entrenamiento)
 */
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

    // =========================
    // 📌 CREAR SERVICIO
    // =========================
    @PostMapping
    public ResponseEntity<ServicioResponse> crear(
            @RequestBody CrearServicioRequest request
    ) {
        ServicioResponse response = crearServicioUseCase.ejecutar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // =========================
    // 📌 ACTUALIZAR PRECIO
    // =========================
    @PutMapping("/{id}/precio")
    public ResponseEntity<ServicioResponse> actualizarPrecio(
            @PathVariable String id,
            @RequestBody ActualizarServicioRequest request
    ) {
        return ResponseEntity.ok(
                actualizarServicioUseCase.ejecutar(id, request)
        );
    }

    // =========================
    // 📌 DESACTIVAR SERVICIO
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(
            @PathVariable String id
    ) {
        desactivarServicioUseCase.ejecutar(id);
        return ResponseEntity.noContent().build();
    }

    // =========================
    // 📌 LISTAR SERVICIOS
    // =========================
    @GetMapping
    public ResponseEntity<List<ServicioResponse>> listarTodos() {
        return ResponseEntity.ok(
                listarServiciosUseCase.listarTodos()
        );
    }

    // =========================
    // 📌 LISTAR POR MODALIDAD
    // =========================
    @GetMapping("/modalidad/{modalidad}")
    public ResponseEntity<List<ServicioResponse>> listarPorModalidad(
            @PathVariable String modalidad
    ) {
        return ResponseEntity.ok(
                listarServiciosUseCase.listarPorModalidad(modalidad)
        );
    }
}

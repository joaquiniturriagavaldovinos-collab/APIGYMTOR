package ApiGymorEjecucion.Api.presentation.controller;


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

    /**
     * Crear un nuevo servicio
     * POST /api/servicios
     */
    @PostMapping
    public ResponseEntity<ServicioResponse> crearServicio(
            @RequestBody CrearServicioRequest request) {

        // TODO: Implementar CrearServicioUseCase
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    /**
     * Listar todos los servicios activos
     * GET /api/servicios
     */
    @GetMapping
    public ResponseEntity<List<ServicioResponse>> listarServicios(
            @RequestParam(required = false) String modalidad) {

        // TODO: Implementar ListarServiciosUseCase
        // Si modalidad != null, filtrar (PRESENCIAL/ONLINE/HIBRIDO)
        return ResponseEntity.ok().build();
    }

    /**
     * Buscar servicio por ID
     * GET /api/servicios/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServicioResponse> buscarPorId(@PathVariable String id) {
        // TODO: Implementar ConsultarServicioUseCase
        return ResponseEntity.ok().build();
    }

    /**
     * Actualizar precio de servicio
     * PUT /api/servicios/{id}/precio
     */
    @PutMapping("/{id}/precio")
    public ResponseEntity<ServicioResponse> actualizarPrecio(
            @PathVariable String id,
            @RequestBody ActualizarPrecioRequest request) {

        // TODO: Implementar ActualizarPrecioServicioUseCase
        return ResponseEntity.ok().build();
    }

    /**
     * Desactivar servicio
     * DELETE /api/servicios/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarServicio(@PathVariable String id) {
        // TODO: Implementar DesactivarServicioUseCase
        return ResponseEntity.noContent().build();
    }

    // DTOs
    public static class CrearServicioRequest {
        private String nombre;
        private String descripcion;
        private String modalidad; // PRESENCIAL, ONLINE, HIBRIDO
        private BigDecimal precioSesion;
        private int duracionMinutos;
        private int capacidadMaxima;
        // getters/setters
    }

    public static class ServicioResponse {
        private String id;
        private String nombre;
        private String descripcion;
        private String modalidad;
        private BigDecimal precioSesion;
        private int duracionMinutos;
        private int capacidadMaxima;
        private boolean activo;
        // getters/setters
    }

    public static class ActualizarPrecioRequest {
        private BigDecimal nuevoPrecio;
        // getters/setters
    }
}
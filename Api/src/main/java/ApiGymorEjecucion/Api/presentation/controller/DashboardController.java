package ApiGymorEjecucion.Api.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Controller REST para métricas y dashboard administrativo
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    /**
     * Obtener resumen de métricas
     * GET /api/dashboard/resumen
     */
    @GetMapping("/resumen")
    public ResponseEntity<ResumenDashboard> obtenerResumen(
            @RequestParam(required = false) LocalDate desde,
            @RequestParam(required = false) LocalDate hasta) {

        // TODO: Implementar ObtenerResumenDashboardUseCase
        return ResponseEntity.ok().build();
    }

    /**
     * Ventas por día
     * GET /api/dashboard/ventas-diarias
     */
    @GetMapping("/ventas-diarias")
    public ResponseEntity<?> obtenerVentasDiarias(
            @RequestParam LocalDate desde,
            @RequestParam LocalDate hasta) {

        // TODO: Implementar ObtenerVentasDiariasUseCase
        return ResponseEntity.ok().build();
    }

    /**
     * Productos más vendidos
     * GET /api/dashboard/productos-top
     */
    @GetMapping("/productos-top")
    public ResponseEntity<?> obtenerProductosTop(
            @RequestParam(defaultValue = "10") int limit) {

        // TODO: Implementar ObtenerProductosTopUseCase
        return ResponseEntity.ok().build();
    }

    /**
     * Estado de pedidos
     * GET /api/dashboard/estados-pedidos
     */
    @GetMapping("/estados-pedidos")
    public ResponseEntity<?> obtenerEstadosPedidos() {
        // TODO: Implementar ObtenerDistribucionEstadosUseCase
        return ResponseEntity.ok().build();
    }

    // DTOs
    public static class ResumenDashboard {
        private long totalPedidos;
        private long pedidosCompletados;
        private long pedidosPendientes;
        private BigDecimal ventasTotales;
        private BigDecimal ventasDelDia;
        private long clientesNuevos;
        private long productosConBajoStock;
        // getters/setters
    }
}
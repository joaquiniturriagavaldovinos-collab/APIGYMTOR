package ApiGymorEjecucion.Api.presentation.controller;

import ApiGymorEjecucion.Api.application.dto.response.dashboard.MetricasGeneralesResponse;
import ApiGymorEjecucion.Api.application.dto.response.dashboard.ProductoTopResponse;
import ApiGymorEjecucion.Api.application.usecase.dashboard.ObtenerMetricasGeneralesUseCase;
import ApiGymorEjecucion.Api.application.usecase.dashboard.ObtenerResumenVentasUseCase;
import ApiGymorEjecucion.Api.application.usecase.dashboard.ObtenerTopProductosUseCase;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Controller REST para métricas y dashboard administrativo
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final ObtenerMetricasGeneralesUseCase obtenerMetricasGeneralesUseCase;
    private final ObtenerResumenVentasUseCase obtenerResumenVentasUseCase;
    private final ObtenerTopProductosUseCase obtenerTopProductosUseCase;

    public DashboardController(
            ObtenerMetricasGeneralesUseCase obtenerMetricasGeneralesUseCase,
            ObtenerResumenVentasUseCase obtenerResumenVentasUseCase,
            ObtenerTopProductosUseCase obtenerTopProductosUseCase
    ) {
        this.obtenerMetricasGeneralesUseCase = obtenerMetricasGeneralesUseCase;
        this.obtenerResumenVentasUseCase = obtenerResumenVentasUseCase;
        this.obtenerTopProductosUseCase = obtenerTopProductosUseCase;
    }

    // 1️⃣ Métricas generales del sistema
    @GetMapping("/metricas-generales")
    public ResponseEntity<MetricasGeneralesResponse> obtenerMetricasGenerales() {
        return ResponseEntity.ok(
                obtenerMetricasGeneralesUseCase.ejecutar()
        );
    }

    // 2️⃣ Resumen de ventas (rango de fechas opcional)
    @GetMapping("/ventas/resumen")
    public ResponseEntity<ObtenerResumenVentasUseCase.ResumenVentasResponse> obtenerResumenVentas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate hasta
    ) {
        return ResponseEntity.ok(
                obtenerResumenVentasUseCase.ejecutar(desde, hasta)
        );
    }

    // 3️⃣ Top productos más vendidos
    @GetMapping("/productos/top")
    public ResponseEntity<List<ProductoTopResponse>> obtenerTopProductos(
            @RequestParam(defaultValue = "10") int limit
    ) {
        return ResponseEntity.ok(
                obtenerTopProductosUseCase.ejecutar(limit)
        );
    }
}
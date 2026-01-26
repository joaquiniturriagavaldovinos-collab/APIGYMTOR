package ApiGymorEjecucion.Api.presentation.controller;

import ApiGymorEjecucion.Api.application.dto.response.dashboard.MetricasGeneralesResponse;
import ApiGymorEjecucion.Api.application.dto.response.dashboard.ProductoTopResponse;
import ApiGymorEjecucion.Api.application.dto.response.dashboard.ResumenVentasResponse;
import ApiGymorEjecucion.Api.application.usecase.dashboard.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(
        name = "Dashboard",
        description = "Métricas y reportes para el panel de administración"
)
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final ObtenerMetricasGeneralesUseCase obtenerMetricasGeneralesUseCase;
    private final ObtenerResumenVentasUseCase obtenerResumenVentasUseCase;
    private final ObtenerTopProductosUseCase obtenerTopProductosUseCase;

    public DashboardController(
            ObtenerMetricasGeneralesUseCase obtenerMetricasGeneralesUseCase,
            ObtenerResumenVentasUseCase obtenerResumenVentasUseCase,
            ObtenerTopProductosUseCase obtenerTopProductosUseCase) {
        this.obtenerMetricasGeneralesUseCase = obtenerMetricasGeneralesUseCase;
        this.obtenerResumenVentasUseCase = obtenerResumenVentasUseCase;
        this.obtenerTopProductosUseCase = obtenerTopProductosUseCase;
    }

    // -----------------------------------------
    // MÉTRICAS GENERALES
    // -----------------------------------------
    @Operation(
            summary = "Obtener métricas generales",
            description = "Obtiene las métricas generales del sistema (pedidos, clientes, ingresos, etc.)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Métricas obtenidas exitosamente",
                    content = @Content(schema = @Schema(implementation = MetricasGeneralesResponse.class))
            )
    })
    @GetMapping("/metricas-generales")
    public ResponseEntity<MetricasGeneralesResponse> obtenerMetricasGenerales() {
        MetricasGeneralesResponse response = obtenerMetricasGeneralesUseCase.ejecutar();
        return ResponseEntity.ok(response);
    }

    // -----------------------------------------
    // RESUMEN DE VENTAS
    // -----------------------------------------
    @Operation(
            summary = "Obtener resumen de ventas",
            description = "Obtiene el resumen de ventas para un rango de fechas específico"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Resumen de ventas obtenido",
                    content = @Content(schema = @Schema(implementation = ResumenVentasResponse.class))
            )
    })
    @GetMapping("/ventas/resumen")
    public ResponseEntity<ResumenVentasResponse> obtenerResumenVentas(
            @Parameter(description = "Fecha de inicio (formato: yyyy-MM-dd)", example = "2026-01-01")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaInicio,

            @Parameter(description = "Fecha de fin (formato: yyyy-MM-dd)", example = "2026-01-31")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaFin) {

        ResumenVentasResponse response = obtenerResumenVentasUseCase.ejecutar(fechaInicio, fechaFin);
        return ResponseEntity.ok(response);
    }

    // -----------------------------------------
    // TOP PRODUCTOS
    // -----------------------------------------
    @Operation(
            summary = "Obtener productos más vendidos",
            description = "Obtiene la lista de los productos más vendidos"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Top productos obtenido",
                    content = @Content(schema = @Schema(implementation = ProductoTopResponse.class))
            )
    })
    @GetMapping("/productos/top")
    public ResponseEntity<List<ProductoTopResponse>> obtenerTopProductos(
            @Parameter(description = "Cantidad de productos a retornar", example = "10")
            @RequestParam(defaultValue = "10") int limite) {

        List<ProductoTopResponse> response = obtenerTopProductosUseCase.ejecutar(limite);
        return ResponseEntity.ok(response);
    }
}
package ApiGymorEjecucion.Api.presentation.controller;

import ApiGymorEjecucion.Api.application.dto.response.dashboard.MetricasGeneralesResponse;
import ApiGymorEjecucion.Api.application.dto.response.dashboard.ProductoTopResponse;
import ApiGymorEjecucion.Api.application.dto.response.dashboard.ResumenVentasResponse;
import ApiGymorEjecucion.Api.application.usecase.dashboard.ObtenerMetricasGeneralesUseCase;
import ApiGymorEjecucion.Api.application.usecase.dashboard.ObtenerResumenVentasUseCase;
import ApiGymorEjecucion.Api.application.usecase.dashboard.ObtenerTopProductosUseCase;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;

/**
 * Controller REST para métricas y dashboard administrativo
 */
@Tag(
        name = "Dashboard",
        description = "Endpoints de métricas y reportes administrativos del sistema"
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
            ObtenerTopProductosUseCase obtenerTopProductosUseCase
    ) {
        this.obtenerMetricasGeneralesUseCase = obtenerMetricasGeneralesUseCase;
        this.obtenerResumenVentasUseCase = obtenerResumenVentasUseCase;
        this.obtenerTopProductosUseCase = obtenerTopProductosUseCase;
    }

    // -------------------------------------------------
    // MÉTRICAS GENERALES
    // -------------------------------------------------
    @Operation(
            summary = "Obtener métricas generales",
            description = "Retorna indicadores generales del sistema como total de ventas, clientes, pedidos y productos"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Métricas obtenidas correctamente",
                    content = @Content(schema = @Schema(implementation = MetricasGeneralesResponse.class))
            )
    })
    @GetMapping("/metricas-generales")
    public ResponseEntity<MetricasGeneralesResponse> obtenerMetricasGenerales() {

        return ResponseEntity.ok(
                obtenerMetricasGeneralesUseCase.ejecutar()
        );
    }

    // -------------------------------------------------
    // RESUMEN DE VENTAS
    // -------------------------------------------------
    @Operation(
            summary = "Obtener resumen de ventas",
            description = "Retorna un resumen de ventas. Permite filtrar opcionalmente por rango de fechas"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Resumen de ventas generado correctamente",
                    content = @Content(schema = @Schema(implementation = ResumenVentasResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Parámetros de fecha inválidos", content = @Content)
    })
    @GetMapping("/ventas/resumen")
    public ResponseEntity<ResumenVentasResponse> obtenerResumenVentas(

            @Parameter(
                    description = "Fecha inicio del rango (YYYY-MM-DD)",
                    example = "2024-01-01"
            )
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate desde,

            @Parameter(
                    description = "Fecha fin del rango (YYYY-MM-DD)",
                    example = "2024-01-31"
            )
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate hasta
    ) {

        return ResponseEntity.ok(
                obtenerResumenVentasUseCase.ejecutar(desde, hasta)
        );
    }

    // -------------------------------------------------
    // TOP PRODUCTOS
    // -------------------------------------------------
    @Operation(
            summary = "Obtener top productos",
            description = "Retorna el ranking de productos más vendidos"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado de productos más vendidos",
                    content = @Content(
                            schema = @Schema(implementation = ProductoTopResponse.class)
                    )
            )
    })
    @GetMapping("/productos/top")
    public ResponseEntity<List<ProductoTopResponse>> obtenerTopProductos(

            @Parameter(
                    description = "Cantidad máxima de productos a retornar",
                    example = "10"
            )
            @RequestParam(defaultValue = "10")
            int limit
    ) {

        return ResponseEntity.ok(
                obtenerTopProductosUseCase.ejecutar(limit)
        );
    }
}

package ApiGymorEjecucion.Api.application.usecase.dashboard;

import ApiGymorEjecucion.Api.application.dto.response.dashboard.ResumenVentasResponse;
import ApiGymorEjecucion.Api.domain.model.Pago.EstadoPago;
import ApiGymorEjecucion.Api.domain.model.Pago.Pago;
import ApiGymorEjecucion.Api.domain.repository.PagoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.RoundingMode;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ObtenerResumenVentasUseCase {

    private final PagoRepository pagoRepository;

    public ObtenerResumenVentasUseCase(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    @Transactional(readOnly = true)
    public ResumenVentasResponse ejecutar(LocalDate fechaInicio, LocalDate fechaFin) {

        // Si no se especifican fechas, usar el mes actual
        if (fechaInicio == null) {
            fechaInicio = LocalDate.now().withDayOfMonth(1);
        }
        if (fechaFin == null) {
            fechaFin = LocalDate.now();
        }

        // Obtener todos los pagos exitosos
        List<Pago> pagosExitosos = pagoRepository.buscarPorEstado(EstadoPago.EXITOSO);

        // Filtrar por rango de fechas
        LocalDateTime inicioDateTime = fechaInicio.atStartOfDay();
        LocalDateTime finDateTime = fechaFin.atTime(23, 59, 59);

        List<Pago> pagosFiltrados = pagosExitosos.stream()
                .filter(pago -> {
                    LocalDateTime fechaPago = pago.getFechaConfirmacion();
                    return fechaPago != null
                            && !fechaPago.isBefore(inicioDateTime)
                            && !fechaPago.isAfter(finDateTime);
                })
                .toList();

        // Calcular métricas
        long totalVentas = pagosFiltrados.size();
        BigDecimal montoTotal = pagosFiltrados.stream()
                .map(Pago::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal ventaPromedio = totalVentas > 0
                ? montoTotal.divide(BigDecimal.valueOf(totalVentas), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        // Construir response
        ResumenVentasResponse response = new ResumenVentasResponse();
        response.setFechaInicio(fechaInicio);
        response.setFechaFin(fechaFin);
        response.setTotalVentas(totalVentas);
        response.setMontoTotal(montoTotal);
        response.setVentaPromedio(ventaPromedio);

        return response;
    }
}
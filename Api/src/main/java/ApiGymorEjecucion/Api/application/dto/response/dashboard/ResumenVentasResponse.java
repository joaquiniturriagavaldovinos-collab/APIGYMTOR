package ApiGymorEjecucion.Api.application.dto.response.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResumenVentasResponse {

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    private long totalVentas;
    private BigDecimal montoTotal;
    private BigDecimal ventaPromedio;
}

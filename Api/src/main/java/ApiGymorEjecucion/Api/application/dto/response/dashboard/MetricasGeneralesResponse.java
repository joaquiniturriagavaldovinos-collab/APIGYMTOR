package ApiGymorEjecucion.Api.application.dto.response.dashboard;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MetricasGeneralesResponse {
    private long totalPedidos;
    private long pedidosCompletados;
    private long pedidosEntregados;
    private long pedidosPendientes;
    private BigDecimal ingresosTotales;
    private long pedidosEnProceso;
    private long totalClientes;
    private long clientesActivos;
    private long clientesNuevosMes;
    private long totalProductos;
    private long productosConStock;
    private long productosBajoStock;
}

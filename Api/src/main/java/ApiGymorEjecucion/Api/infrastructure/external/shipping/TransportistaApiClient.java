package ApiGymorEjecucion.Api.infrastructure.external.shipping;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Cliente simulado para la API del transportista
 * En producción, esto se conectaría a la API real
 */
@Component
public class TransportistaApiClient {

    public EstadoEnvioResponse consultarEstado(String numeroGuia) {
        // Simulación - en producción haría un HTTP call
        return new EstadoEnvioResponse(
                "EN_TRANSITO",
                "El paquete está en camino",
                LocalDateTime.now()
        );
    }

    public static class EstadoEnvioResponse {
        private final String estado;
        private final String descripcion;
        private final LocalDateTime fechaEntregaReal;

        public EstadoEnvioResponse(String estado, String descripcion, LocalDateTime fechaEntregaReal) {
            this.estado = estado;
            this.descripcion = descripcion;
            this.fechaEntregaReal = fechaEntregaReal;
        }

        public String getEstado() {
            return estado;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public LocalDateTime getFechaEntregaReal() {
            return fechaEntregaReal;
        }
    }
}
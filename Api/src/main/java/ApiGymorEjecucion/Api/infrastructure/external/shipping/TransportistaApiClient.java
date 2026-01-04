package ApiGymorEjecucion.Api.infrastructure.external.shipping;


import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Cliente para integración con API de transportista
 *
 * NOTA: Implementación MOCK para desarrollo
 */
@Component
public class TransportistaApiClient {

    /**
     * Registra un envío en el sistema del transportista
     */
    public RegistrarEnvioResponse registrarEnvio(RegistrarEnvioRequest request) {
        // TODO: Implementar llamada HTTP real a API del transportista

        System.out.println("=== MOCK: Registrando envío con transportista ===");
        System.out.println("Destino: " + request.getDireccion());
        System.out.println("Peso estimado: " + request.getPesoKg() + " kg");

        String numeroGuia = "TRACK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String urlTracking = "https://tracking-mock.com/" + numeroGuia;

        return new RegistrarEnvioResponse(
                numeroGuia,
                urlTracking,
                LocalDateTime.now().plusDays(3) // Entrega estimada en 3 días
        );
    }

    /**
     * Consulta el estado de un envío
     */
    public EstadoEnvioResponse consultarEstado(String numeroGuia) {
        // TODO: Implementar llamada HTTP real

        System.out.println("=== MOCK: Consultando estado de envío ===");
        System.out.println("Guía: " + numeroGuia);

        return new EstadoEnvioResponse(
                numeroGuia,
                "EN_TRANSITO",
                "El paquete está en camino",
                null
        );
    }

    /**
     * Cancela un envío programado
     */
    public void cancelarEnvio(String numeroGuia) {
        // TODO: Implementar llamada HTTP real

        System.out.println("=== MOCK: Cancelando envío ===");
        System.out.println("Guía: " + numeroGuia);
    }

    // ===== DTOs =====

    public static class RegistrarEnvioRequest {
        private String pedidoId;
        private String direccion;
        private String nombreDestinatario;
        private String telefonoDestinatario;
        private double pesoKg;
        private String observaciones;

        // Constructor
        public RegistrarEnvioRequest(String pedidoId, String direccion, String nombreDestinatario,
                                     String telefonoDestinatario, double pesoKg, String observaciones) {
            this.pedidoId = pedidoId;
            this.direccion = direccion;
            this.nombreDestinatario = nombreDestinatario;
            this.telefonoDestinatario = telefonoDestinatario;
            this.pesoKg = pesoKg;
            this.observaciones = observaciones;
        }

        // Getters
        public String getPedidoId() {
            return pedidoId;
        }

        public String getDireccion() {
            return direccion;
        }

        public String getNombreDestinatario() {
            return nombreDestinatario;
        }

        public String getTelefonoDestinatario() {
            return telefonoDestinatario;
        }

        public double getPesoKg() {
            return pesoKg;
        }

        public String getObservaciones() {
            return observaciones;
        }
    }

    public static class RegistrarEnvioResponse {
        private final String numeroGuia;
        private final String urlTracking;
        private final LocalDateTime fechaEntregaEstimada;

        public RegistrarEnvioResponse(String numeroGuia, String urlTracking, LocalDateTime fechaEntregaEstimada) {
            this.numeroGuia = numeroGuia;
            this.urlTracking = urlTracking;
            this.fechaEntregaEstimada = fechaEntregaEstimada;
        }

        public String getNumeroGuia() {
            return numeroGuia;
        }

        public String getUrlTracking() {
            return urlTracking;
        }

        public LocalDateTime getFechaEntregaEstimada() {
            return fechaEntregaEstimada;
        }
    }

    public static class EstadoEnvioResponse {
        private final String numeroGuia;
        private final String estado;
        private final String descripcion;
        private final LocalDateTime fechaEntregaReal;

        public EstadoEnvioResponse(String numeroGuia, String estado, String descripcion, LocalDateTime fechaEntregaReal) {
            this.numeroGuia = numeroGuia;
            this.estado = estado;
            this.descripcion = descripcion;
            this.fechaEntregaReal = fechaEntregaReal;
        }

        public String getNumeroGuia() {
            return numeroGuia;
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
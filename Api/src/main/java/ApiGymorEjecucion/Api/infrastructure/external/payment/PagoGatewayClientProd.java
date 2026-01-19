package ApiGymorEjecucion.Api.infrastructure.external.payment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Cliente REAL para Transbank WebPay Plus
 *
 * IMPORTANTE: Este es un ejemplo funcional para producción
 * Requiere credenciales de Transbank
 */
@Component
@Profile("prod")  // Solo se activa en producción
public class PagoGatewayClientProd {

    @Value("${transbank.api.url:https://webpay3gint.transbank.cl}")
    private String apiUrl;

    @Value("${transbank.api.key}")
    private String apiKey;

    @Value("${transbank.commerce.code}")
    private String commerceCode;

    private final RestTemplate restTemplate;

    public PagoGatewayClientProd() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Inicia una transacción en Transbank WebPay Plus
     */
    public IniciarPagoResponse iniciarPago(String pedidoId, BigDecimal monto, String metodoPago) {

        String endpoint = apiUrl + "/rswebpaytransaction/api/webpay/v1.2/transactions";

        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Tbk-Api-Key-Id", commerceCode);
        headers.set("Tbk-Api-Key-Secret", apiKey);

        // Body (según documentación Transbank)
        Map<String, Object> requestBody = Map.of(
                "buy_order", pedidoId,
                "session_id", "sesion-" + pedidoId,
                "amount", monto.intValue(),  // Transbank usa int (pesos chilenos)
                "return_url", "https://tu-app.com/api/webhooks/pago/return"
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    endpoint,
                    request,
                    Map.class
            );

            Map<String, Object> body = response.getBody();

            return new IniciarPagoResponse(
                    (String) body.get("token"),           // Token de transacción
                    (String) body.get("url") + "?token_ws=" + body.get("token"),  // URL de pago
                    "INITIATED"
            );

        } catch (Exception e) {
            System.err.println("Error al iniciar pago en Transbank: " + e.getMessage());
            throw new RuntimeException("Error en pasarela de pago", e);
        }
    }

    /**
     * Confirma una transacción con Transbank
     */
    public ConsultarPagoResponse confirmarTransaccion(String token) {

        String endpoint = apiUrl + "/rswebpaytransaction/api/webpay/v1.2/transactions/" + token;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Tbk-Api-Key-Id", commerceCode);
        headers.set("Tbk-Api-Key-Secret", apiKey);

        HttpEntity<?> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    endpoint,
                    HttpMethod.PUT,
                    request,
                    Map.class
            );

            Map<String, Object> body = response.getBody();

            String status = "APPROVED".equals(body.get("status")) ? "APPROVED" : "REJECTED";
            String authCode = (String) body.get("authorization_code");

            return new ConsultarPagoResponse(token, status, authCode);

        } catch (Exception e) {
            System.err.println("Error al confirmar pago: " + e.getMessage());
            return new ConsultarPagoResponse(token, "ERROR", null);
        }
    }

    // ===== DTOs (mismos que el MOCK) =====

    public static class IniciarPagoResponse {
        private final String referenciaPasarela;
        private final String urlPago;
        private final String estado;

        public IniciarPagoResponse(String referenciaPasarela, String urlPago, String estado) {
            this.referenciaPasarela = referenciaPasarela;
            this.urlPago = urlPago;
            this.estado = estado;
        }

        public String getReferenciaPasarela() { return referenciaPasarela; }
        public String getUrlPago() { return urlPago; }
        public String getEstado() { return estado; }
    }

    public static class ConsultarPagoResponse {
        private final String referencia;
        private final String estado;
        private final String codigoAutorizacion;

        public ConsultarPagoResponse(String referencia, String estado, String codigoAutorizacion) {
            this.referencia = referencia;
            this.estado = estado;
            this.codigoAutorizacion = codigoAutorizacion;
        }

        public String getReferencia() { return referencia; }
        public String getEstado() { return estado; }
        public String getCodigoAutorizacion() { return codigoAutorizacion; }
    }
}
package ApiGymorEjecucion.Api.infrastructure.external.payment;


import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Manejador de webhooks de la pasarela de pago
 *
 * Procesa las notificaciones asíncronas de la pasarela
 */
@Component
public class WebhookPagoHandler {

    /**
     * Valida la firma del webhook para asegurar que viene de la pasarela
     *
     * @param payload Datos recibidos
     * @param signature Firma recibida en header
     * @return true si la firma es válida
     */
    public boolean validarFirma(Map<String, Object> payload, String signature) {
        // TODO: Implementar validación real según la pasarela
        // Ejemplo: HMAC-SHA256 con clave secreta

        if (signature == null || signature.isBlank()) {
            System.out.println("⚠️ Webhook sin firma - Rechazado");
            return false;
        }

        System.out.println("✅ MOCK: Firma de webhook validada");
        return true;
    }

    /**
     * Extrae el ID del pedido del payload del webhook
     */
    public String extraerPedidoId(Map<String, Object> payload) {
        // La estructura depende de la pasarela específica
        Object pedidoId = payload.get("order_id");
        if (pedidoId == null) {
            pedidoId = payload.get("external_reference");
        }

        return pedidoId != null ? pedidoId.toString() : null;
    }

    /**
     * Extrae el estado del pago del payload
     */
    public String extraerEstadoPago(Map<String, Object> payload) {
        Object estado = payload.get("status");
        return estado != null ? estado.toString() : "UNKNOWN";
    }

    /**
     * Extrae la referencia de pago de la pasarela
     */
    public String extraerReferenciaPago(Map<String, Object> payload) {
        Object referencia = payload.get("payment_id");
        if (referencia == null) {
            referencia = payload.get("transaction_id");
        }

        return referencia != null ? referencia.toString() : null;
    }

    /**
     * Extrae el motivo de rechazo si el pago falló
     */
    public String extraerMotivoRechazo(Map<String, Object> payload) {
        Object motivo = payload.get("status_detail");
        if (motivo == null) {
            motivo = payload.get("failure_reason");
        }

        return motivo != null ? motivo.toString() : "No especificado";
    }

    /**
     * Determina si el webhook indica un pago exitoso
     */
    public boolean esPagoExitoso(String estadoPago) {
        return "approved".equalsIgnoreCase(estadoPago)
                || "success".equalsIgnoreCase(estadoPago)
                || "paid".equalsIgnoreCase(estadoPago);
    }

    /**
     * Determina si el webhook indica un pago rechazado
     */
    public boolean esPagoRechazado(String estadoPago) {
        return "rejected".equalsIgnoreCase(estadoPago)
                || "failed".equalsIgnoreCase(estadoPago)
                || "declined".equalsIgnoreCase(estadoPago);
    }
}
package ApiGymorEjecucion.Api.application.dto.request.pago;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * DTO para solicitud de inicio de pago
 */
public class IniciarPagoRequest {

    @NotBlank(message = "El ID del pedido es obligatorio")
    private String pedidoId;

    /**
     * Método de pago permitido:
     * - TARJETA
     * - TRANSFERENCIA
     */
    @NotBlank(message = "El método de pago es obligatorio")
    @Pattern(
            regexp = "TARJETA|TRANSFERENCIA",
            message = "El método de pago debe ser TARJETA o TRANSFERENCIA"
    )
    private String metodoPago;

    // Constructors

    public IniciarPagoRequest() {
    }

    public IniciarPagoRequest(String pedidoId, String metodoPago) {
        this.pedidoId = pedidoId;
        this.metodoPago = metodoPago;
    }

    // Getters y Setters

    public String getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(String pedidoId) {
        this.pedidoId = pedidoId;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
}

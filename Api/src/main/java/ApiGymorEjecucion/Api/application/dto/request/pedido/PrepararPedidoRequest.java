package ApiGymorEjecucion.Api.application.dto.request.pedido;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para solicitud de preparación de pedido
 */
public class PrepararPedidoRequest {

    @NotBlank(message = "El ID del pedido es obligatorio")
    private String pedidoId;

    @NotBlank(message = "El ID del operador es obligatorio")
    private String operadorId;

    @Size(max = 500, message = "Las observaciones no pueden superar los 500 caracteres")
    private String observaciones;

    // Constructors

    public PrepararPedidoRequest() {
    }

    public PrepararPedidoRequest(String pedidoId, String operadorId, String observaciones) {
        this.pedidoId = pedidoId;
        this.operadorId = operadorId;
        this.observaciones = observaciones;
    }

    // Getters y Setters

    public String getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(String pedidoId) {
        this.pedidoId = pedidoId;
    }

    public String getOperadorId() {
        return operadorId;
    }

    public void setOperadorId(String operadorId) {
        this.operadorId = operadorId;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}

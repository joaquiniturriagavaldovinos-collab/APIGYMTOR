package ApiGymorEjecucion.Api.application.dto.request.pedido;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para solicitud de despacho de pedido
 */
public class DespacharPedidoRequest {

    @NotBlank(message = "El ID del pedido es obligatorio")
    private String pedidoId;

    @NotBlank(message = "La guía de despacho es obligatoria")
    @Size(max = 100, message = "La guía de despacho no puede superar los 100 caracteres")
    private String guiaDespacho;

    @NotBlank(message = "El transportista es obligatorio")
    @Size(max = 100, message = "El transportista no puede superar los 100 caracteres")
    private String transportista;

    // Constructors

    public DespacharPedidoRequest() {
    }

    public DespacharPedidoRequest(String pedidoId, String guiaDespacho, String transportista) {
        this.pedidoId = pedidoId;
        this.guiaDespacho = guiaDespacho;
        this.transportista = transportista;
    }

    // Getters y Setters

    public String getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(String pedidoId) {
        this.pedidoId = pedidoId;
    }

    public String getGuiaDespacho() {
        return guiaDespacho;
    }

    public void setGuiaDespacho(String guiaDespacho) {
        this.guiaDespacho = guiaDespacho;
    }

    public String getTransportista() {
        return transportista;
    }

    public void setTransportista(String transportista) {
        this.transportista = transportista;
    }
}

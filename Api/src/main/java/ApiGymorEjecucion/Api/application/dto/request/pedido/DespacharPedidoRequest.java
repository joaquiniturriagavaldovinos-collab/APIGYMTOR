package ApiGymorEjecucion.Api.application.dto.request.pedido;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request para despachar un pedido
 */
public class DespacharPedidoRequest {

    @NotBlank(message = "El ID del pedido es obligatorio")
    private String pedidoId;

    @NotBlank(message = "El número de guía es obligatorio")
    @Size(max = 50, message = "El número de guía no puede superar 50 caracteres")
    private String numeroGuia;

    @Size(max = 255, message = "La URL de tracking no puede superar 255 caracteres")
    private String urlTracking;

    @NotBlank(message = "El nombre del transportista es obligatorio")
    @Size(max = 100, message = "El nombre del transportista no puede superar 100 caracteres")
    private String nombreTransportista;

    @NotBlank(message = "El código del transportista es obligatorio")
    @Size(max = 20, message = "El código del transportista no puede superar 20 caracteres")
    private String codigoTransportista;

    @Size(max = 20, message = "El teléfono no puede superar 20 caracteres")
    private String telefonoTransportista;

    @NotBlank(message = "La dirección de entrega es obligatoria")
    @Size(max = 255, message = "La dirección no puede superar 255 caracteres")
    private String direccionEntrega;

    // Constructors
    public DespacharPedidoRequest() {
    }

    public DespacharPedidoRequest(
            String pedidoId,
            String numeroGuia,
            String urlTracking,
            String nombreTransportista,
            String codigoTransportista,
            String telefonoTransportista,
            String direccionEntrega
    ) {
        this.pedidoId = pedidoId;
        this.numeroGuia = numeroGuia;
        this.urlTracking = urlTracking;
        this.nombreTransportista = nombreTransportista;
        this.codigoTransportista = codigoTransportista;
        this.telefonoTransportista = telefonoTransportista;
        this.direccionEntrega = direccionEntrega;
    }

    // Getters y Setters
    public String getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(String pedidoId) {
        this.pedidoId = pedidoId;
    }

    public String getNumeroGuia() {
        return numeroGuia;
    }

    public void setNumeroGuia(String numeroGuia) {
        this.numeroGuia = numeroGuia;
    }

    public String getUrlTracking() {
        return urlTracking;
    }

    public void setUrlTracking(String urlTracking) {
        this.urlTracking = urlTracking;
    }

    public String getNombreTransportista() {
        return nombreTransportista;
    }

    public void setNombreTransportista(String nombreTransportista) {
        this.nombreTransportista = nombreTransportista;
    }

    public String getCodigoTransportista() {
        return codigoTransportista;
    }

    public void setCodigoTransportista(String codigoTransportista) {
        this.codigoTransportista = codigoTransportista;
    }

    public String getTelefonoTransportista() {
        return telefonoTransportista;
    }

    public void setTelefonoTransportista(String telefonoTransportista) {
        this.telefonoTransportista = telefonoTransportista;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }
}
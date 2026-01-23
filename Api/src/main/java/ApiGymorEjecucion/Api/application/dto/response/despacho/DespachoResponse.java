package ApiGymorEjecucion.Api.application.dto.response.despacho;

import java.time.LocalDateTime;

public class DespachoResponse {

    private String id;
    private String pedidoId;
    private String numeroGuia;
    private String urlTracking;
    private LocalDateTime fechaEmisionGuia;

    private String transportista;
    private String codigoTransportista;
    private String telefonoTransportista;

    private String direccionCompleta;

    private LocalDateTime fechaDespacho;
    private LocalDateTime fechaEntregaEstimada;
    private LocalDateTime fechaEntregaReal;

    private String observaciones;

    private boolean estaDespachado;
    private boolean estaEntregado;

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public LocalDateTime getFechaEmisionGuia() {
        return fechaEmisionGuia;
    }

    public void setFechaEmisionGuia(LocalDateTime fechaEmisionGuia) {
        this.fechaEmisionGuia = fechaEmisionGuia;
    }

    public String getTransportista() {
        return transportista;
    }

    public void setTransportista(String transportista) {
        this.transportista = transportista;
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

    public String getDireccionCompleta() {
        return direccionCompleta;
    }

    public void setDireccionCompleta(String direccionCompleta) {
        this.direccionCompleta = direccionCompleta;
    }

    public LocalDateTime getFechaDespacho() {
        return fechaDespacho;
    }

    public void setFechaDespacho(LocalDateTime fechaDespacho) {
        this.fechaDespacho = fechaDespacho;
    }

    public LocalDateTime getFechaEntregaEstimada() {
        return fechaEntregaEstimada;
    }

    public void setFechaEntregaEstimada(LocalDateTime fechaEntregaEstimada) {
        this.fechaEntregaEstimada = fechaEntregaEstimada;
    }

    public LocalDateTime getFechaEntregaReal() {
        return fechaEntregaReal;
    }

    public void setFechaEntregaReal(LocalDateTime fechaEntregaReal) {
        this.fechaEntregaReal = fechaEntregaReal;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public boolean isEstaDespachado() {
        return estaDespachado;
    }

    public void setEstaDespachado(boolean estaDespachado) {
        this.estaDespachado = estaDespachado;
    }

    public boolean isEstaEntregado() {
        return estaEntregado;
    }

    public void setEstaEntregado(boolean estaEntregado) {
        this.estaEntregado = estaEntregado;
    }
}
package ApiGymorEjecucion.Api.application.dto.response.despacho;

public class TrackingResponse {

    private String pedidoId;
    private String numeroGuia;
    private String urlTracking;
    private String estadoActual;
    private String descripcionEstado;
    private java.time.LocalDateTime fechaUltimaActualizacion;
    private boolean estaEntregado;

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

    public String getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(String estadoActual) {
        this.estadoActual = estadoActual;
    }

    public String getDescripcionEstado() {
        return descripcionEstado;
    }

    public void setDescripcionEstado(String descripcionEstado) {
        this.descripcionEstado = descripcionEstado;
    }

    public java.time.LocalDateTime getFechaUltimaActualizacion() {
        return fechaUltimaActualizacion;
    }

    public void setFechaUltimaActualizacion(
            java.time.LocalDateTime fechaUltimaActualizacion) {
        this.fechaUltimaActualizacion = fechaUltimaActualizacion;
    }

    public boolean isEstaEntregado() {
        return estaEntregado;
    }

    public void setEstaEntregado(boolean estaEntregado) {
        this.estaEntregado = estaEntregado;
    }
}

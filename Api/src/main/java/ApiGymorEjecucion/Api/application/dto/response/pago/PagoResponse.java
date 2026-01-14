package ApiGymorEjecucion.Api.application.dto.response.pago;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PagoResponse {
    private String id;
    private String pedidoId;
    private BigDecimal monto;
    private String metodoPago;
    private String metodoPagoDescripcion;
    private String estado;
    private String estadoDescripcion;
    private String referenciaPasarela;
    private String codigoAutorizacion;
    private String motivoRechazo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaProcesamiento;
    private LocalDateTime fechaConfirmacion;
    private boolean esExitoso;
    private boolean estaFinalizado;

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPedidoId() { return pedidoId; }
    public void setPedidoId(String pedidoId) { this.pedidoId = pedidoId; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public String getMetodoPagoDescripcion() { return metodoPagoDescripcion; }
    public void setMetodoPagoDescripcion(String metodoPagoDescripcion) {
        this.metodoPagoDescripcion = metodoPagoDescripcion;
    }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getEstadoDescripcion() { return estadoDescripcion; }
    public void setEstadoDescripcion(String estadoDescripcion) {
        this.estadoDescripcion = estadoDescripcion;
    }

    public String getReferenciaPasarela() { return referenciaPasarela; }
    public void setReferenciaPasarela(String referenciaPasarela) {
        this.referenciaPasarela = referenciaPasarela;
    }

    public String getCodigoAutorizacion() { return codigoAutorizacion; }
    public void setCodigoAutorizacion(String codigoAutorizacion) {
        this.codigoAutorizacion = codigoAutorizacion;
    }

    public String getMotivoRechazo() { return motivoRechazo; }
    public void setMotivoRechazo(String motivoRechazo) { this.motivoRechazo = motivoRechazo; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaProcesamiento() { return fechaProcesamiento; }
    public void setFechaProcesamiento(LocalDateTime fechaProcesamiento) {
        this.fechaProcesamiento = fechaProcesamiento;
    }

    public LocalDateTime getFechaConfirmacion() { return fechaConfirmacion; }
    public void setFechaConfirmacion(LocalDateTime fechaConfirmacion) {
        this.fechaConfirmacion = fechaConfirmacion;
    }

    public boolean isEsExitoso() { return esExitoso; }
    public void setEsExitoso(boolean esExitoso) { this.esExitoso = esExitoso; }

    public boolean isEstaFinalizado() { return estaFinalizado; }
    public void setEstaFinalizado(boolean estaFinalizado) { this.estaFinalizado = estaFinalizado; }
}

package ApiGymorEjecucion.Api.application.dto.response.pago;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PagoResponse {

    // Identificadores
    private String id;
    private String pedidoId;

    // Monto y método
    private BigDecimal monto;
    private String metodoPago;
    private String metodoPagoDescripcion;

    // Estado
    private String estado;
    private String estadoDescripcion;
    private boolean esExitoso;
    private boolean estaFinalizado;
    private boolean requiereAccionCliente; // Nuevo

    // Referencias externas
    private String referenciaPasarela;
    private String codigoAutorizacion;
    private String motivoRechazo;

    // URLs útiles
    private String urlPago; // Para redirigir al cliente
    private String urlComprobante; // PDF del comprobante

    // Fechas
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaProcesamiento;
    private LocalDateTime fechaConfirmacion;
    private LocalDateTime fechaExpiracion;

    // Metadata
    private String mensajeCliente;
    private String siguienteAccion;

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

    public boolean isEsExitoso() { return esExitoso; }
    public void setEsExitoso(boolean esExitoso) { this.esExitoso = esExitoso; }

    public boolean isEstaFinalizado() { return estaFinalizado; }
    public void setEstaFinalizado(boolean estaFinalizado) {
        this.estaFinalizado = estaFinalizado;
    }

    public boolean isRequiereAccionCliente() { return requiereAccionCliente; }
    public void setRequiereAccionCliente(boolean requiereAccionCliente) {
        this.requiereAccionCliente = requiereAccionCliente;
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
    public void setMotivoRechazo(String motivoRechazo) {
        this.motivoRechazo = motivoRechazo;
    }

    public String getUrlPago() { return urlPago; }
    public void setUrlPago(String urlPago) { this.urlPago = urlPago; }

    public String getUrlComprobante() { return urlComprobante; }
    public void setUrlComprobante(String urlComprobante) {
        this.urlComprobante = urlComprobante;
    }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaProcesamiento() { return fechaProcesamiento; }
    public void setFechaProcesamiento(LocalDateTime fechaProcesamiento) {
        this.fechaProcesamiento = fechaProcesamiento;
    }

    public LocalDateTime getFechaConfirmacion() { return fechaConfirmacion; }
    public void setFechaConfirmacion(LocalDateTime fechaConfirmacion) {
        this.fechaConfirmacion = fechaConfirmacion;
    }

    public LocalDateTime getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(LocalDateTime fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public String getMensajeCliente() { return mensajeCliente; }
    public void setMensajeCliente(String mensajeCliente) {
        this.mensajeCliente = mensajeCliente;
    }

    public String getSiguienteAccion() { return siguienteAccion; }
    public void setSiguienteAccion(String siguienteAccion) {
        this.siguienteAccion = siguienteAccion;
    }
}
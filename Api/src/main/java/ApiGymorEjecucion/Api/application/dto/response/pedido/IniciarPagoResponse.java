package ApiGymorEjecucion.Api.application.dto.response.pedido;

import ApiGymorEjecucion.Api.application.dto.response.pago.PagoResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response completo al iniciar el pago de un pedido.
 * Combina información del pedido actualizado y el pago creado.
 */
public class IniciarPagoResponse {

    // Información del pedido
    private PedidoResumen pedido;

    // Información del pago
    private PagoResumen pago;

    // Metadata útil
    private String mensajeCliente;
    private String siguientePaso;

    // Constructor
    public IniciarPagoResponse() {}

    public IniciarPagoResponse(PedidoResumen pedido, PagoResumen pago,
                               String mensajeCliente, String siguientePaso) {
        this.pedido = pedido;
        this.pago = pago;
        this.mensajeCliente = mensajeCliente;
        this.siguientePaso = siguientePaso;
    }

    // Inner classes para resumir información
    public static class PedidoResumen {
        private String id;
        private String estado;
        private String estadoDescripcion;
        private BigDecimal total;
        private LocalDateTime fechaActualizacion;

        // Getters y Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }

        public String getEstadoDescripcion() { return estadoDescripcion; }
        public void setEstadoDescripcion(String estadoDescripcion) {
            this.estadoDescripcion = estadoDescripcion;
        }

        public BigDecimal getTotal() { return total; }
        public void setTotal(BigDecimal total) { this.total = total; }

        public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
        public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
            this.fechaActualizacion = fechaActualizacion;
        }
    }

    public static class PagoResumen {
        private String id;
        private String metodoPago;
        private String metodoPagoDescripcion;
        private String estado;
        private String estadoDescripcion;
        private BigDecimal monto;
        private String referenciaPasarela;
        private String urlPago; // URL para redirigir al cliente
        private LocalDateTime fechaCreacion;
        private LocalDateTime fechaExpiracion; // Si aplica

        // Getters y Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

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

        public BigDecimal getMonto() { return monto; }
        public void setMonto(BigDecimal monto) { this.monto = monto; }

        public String getReferenciaPasarela() { return referenciaPasarela; }
        public void setReferenciaPasarela(String referenciaPasarela) {
            this.referenciaPasarela = referenciaPasarela;
        }

        public String getUrlPago() { return urlPago; }
        public void setUrlPago(String urlPago) { this.urlPago = urlPago; }

        public LocalDateTime getFechaCreacion() { return fechaCreacion; }
        public void setFechaCreacion(LocalDateTime fechaCreacion) {
            this.fechaCreacion = fechaCreacion;
        }

        public LocalDateTime getFechaExpiracion() { return fechaExpiracion; }
        public void setFechaExpiracion(LocalDateTime fechaExpiracion) {
            this.fechaExpiracion = fechaExpiracion;
        }
    }

    // Getters y Setters principales
    public PedidoResumen getPedido() { return pedido; }
    public void setPedido(PedidoResumen pedido) { this.pedido = pedido; }

    public PagoResumen getPago() { return pago; }
    public void setPago(PagoResumen pago) { this.pago = pago; }

    public String getMensajeCliente() { return mensajeCliente; }
    public void setMensajeCliente(String mensajeCliente) {
        this.mensajeCliente = mensajeCliente;
    }

    public String getSiguientePaso() { return siguientePaso; }
    public void setSiguientePaso(String siguientePaso) {
        this.siguientePaso = siguientePaso;
    }
}
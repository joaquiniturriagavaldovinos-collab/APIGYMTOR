package ApiGymorEjecucion.Api.application.dto.response.pedido;

import java.time.LocalDateTime;

/**
 * DTO de respuesta con información del estado de un pedido
 */
public class EstadoPedidoResponse {
    private String pedidoId;
    private String estado;
    private String estadoDescripcion;
    private LocalDateTime fechaActualizacion;
    private boolean esFinal;
    private String siguientePasoPermitido;

    // Constructors
    public EstadoPedidoResponse() {
    }

    public EstadoPedidoResponse(String pedidoId, String estado, String estadoDescripcion,
                                LocalDateTime fechaActualizacion, boolean esFinal,
                                String siguientePasoPermitido) {
        this.pedidoId = pedidoId;
        this.estado = estado;
        this.estadoDescripcion = estadoDescripcion;
        this.fechaActualizacion = fechaActualizacion;
        this.esFinal = esFinal;
        this.siguientePasoPermitido = siguientePasoPermitido;
    }

    // Getters y Setters
    public String getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(String pedidoId) {
        this.pedidoId = pedidoId;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstadoDescripcion() {
        return estadoDescripcion;
    }

    public void setEstadoDescripcion(String estadoDescripcion) {
        this.estadoDescripcion = estadoDescripcion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public boolean isEsFinal() {
        return esFinal;
    }

    public void setEsFinal(boolean esFinal) {
        this.esFinal = esFinal;
    }

    public String getSiguientePasoPermitido() {
        return siguientePasoPermitido;
    }

    public void setSiguientePasoPermitido(String siguientePasoPermitido) {
        this.siguientePasoPermitido = siguientePasoPermitido;
    }
}
package ApiGymorEjecucion.Api.application.dto.response.pedido;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Response completo de un pedido con información útil para el cliente
 */
@JsonInclude(JsonInclude.Include.NON_NULL) // No incluir campos null
public class PedidoResponse {

    // Información básica
    private String id;
    private String clienteId;

    // Estado
    private String estado;
    private String estadoDescripcion;
    private boolean esFinal;
    private boolean estaPagado;

    // Items y totales
    private List<ItemPedidoResponse> items;
    private int cantidadItems;
    private BigDecimal total;

    // Fechas
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    // Información de pago (solo si existe)
    private String referenciaPago;

    // Información de despacho (solo si existe)
    private String guiaDespacho;

    private String despachoId;


    // Acciones disponibles
    private List<String> accionesDisponibles;

    // Metadata útil
    private String siguientePaso;
    private String mensajeEstado;

    // Constructors
    public PedidoResponse() {}

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getEstadoDescripcion() { return estadoDescripcion; }
    public void setEstadoDescripcion(String estadoDescripcion) {
        this.estadoDescripcion = estadoDescripcion;
    }

    public boolean isEsFinal() { return esFinal; }
    public void setEsFinal(boolean esFinal) { this.esFinal = esFinal; }

    public boolean isEstaPagado() { return estaPagado; }
    public void setEstaPagado(boolean estaPagado) { this.estaPagado = estaPagado; }

    public List<ItemPedidoResponse> getItems() { return items; }
    public void setItems(List<ItemPedidoResponse> items) { this.items = items; }

    public int getCantidadItems() { return cantidadItems; }
    public void setCantidadItems(int cantidadItems) { this.cantidadItems = cantidadItems; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getReferenciaPago() { return referenciaPago; }
    public void setReferenciaPago(String referenciaPago) {
        this.referenciaPago = referenciaPago;
    }

    public String getGuiaDespacho() { return guiaDespacho; }
    public void setGuiaDespacho(String guiaDespacho) {
        this.guiaDespacho = guiaDespacho;
    }

    public List<String> getAccionesDisponibles() { return accionesDisponibles; }
    public void setAccionesDisponibles(List<String> accionesDisponibles) {
        this.accionesDisponibles = accionesDisponibles;
    }

    public String getSiguientePaso() { return siguientePaso; }
    public void setSiguientePaso(String siguientePaso) {
        this.siguientePaso = siguientePaso;
    }

    public String getMensajeEstado() { return mensajeEstado; }
    public void setMensajeEstado(String mensajeEstado) {
        this.mensajeEstado = mensajeEstado;
    }

    public String getDespachoId() {
        return despachoId;
    }

    public void setDespachoId(String despachoId) {
        this.despachoId = despachoId;
    }


}
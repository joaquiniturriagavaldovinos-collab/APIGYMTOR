package ApiGymorEjecucion.Api.domain.model.pedido;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Value Object: Registra una transición de estado en el pedido.
 * Útil para auditoría y trazabilidad.
 */
public class TransicionEstado {
    private final EstadoPedido estadoAnterior;
    private final EstadoPedido estadoNuevo;
    private final LocalDateTime fechaTransicion;
    private final String observacion;

    private TransicionEstado(EstadoPedido estadoAnterior, EstadoPedido estadoNuevo,
                             String observacion) {
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.fechaTransicion = LocalDateTime.now();
        this.observacion = observacion;
    }

    public static TransicionEstado crear(EstadoPedido estadoAnterior,
                                         EstadoPedido estadoNuevo) {
        return new TransicionEstado(estadoAnterior, estadoNuevo, null);
    }

    public static TransicionEstado crear(EstadoPedido estadoAnterior,
                                         EstadoPedido estadoNuevo,
                                         String observacion) {
        return new TransicionEstado(estadoAnterior, estadoNuevo, observacion);
    }

    // Getters
    public EstadoPedido getEstadoAnterior() {
        return estadoAnterior;
    }

    public EstadoPedido getEstadoNuevo() {
        return estadoNuevo;
    }

    public LocalDateTime getFechaTransicion() {
        return fechaTransicion;
    }

    public String getObservacion() {
        return observacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransicionEstado that = (TransicionEstado) o;
        return estadoAnterior == that.estadoAnterior &&
                estadoNuevo == that.estadoNuevo &&
                Objects.equals(fechaTransicion, that.fechaTransicion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(estadoAnterior, estadoNuevo, fechaTransicion);
    }

    @Override
    public String toString() {
        return String.format("%s -> %s (%s)",
                estadoAnterior.getDescripcion(),
                estadoNuevo.getDescripcion(),
                fechaTransicion);
    }
}

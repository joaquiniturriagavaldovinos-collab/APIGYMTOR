package ApiGymorEjecucion.Api.domain.model.Pago;


import ApiGymorEjecucion.Api.domain.exception.PagoInvalidoException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad: Pago
 * Representa una transacción de pago asociada a un pedido
 */
public class Pago {
    private final String id;
    private final String pedidoId;
    private final BigDecimal monto;
    private final MetodoPago metodoPago;
    private EstadoPago estado;
    private String referenciaPasarela;
    private String codigoAutorizacion;
    private String motivoRechazo;
    private final LocalDateTime fechaCreacion;
    private LocalDateTime fechaProcesamiento;
    private LocalDateTime fechaConfirmacion;


    // Constructor 1: Para lógica de negocio (crear nuevos pagos)
    private Pago(String id, String pedidoId, BigDecimal monto, MetodoPago metodoPago) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.estado = EstadoPago.PENDIENTE; // ← Siempre inicia en PENDIENTE
        this.fechaCreacion = LocalDateTime.now(); // ← Fecha actual
    }

    // Constructor 2: Para reconstrucción desde BD
    private Pago(String id, String pedidoId, BigDecimal monto, MetodoPago metodoPago,
                 EstadoPago estado, String referenciaPasarela, String codigoAutorizacion,
                 String motivoRechazo, LocalDateTime fechaCreacion,
                 LocalDateTime fechaProcesamiento, LocalDateTime fechaConfirmacion) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.estado = estado; // ← Estado persistido
        this.referenciaPasarela = referenciaPasarela;
        this.codigoAutorizacion = codigoAutorizacion;
        this.motivoRechazo = motivoRechazo;
        this.fechaCreacion = fechaCreacion; // ← Fecha persistida
        this.fechaProcesamiento = fechaProcesamiento;
        this.fechaConfirmacion = fechaConfirmacion;
    }

    public static Pago crear(String id, String pedidoId, BigDecimal monto, MetodoPago metodoPago) {
        validarDatosCreacion(id, pedidoId, monto, metodoPago);
        return new Pago(id, pedidoId, monto, metodoPago);
    }

    /**
     * Factory method: Reconstruye un pago desde la persistencia
     */
    public static Pago reconstruir(
            String id, String pedidoId, BigDecimal monto, MetodoPago metodoPago,
            EstadoPago estado, String referenciaPasarela, String codigoAutorizacion,
            String motivoRechazo, LocalDateTime fechaCreacion,
            LocalDateTime fechaProcesamiento, LocalDateTime fechaConfirmacion) {

        return new Pago(id, pedidoId, monto, metodoPago, estado,
                referenciaPasarela, codigoAutorizacion, motivoRechazo,
                fechaCreacion, fechaProcesamiento, fechaConfirmacion);
    }
    /**
     * Factory method: Crea un nuevo pago en estado PENDIENTE
     */

    private static void validarDatosCreacion(String id, String pedidoId,
                                             BigDecimal monto, MetodoPago metodoPago) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID del pago es requerido");
        }
        if (pedidoId == null || pedidoId.isBlank()) {
            throw new IllegalArgumentException("El ID del pedido es requerido");
        }
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }
        if (metodoPago == null) {
            throw new IllegalArgumentException("El método de pago es requerido");
        }
    }

    // ===== MÉTODOS DE NEGOCIO =====

    /**
     * Marca el pago como en procesamiento
     */
    public void iniciarProcesamiento(String referenciaPasarela) {
        if (this.estado != EstadoPago.PENDIENTE) {
            throw new PagoInvalidoException(
                    String.format("No se puede procesar un pago en estado %s", this.estado)
            );
        }

        this.estado = EstadoPago.PROCESANDO;
        this.referenciaPasarela = referenciaPasarela;
        this.fechaProcesamiento = LocalDateTime.now();
    }

    /**
     * Confirma el pago como exitoso
     */
    public void confirmarExitoso(String codigoAutorizacion) {
        System.out.println("\n🎯 Pago.confirmarExitoso() - INICIO");
        System.out.println("   ID del pago: " + this.id);
        System.out.println("   Estado actual: " + this.estado);
        System.out.println("   Código actual: " + this.codigoAutorizacion);
        System.out.println("   Código a asignar: " + codigoAutorizacion);

        if (this.estado != EstadoPago.PROCESANDO && this.estado != EstadoPago.PENDIENTE) {
            throw new PagoInvalidoException(
                    String.format("No se puede confirmar un pago en estado %s", this.estado)
            );
        }

        if (codigoAutorizacion == null || codigoAutorizacion.isBlank()) {
            throw new IllegalArgumentException("El código de autorización es requerido");
        }

        this.estado = EstadoPago.EXITOSO;
        this.codigoAutorizacion = codigoAutorizacion;
        this.fechaConfirmacion = LocalDateTime.now();

        System.out.println("\n   ✅ Asignación completada:");
        System.out.println("   Estado nuevo: " + this.estado);
        System.out.println("   Código nuevo: " + this.codigoAutorizacion);
        System.out.println("   Fecha confirmación: " + this.fechaConfirmacion);
        System.out.println("🎯 Pago.confirmarExitoso() - FIN\n");
    }

    /**
     * Marca el pago como rechazado
     */
    public void marcarRechazado(String motivo) {
        if (this.estado.esFinal()) {
            throw new PagoInvalidoException(
                    String.format("No se puede rechazar un pago en estado final %s", this.estado)
            );
        }

        this.estado = EstadoPago.RECHAZADO;
        this.motivoRechazo = motivo;
        this.fechaConfirmacion = LocalDateTime.now();
    }

    /**
     * Cancela el pago
     */
    public void cancelar(String motivo) {
        if (this.estado == EstadoPago.EXITOSO) {
            throw new PagoInvalidoException(
                    "No se puede cancelar un pago exitoso. Use reembolso en su lugar."
            );
        }

        if (this.estado.esFinal()) {
            throw new PagoInvalidoException(
                    String.format("No se puede cancelar un pago en estado final %s", this.estado)
            );
        }

        this.estado = EstadoPago.CANCELADO;
        this.motivoRechazo = motivo;
        this.fechaConfirmacion = LocalDateTime.now();
    }

    /**
     * Procesa un reembolso
     */
    public void reembolsar() {
        if (this.estado != EstadoPago.EXITOSO) {
            throw new PagoInvalidoException(
                    "Solo se pueden reembolsar pagos exitosos"
            );
        }

        this.estado = EstadoPago.REEMBOLSADO;
        this.fechaConfirmacion = LocalDateTime.now();
    }

    // ===== MÉTODOS DE CONSULTA =====

    public boolean esExitoso() {
        return this.estado == EstadoPago.EXITOSO;
    }

    public boolean estaFinalizado() {
        return this.estado.esFinal();
    }

    public boolean requierePasarelaExterna() {
        return this.metodoPago.requierePasarelaExterna();
    }

    // ===== GETTERS =====

    public String getId() {
        return id;
    }

    public String getPedidoId() {
        return pedidoId;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public EstadoPago getEstado() {
        return estado;
    }

    public String getReferenciaPasarela() {
        return referenciaPasarela;
    }

    public String getCodigoAutorizacion() {
        return codigoAutorizacion;
    }

    public String getMotivoRechazo() {
        return motivoRechazo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaProcesamiento() {
        return fechaProcesamiento;
    }

    public LocalDateTime getFechaConfirmacion() {
        return fechaConfirmacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pago pago = (Pago) o;
        return Objects.equals(id, pago.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Pago{id='%s', pedidoId='%s', estado=%s, monto=%s}",
                id, pedidoId, estado, monto);
    }
}
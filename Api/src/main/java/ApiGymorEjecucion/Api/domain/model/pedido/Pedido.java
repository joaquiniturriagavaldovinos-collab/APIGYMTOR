package ApiGymorEjecucion.Api.domain.model.pedido;

import ApiGymorEjecucion.Api.domain.exception.EstadoFinalException;
import ApiGymorEjecucion.Api.domain.exception.TransicionEstadoInvalidaException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * ENTIDAD CENTRAL DEL DOMINIO.
 * Representa un pedido de compra con su máquina de estados integrada.
 */
public class Pedido {
    private final String id;
    private final String clienteId;
    private EstadoPedido estado;
    private final List<ItemPedido> items;
    private final List<TransicionEstado> historialEstados;
    private final LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private String referenciaPago;
    private String guiaDespacho;
    private String despachoId;
    private BigDecimal total;

    // Constructor privado - usar factory method
    private Pedido(String id, String clienteId, List<ItemPedido> items) {
        this.id = id;
        this.clienteId = clienteId;
        this.estado = EstadoPedido.CREATED;
        this.items = new ArrayList<>(items);
        this.historialEstados = new ArrayList<>();
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
        this.total = calcularTotal();

        this.historialEstados.add(
                TransicionEstado.crear(null, EstadoPedido.CREATED, "Pedido creado")
        );
    }

    /**
     * Factory method: Crea un nuevo pedido en estado CREATED
     */
    public static Pedido crear(String id, String clienteId, List<ItemPedido> items) {
        validarDatosCreacion(id, clienteId, items);
        return new Pedido(id, clienteId, items);
    }

    private static void validarDatosCreacion(String id, String clienteId,
                                             List<ItemPedido> items) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID del pedido es requerido");
        }
        if (clienteId == null || clienteId.isBlank()) {
            throw new IllegalArgumentException("El ID del cliente es requerido");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("El pedido debe tener al menos un item");
        }
    }

    // ==========================================
    // MÁQUINA DE ESTADOS - CORE DEL NEGOCIO
    // ==========================================

    /**
     * Valida y ejecuta una transición de estado.
     */
    private void cambiarEstado(EstadoPedido nuevoEstado, String observacion) {
        // Validar que no sea estado final
        if (this.estado.esFinal()) {
            throw new EstadoFinalException(
                    String.format("El pedido está en estado final %s y no puede modificarse",
                            this.estado.getDescripcion())
            );
        }

        // Validar transición permitida
        if (!esTransicionValida(this.estado, nuevoEstado)) {
            throw new TransicionEstadoInvalidaException(
                    this.estado,
                    nuevoEstado,
                    String.format("Transición inválida: no se puede pasar de %s a %s",
                            this.estado.getDescripcion(),
                            nuevoEstado.getDescripcion())
            );
        }

        // Ejecutar transición
        EstadoPedido estadoAnterior = this.estado;
        this.estado = nuevoEstado;
        this.fechaActualizacion = LocalDateTime.now();

        // Registrar en historial
        registrarTransicion(estadoAnterior, nuevoEstado, observacion);
    }

    private void registrarTransicion(EstadoPedido estadoAnterior,
                                     EstadoPedido estadoNuevo,
                                     String observacion) {
        this.historialEstados.add(
                TransicionEstado.crear(estadoAnterior, estadoNuevo, observacion)
        );
    }

    /**
     * TABLA DE TRANSICIONES VÁLIDAS
     */
    private boolean esTransicionValida(EstadoPedido actual, EstadoPedido nuevo) {
        return switch (actual) {
            case CREATED -> nuevo == EstadoPedido.PAYMENT_PENDING;

            case PAYMENT_PENDING -> nuevo == EstadoPedido.PAID
                    || nuevo == EstadoPedido.FAILED;

            case PAID -> nuevo == EstadoPedido.PREPARING
                    || nuevo == EstadoPedido.CANCELLED;

            case PREPARING -> nuevo == EstadoPedido.DISPATCHED
                    || nuevo == EstadoPedido.CANCELLED;

            case DISPATCHED -> nuevo == EstadoPedido.DELIVERED;

            // Estados finales no permiten transiciones
            case DELIVERED, FAILED, CANCELLED -> false;
        };
    }

    // ==========================================
    // MÉTODOS DE NEGOCIO (Casos de Uso)
    // ==========================================

    /**
     * CU2: Iniciar Pago de Pedido
     * Transición: CREATED -> PAYMENT_PENDING
     */
    public void iniciarPago() {
        cambiarEstado(EstadoPedido.PAYMENT_PENDING, "Pago iniciado");
    }

    /**
     * CU3: Confirmar Pago Exitoso
     * Transición: PAYMENT_PENDING -> PAID
     */
    public void confirmarPago(String referenciaPago) {
        if (this.estado != EstadoPedido.PAYMENT_PENDING) {
            throw new TransicionEstadoInvalidaException(
                    this.estado,
                    EstadoPedido.PAID,
                    "Solo se puede confirmar el pago de pedidos pendientes de pago"
            );
        }

        this.estado = EstadoPedido.PAID;
        this.referenciaPago = referenciaPago;
        this.fechaActualizacion = LocalDateTime.now();

        registrarTransicion(
                EstadoPedido.PAYMENT_PENDING,
                EstadoPedido.PAID,
                "Pago confirmado exitosamente con referencia: " + referenciaPago
        );
    }

    /**
     * CU3: Marcar Pago Fallido
     * Transición: PAYMENT_PENDING -> FAILED
     */
    public void marcarPagoFallido(String motivo) {
        if (this.estado != EstadoPedido.PAYMENT_PENDING) {
            throw new TransicionEstadoInvalidaException(
                    this.estado,
                    EstadoPedido.FAILED,
                    "Solo se puede marcar como fallido pedidos pendientes de pago"
            );
        }

        this.estado = EstadoPedido.FAILED;
        this.fechaActualizacion = LocalDateTime.now();

        registrarTransicion(
                EstadoPedido.PAYMENT_PENDING,
                EstadoPedido.FAILED,
                "Pago rechazado: " + motivo
        );
    }

    /**
     * CU4: Preparar Pedido
     * Transición: PAID -> PREPARING
     */
    public void preparar() {
        cambiarEstado(EstadoPedido.PREPARING, "Pedido en preparación");
    }

    /**
     * CU5: Despachar Pedido
     * Transición: PREPARING -> DISPATCHED
     */
    public void despachar(String guiaDespacho) {
        if (guiaDespacho == null || guiaDespacho.isBlank()) {
            throw new IllegalArgumentException("La guía de despacho es requerida");
        }
        this.guiaDespacho = guiaDespacho;
        cambiarEstado(EstadoPedido.DISPATCHED, "Despachado con guía: " + guiaDespacho);
    }

    /**
     * CU6: Confirmar Entrega
     * Transición: DISPATCHED -> DELIVERED
     */
    public void confirmarEntrega() {
        cambiarEstado(EstadoPedido.DELIVERED, "Pedido entregado al cliente");
    }

    /**
     * Cancelar Pedido (solo en estados permitidos)
     */
    public void cancelar(String motivo) {
        if (!this.estado.permiteCanelacion() && this.estado != EstadoPedido.PREPARING) {
            throw new TransicionEstadoInvalidaException(
                    this.estado,
                    EstadoPedido.CANCELLED,
                    String.format("No se puede cancelar un pedido en estado %s",
                            this.estado.getDescripcion())
            );
        }
        cambiarEstado(EstadoPedido.CANCELLED, "Cancelado: " + motivo);
    }

    // ==========================================
    // CÁLCULOS DE NEGOCIO
    // ==========================================

    /**
     * Calcula el total del pedido sumando todos los items
     */
    public BigDecimal calcularTotal() {
        return items.stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Cuenta la cantidad total de items en el pedido
     */
    public int contarItems() {
        return items.stream()
                .mapToInt(ItemPedido::getCantidad)
                .sum();
    }


    public void asignarGuiaDespacho(String numeroGuia) {
        if (numeroGuia == null || numeroGuia.isBlank()) {
            throw new IllegalArgumentException("El número de guía es requerido");
        }
        this.guiaDespacho = numeroGuia;
    }

    // ==========================================
    // GETTERS (Sin setters - inmutabilidad)
    // ==========================================

    public String getId() {
        return id;
    }

    public String getClienteId() {
        return clienteId;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public BigDecimal getTotal() {
        return total;
    }

    /**
     * Retorna copia inmutable de los items
     */
    public List<ItemPedido> getItems() {
        return Collections.unmodifiableList(items);
    }

    /**
     * Retorna copia inmutable del historial
     */
    public List<TransicionEstado> getHistorialEstados() {
        return Collections.unmodifiableList(historialEstados);
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public String getReferenciaPago() {
        return referenciaPago;
    }

    public String getGuiaDespacho() {
        return guiaDespacho;
    }
    public String getDespachoId() {
        return despachoId;
    }
    // ==========================================
    // MÉTODOS DE CONSULTA
    // ==========================================

    /**
     * ✅ CORREGIDO: Verifica si el pedido está pagado
     */
    public boolean estaPagado() {
        return this.estado == EstadoPedido.PAID
                || this.estado == EstadoPedido.PREPARING
                || this.estado == EstadoPedido.DISPATCHED
                || this.estado == EstadoPedido.DELIVERED;
    }

    /**
     * ✅ NUEVO: Verifica si el pedido está en estado final
     */
    public boolean esFinal() {
        return this.estado.esFinal();
    }

    public boolean estaFinalizado() {
        return this.estado.esFinal();
    }

    public boolean puedePrepararse() {
        return this.estado == EstadoPedido.PAID;
    }

    public boolean puedeDespacharse() {
        return this.estado == EstadoPedido.PREPARING;
    }


    public void asignarDespacho(String despachoId, String numeroGuia) {
        if (despachoId == null || despachoId.isBlank()) {
            throw new IllegalArgumentException("El ID del despacho es requerido");
        }
        if (numeroGuia == null || numeroGuia.isBlank()) {
            throw new IllegalArgumentException("El número de guía es requerido");
        }
        this.despachoId = despachoId;
        this.guiaDespacho = numeroGuia;
    }


    // ==========================================
    // EQUALS, HASHCODE, TOSTRING
    // ==========================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return Objects.equals(id, pedido.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format(
                "Pedido{id='%s', estado=%s, total=%s, items=%d}",
                id, estado, total, items.size()
        );
    }
}
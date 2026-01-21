package ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.pedido;

public enum EstadoPedidoEntity {
    CREATED, PAYMENT_PENDING, PAID, PREPARING, DISPATCHED, DELIVERED, FAILED, CANCELLED
}
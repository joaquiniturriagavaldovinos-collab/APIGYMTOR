package ApiGymorEjecucion.Api.application.usecase.pedido;

import ApiGymorEjecucion.Api.application.dto.response.pedido.IniciarPagoResponse;
import ApiGymorEjecucion.Api.application.dto.response.pedido.IniciarPagoResponse.PedidoResumen;
import ApiGymorEjecucion.Api.application.dto.response.pedido.IniciarPagoResponse.PagoResumen;
import ApiGymorEjecucion.Api.domain.exception.PedidoNoEncontradoException;
import ApiGymorEjecucion.Api.domain.model.Pago.MetodoPago;
import ApiGymorEjecucion.Api.domain.model.Pago.Pago;
import ApiGymorEjecucion.Api.domain.model.pedido.Pedido;
import ApiGymorEjecucion.Api.domain.repository.PagoRepository;
import ApiGymorEjecucion.Api.domain.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * CU2: Iniciar Pago de Pedido
 *
 * Transición: CREATED -> PAYMENT_PENDING
 * Bloquea el pedido para cambios e inicia el proceso de pago.
 */
@Service
public class IniciarPagoPedidoUseCase {

    private final PedidoRepository pedidoRepository;
    private final PagoRepository pagoRepository;

    public IniciarPagoPedidoUseCase(
            PedidoRepository pedidoRepository,
            PagoRepository pagoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.pagoRepository = pagoRepository;
    }

    /**
     * Inicia el proceso de pago para un pedido
     *
     * @param pedidoId ID del pedido
     * @param metodoPagoStr Método de pago seleccionado
     * @return Response combinado con información del pedido y pago
     */
    @Transactional
    public IniciarPagoResponse ejecutar(String pedidoId, String metodoPagoStr) {

        // 1. Validar input
        if (pedidoId == null || pedidoId.isBlank()) {
            throw new IllegalArgumentException("El ID del pedido es requerido");
        }
        if (metodoPagoStr == null || metodoPagoStr.isBlank()) {
            throw new IllegalArgumentException("El método de pago es requerido");
        }

        // 2. Buscar pedido
        Pedido pedido = pedidoRepository.buscarPorId(pedidoId)
                .orElseThrow(() -> new PedidoNoEncontradoException(pedidoId));

        // 3. Validar que tenga un total válido
        BigDecimal total = pedido.getTotal();
        if (total == null || total.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("El pedido debe tener un monto válido");
        }

        // 4. Cambiar estado del pedido (dominio valida la transición)
        pedido.iniciarPago();

        // 5. Persistir pedido actualizado
        Pedido pedidoActualizado = pedidoRepository.guardar(pedido);

        // 6. Convertir string a enum
        MetodoPago metodoPago = MetodoPago.fromString(metodoPagoStr);

        // 7. Crear el registro de pago
        Pago pago = Pago.crear(
                UUID.randomUUID().toString(),
                pedidoId,
                total,
                metodoPago
        );

        // 8. Si requiere pasarela externa, iniciar procesamiento
        if (metodoPago.requierePasarelaExterna()) {
            String referencia = generarReferenciaPasarela();
            pago.iniciarProcesamiento(referencia);
        }

        // 9. Guardar pago en BD
        Pago pagoGuardado = pagoRepository.guardar(pago);

        // 10. Retornar response combinado
        return mapToResponse(pedidoActualizado, pagoGuardado);
    }

    /**
     * Genera una referencia única para la pasarela de pago
     */
    private String generarReferenciaPasarela() {
        return "REF-" + System.currentTimeMillis() + "-" +
                UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * Mapea el pedido y pago a un response combinado
     */
    private IniciarPagoResponse mapToResponse(Pedido pedido, Pago pago) {

        // Mapear pedido
        PedidoResumen pedidoResumen = new PedidoResumen();
        pedidoResumen.setId(pedido.getId());
        pedidoResumen.setEstado(pedido.getEstado().name());
        pedidoResumen.setEstadoDescripcion(pedido.getEstado().getDescripcion());
        pedidoResumen.setTotal(pedido.getTotal());
        pedidoResumen.setFechaActualizacion(pedido.getFechaActualizacion());

        // Mapear pago
        PagoResumen pagoResumen = new PagoResumen();
        pagoResumen.setId(pago.getId());
        pagoResumen.setMetodoPago(pago.getMetodoPago().name());
        pagoResumen.setMetodoPagoDescripcion(pago.getMetodoPago().getDescripcion());
        pagoResumen.setEstado(pago.getEstado().name());
        pagoResumen.setEstadoDescripcion(pago.getEstado().getDescripcion());
        pagoResumen.setMonto(pago.getMonto());
        pagoResumen.setReferenciaPasarela(pago.getReferenciaPasarela());
        pagoResumen.setFechaCreacion(pago.getFechaCreacion());

        // URL de pago (si requiere pasarela)
        if (pago.requierePasarelaExterna()) {
            String urlPago = generarUrlPasarela(
                    pago.getReferenciaPasarela(),
                    pago.getMetodoPago()
            );
            pagoResumen.setUrlPago(urlPago);
            pagoResumen.setFechaExpiracion(LocalDateTime.now().plusMinutes(30));
        }

        // Mensajes útiles
        String mensaje = generarMensajeCliente(pago);
        String siguientePaso = generarSiguientePaso(pago);

        return new IniciarPagoResponse(pedidoResumen, pagoResumen, mensaje, siguientePaso);
    }

    /**
     * Genera la URL de la pasarela de pago según el método
     */
    private String generarUrlPasarela(String referencia, MetodoPago metodo) {
        return switch (metodo) {
            case WEBPAY -> "https://webpay.transbank.cl/pagar?ref=" + referencia;
            case MERCADO_PAGO -> "https://mercadopago.cl/checkout/" + referencia;
            case TARJETA_CREDITO, TARJETA_DEBITO ->
                    "https://pasarela.gymor.cl/pagar?ref=" + referencia;
            default -> null;
        };
    }

    /**
     * Genera un mensaje informativo para el cliente
     */
    private String generarMensajeCliente(Pago pago) {
        if (pago.requierePasarelaExterna()) {
            return "Serás redirigido a la pasarela de pago para completar la transacción.";
        } else if (pago.getMetodoPago() == MetodoPago.TRANSFERENCIA_BANCARIA) {
            return "Realiza la transferencia bancaria y envía el comprobante.";
        } else if (pago.getMetodoPago() == MetodoPago.EFECTIVO) {
            return "Prepara el monto en efectivo para pagar al recibir tu pedido.";
        } else {
            return "Pago registrado. Procederemos con la confirmación.";
        }
    }

    /**
     * Indica al cliente cuál es el siguiente paso
     */
    private String generarSiguientePaso(Pago pago) {
        if (pago.requierePasarelaExterna()) {
            return "Accede a la URL de pago y completa la transacción en los próximos 30 minutos.";
        } else if (pago.getMetodoPago() == MetodoPago.TRANSFERENCIA_BANCARIA) {
            return "Realiza la transferencia a la cuenta indicada y sube el comprobante.";
        } else if (pago.getMetodoPago() == MetodoPago.EFECTIVO) {
            return "Espera la confirmación de tu pedido. Pagarás al recibir.";
        } else {
            return "Espera la confirmación del pago por parte del administrador.";
        }
    }
}
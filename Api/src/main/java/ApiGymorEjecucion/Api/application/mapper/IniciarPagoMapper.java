package ApiGymorEjecucion.Api.application.mapper;


import ApiGymorEjecucion.Api.application.dto.response.pedido.IniciarPagoResponse;
import ApiGymorEjecucion.Api.application.dto.response.pedido.IniciarPagoResponse.PedidoResumen;
import ApiGymorEjecucion.Api.application.dto.response.pedido.IniciarPagoResponse.PagoResumen;
import ApiGymorEjecucion.Api.domain.model.Pago.MetodoPago;
import ApiGymorEjecucion.Api.domain.model.Pago.Pago;
import ApiGymorEjecucion.Api.domain.model.pedido.Pedido;

import java.time.LocalDateTime;

public class IniciarPagoMapper {

    public static IniciarPagoResponse toResponse(Pedido pedido, Pago pago) {

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
            String urlPago = generarUrlPasarela(pago.getReferenciaPasarela(), pago.getMetodoPago());
            pagoResumen.setUrlPago(urlPago);
            pagoResumen.setFechaExpiracion(LocalDateTime.now().plusMinutes(30)); // 30 min
        }

        // Mensajes útiles
        String mensaje = generarMensajeCliente(pago);
        String siguientePaso = generarSiguientePaso(pago);

        return new IniciarPagoResponse(pedidoResumen, pagoResumen, mensaje, siguientePaso);
    }

    private static String generarUrlPasarela(String referencia, MetodoPago metodo) {
        return switch (metodo) {
            case WEBPAY -> "https://webpay.transbank.cl/pagar?ref=" + referencia;
            case MERCADO_PAGO -> "https://mercadopago.cl/checkout/" + referencia;
            case TARJETA_CREDITO, TARJETA_DEBITO ->
                    "https://pasarela.gymor.cl/pagar?ref=" + referencia;
            default -> null;
        };
    }

    private static String generarMensajeCliente(Pago pago) {
        if (pago.requierePasarelaExterna()) {
            return "Serás redirigido a la pasarela de pago para completar la transacción.";
        } else {
            return "Pago registrado. Procederemos con la confirmación manual.";
        }
    }

    private static String generarSiguientePaso(Pago pago) {
        if (pago.requierePasarelaExterna()) {
            return "Accede a la URL de pago y completa la transacción.";
        } else {
            return "Espera la confirmación del pago por parte del administrador.";
        }
    }
}

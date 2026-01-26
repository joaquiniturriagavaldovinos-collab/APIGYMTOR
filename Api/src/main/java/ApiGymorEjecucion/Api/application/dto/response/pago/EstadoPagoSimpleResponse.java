package ApiGymorEjecucion.Api.application.dto.response.pago;

public record EstadoPagoSimpleResponse(
        String pagoId,
        String estado,
        String estadoDescripcion,
        boolean esExitoso,
        boolean estaFinalizado
) {}
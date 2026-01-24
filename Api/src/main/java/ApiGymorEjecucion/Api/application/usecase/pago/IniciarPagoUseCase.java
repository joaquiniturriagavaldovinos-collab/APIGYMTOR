package ApiGymorEjecucion.Api.application.usecase.pago;

import ApiGymorEjecucion.Api.application.dto.response.pago.PagoResponse;
import ApiGymorEjecucion.Api.application.mapper.PagoMapper;
import ApiGymorEjecucion.Api.domain.model.Pago.MetodoPago;
import ApiGymorEjecucion.Api.domain.model.Pago.Pago;
import ApiGymorEjecucion.Api.domain.model.pedido.Pedido;
import ApiGymorEjecucion.Api.domain.repository.PagoRepository;
import ApiGymorEjecucion.Api.domain.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class IniciarPagoUseCase {

    private final PagoRepository pagoRepository;
    private final PedidoRepository pedidoRepository;

    public IniciarPagoUseCase(
            PagoRepository pagoRepository,
            PedidoRepository pedidoRepository) {
        this.pagoRepository = pagoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    // Sobrecarga 1: Con monto explícito (llamado desde IniciarPagoPedidoUseCase)
    public PagoResponse ejecutar(String pedidoId, BigDecimal monto, MetodoPago metodo) {
        return crearPago(pedidoId, monto, metodo);
    }

    // Sobrecarga 2: Sin monto (lo calcula desde el pedido)
    public PagoResponse ejecutar(String pedidoId, MetodoPago metodo) {
        Pedido pedido = pedidoRepository.buscarPorId(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el pedido con ID: " + pedidoId
                ));

        return crearPago(pedidoId, pedido.getTotal(), metodo);
    }

    // Sobrecarga 3: Retorna el dominio (usado por IniciarPagoPedidoUseCase)
    public Pago ejecutarYRetornarDominio(
            String pedidoId,
            BigDecimal monto,
            MetodoPago metodo) {

        // Validaciones
        validarDatos(pedidoId, monto, metodo);

        // Crear pago
        Pago pago = Pago.crear(
                UUID.randomUUID().toString(),
                pedidoId,
                monto,
                metodo
        );

        // Si requiere pasarela externa, iniciar procesamiento
        if (metodo.requierePasarelaExterna()) {
            pago.iniciarProcesamiento(generarReferenciaPasarela());
        }

        // Guardar y retornar
        return pagoRepository.guardar(pago);
    }

    // ========== MÉTODOS PRIVADOS ==========

    private PagoResponse crearPago(String pedidoId, BigDecimal monto, MetodoPago metodo) {
        // Validaciones
        validarDatos(pedidoId, monto, metodo);

        // Crear pago
        Pago pago = Pago.crear(
                UUID.randomUUID().toString(),
                pedidoId,
                monto,
                metodo
        );

        // Si requiere pasarela externa, iniciar procesamiento
        if (metodo.requierePasarelaExterna()) {
            String referencia = generarReferenciaPasarela();
            pago.iniciarProcesamiento(referencia);
        }

        // Guardar
        Pago saved = pagoRepository.guardar(pago);

        // ✅ Usar mapper centralizado
        return PagoMapper.toResponse(saved);
    }

    private void validarDatos(String pedidoId, BigDecimal monto, MetodoPago metodo) {
        if (pedidoId == null || pedidoId.isBlank()) {
            throw new IllegalArgumentException("El ID del pedido es requerido");
        }
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }
        if (metodo == null) {
            throw new IllegalArgumentException("El método de pago es requerido");
        }
    }

    private String generarReferenciaPasarela() {
        return "REF-" + System.currentTimeMillis() + "-" +
                UUID.randomUUID().toString().substring(0, 8);
    }


}
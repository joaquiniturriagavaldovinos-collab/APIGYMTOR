package ApiGymorEjecucion.Api.domain.service;



import ApiGymorEjecucion.Api.domain.model.Cliente.TipoCliente;
import ApiGymorEjecucion.Api.domain.model.pedido.ItemPedido;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Servicio de Dominio: Calculador de Precios
 *
 * Centraliza la lógica de cálculo de precios con descuentos,
 * impuestos y promociones.
 */
public class CalculadorPrecioService {

    private static final BigDecimal DESCUENTO_MAYORISTA = new BigDecimal("0.15"); // 15%
    private static final BigDecimal IVA = new BigDecimal("0.19"); // 19%
    private static final int CANTIDAD_DESCUENTO_VOLUMEN = 10;
    private static final BigDecimal DESCUENTO_VOLUMEN = new BigDecimal("0.05"); // 5%

    /**
     * Calcula el subtotal de una lista de items
     */
    public BigDecimal calcularSubtotal(List<ItemPedido> items) {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return items.stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calcula descuento para cliente mayorista
     */
    public BigDecimal calcularDescuentoMayorista(BigDecimal subtotal, TipoCliente tipoCliente) {
        if (tipoCliente == null || tipoCliente != TipoCliente.MAYORISTA) {
            return BigDecimal.ZERO;
        }

        return subtotal.multiply(DESCUENTO_MAYORISTA)
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calcula descuento por volumen (cantidad de items)
     */
    public BigDecimal calcularDescuentoVolumen(List<ItemPedido> items, BigDecimal subtotal) {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }

        int cantidadTotal = items.stream()
                .mapToInt(ItemPedido::getCantidad)
                .sum();

        if (cantidadTotal >= CANTIDAD_DESCUENTO_VOLUMEN) {
            return subtotal.multiply(DESCUENTO_VOLUMEN)
                    .setScale(2, RoundingMode.HALF_UP);
        }

        return BigDecimal.ZERO;
    }

    /**
     * Calcula el IVA sobre un monto
     */
    public BigDecimal calcularIVA(BigDecimal monto) {
        if (monto == null) {
            return BigDecimal.ZERO;
        }

        return monto.multiply(IVA)
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calcula el total final con todos los descuentos e impuestos
     */
    public BigDecimal calcularTotalFinal(List<ItemPedido> items, TipoCliente tipoCliente) {
        BigDecimal subtotal = calcularSubtotal(items);
        BigDecimal descuentoMayorista = calcularDescuentoMayorista(subtotal, tipoCliente);
        BigDecimal descuentoVolumen = calcularDescuentoVolumen(items, subtotal);

        BigDecimal subtotalConDescuentos = subtotal
                .subtract(descuentoMayorista)
                .subtract(descuentoVolumen);

        BigDecimal iva = calcularIVA(subtotalConDescuentos);

        return subtotalConDescuentos.add(iva)
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calcula el precio unitario con descuento mayorista aplicado
     */
    public BigDecimal calcularPrecioUnitarioConDescuento(BigDecimal precioBase, TipoCliente tipoCliente) {
        if (tipoCliente != TipoCliente.MAYORISTA) {
            return precioBase;
        }

        BigDecimal descuento = precioBase.multiply(DESCUENTO_MAYORISTA);
        return precioBase.subtract(descuento)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
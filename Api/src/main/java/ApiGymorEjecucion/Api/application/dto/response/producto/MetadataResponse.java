package ApiGymorEjecucion.Api.application.dto.response.producto;

import ApiGymorEjecucion.Api.domain.model.producto.Producto;

import java.math.BigDecimal;

public class MetadataResponse {
    private boolean disponibleParaVenta;
    private boolean requiereDespachoEspecial;
    private boolean esFabricadoPorGymor;
    private BigDecimal valorInventario;
    private BigDecimal valorStockDisponible;

    public static MetadataResponse fromDomain(Producto producto) {
        MetadataResponse response = new MetadataResponse();
        response.disponibleParaVenta = producto.estaDisponibleParaVenta();
        response.requiereDespachoEspecial = producto.requiereDespachoEspecial();
        response.esFabricadoPorGymor = producto.esFabricadoPorGymor();
        response.valorInventario = producto.calcularValorInventario();
        response.valorStockDisponible = producto.calcularValorStockDisponible();
        return response;
    }

    // Getters y Setters
    public boolean isDisponibleParaVenta() { return disponibleParaVenta; }
    public void setDisponibleParaVenta(boolean disponibleParaVenta) {
        this.disponibleParaVenta = disponibleParaVenta;
    }
    public boolean isRequiereDespachoEspecial() { return requiereDespachoEspecial; }
    public void setRequiereDespachoEspecial(boolean requiereDespachoEspecial) {
        this.requiereDespachoEspecial = requiereDespachoEspecial;
    }
    public boolean isEsFabricadoPorGymor() { return esFabricadoPorGymor; }
    public void setEsFabricadoPorGymor(boolean esFabricadoPorGymor) {
        this.esFabricadoPorGymor = esFabricadoPorGymor;
    }
    public BigDecimal getValorInventario() { return valorInventario; }
    public void setValorInventario(BigDecimal valorInventario) {
        this.valorInventario = valorInventario;
    }
    public BigDecimal getValorStockDisponible() { return valorStockDisponible; }
    public void setValorStockDisponible(BigDecimal valorStockDisponible) {
        this.valorStockDisponible = valorStockDisponible;
    }
}
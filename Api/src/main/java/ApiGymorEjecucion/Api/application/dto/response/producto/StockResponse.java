package ApiGymorEjecucion.Api.application.dto.response.producto;

import ApiGymorEjecucion.Api.domain.model.producto.Stock;

public class StockResponse {
    private int cantidad;
    private int cantidadReservada;
    private int cantidadDisponible;
    private double porcentajeDisponible;
    private boolean agotado;
    private boolean stockBajo;
    private boolean necesitaReposicion;

    public static StockResponse fromDomain(Stock stock) {
        StockResponse response = new StockResponse();
        response.cantidad = stock.getCantidad();
        response.cantidadReservada = stock.getCantidadReservada();
        response.cantidadDisponible = stock.getCantidadDisponible();
        response.porcentajeDisponible = stock.calcularPorcentajeDisponible();
        response.agotado = stock.estaAgotado();
        response.stockBajo = stock.esStockCritico();
        response.necesitaReposicion = stock.necesitaReposicion();
        return response;
    }

    // Getters y Setters
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public int getCantidadReservada() { return cantidadReservada; }
    public void setCantidadReservada(int cantidadReservada) {
        this.cantidadReservada = cantidadReservada;
    }
    public int getCantidadDisponible() { return cantidadDisponible; }
    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }
    public double getPorcentajeDisponible() { return porcentajeDisponible; }
    public void setPorcentajeDisponible(double porcentajeDisponible) {
        this.porcentajeDisponible = porcentajeDisponible;
    }
    public boolean isAgotado() { return agotado; }
    public void setAgotado(boolean agotado) { this.agotado = agotado; }
    public boolean isStockBajo() { return stockBajo; }
    public void setStockBajo(boolean stockBajo) { this.stockBajo = stockBajo; }
    public boolean isNecesitaReposicion() { return necesitaReposicion; }
    public void setNecesitaReposicion(boolean necesitaReposicion) {
        this.necesitaReposicion = necesitaReposicion;
    }
}
package ApiGymorEjecucion.Api.application.dto.request.pago;

import java.math.BigDecimal;

public class ReembolsoRequest {
    private BigDecimal monto;
    private String motivo;

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}
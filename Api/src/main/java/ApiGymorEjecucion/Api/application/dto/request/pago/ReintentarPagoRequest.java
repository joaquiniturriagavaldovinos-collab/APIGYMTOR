package ApiGymorEjecucion.Api.application.dto.request.pago;

import jakarta.validation.constraints.NotBlank;

public class ReintentarPagoRequest {


    @NotBlank(message = "El ide del pago original es requerido :) por fa hermano")
    private String pagoIdOriginal;

    public String getPagoIdOriginal(){
        return pagoIdOriginal;
    }

    public void setPagoIdOriginal(String pagoIdOriginal){
        this.pagoIdOriginal = pagoIdOriginal;
    }




}

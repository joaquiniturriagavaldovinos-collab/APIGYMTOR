package ApiGymorEjecucion.Api.domain.model.servicio;


/**
 * Modalidades de clases de entrenamiento
 */
public enum ModalidadClase {
    PRESENCIAL("Presencial"),
    ONLINE("Online"),
    HIBRIDO("Híbrido");

    private final String descripcion;

    ModalidadClase(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean permiteAsistenciaRemota() {
        return this == ONLINE || this == HIBRIDO;
    }

    public boolean requiereEspacioFisico() {
        return this == PRESENCIAL || this == HIBRIDO;
    }
}
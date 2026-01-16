package ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity;

import ApiGymorEjecucion.Api.domain.model.servicio.ModalidadClase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "servicios")
public class ServicioJpaEntity {

    @Id
    private String id;

    private String nombre;
    private String descripcion;

    @Enumerated(EnumType.STRING)
    private ModalidadClase modalidad;

    private BigDecimal precioSesion;
    private int duracionMinutos;
    private int capacidadMaxima;
    private boolean activo;

    private LocalDateTime fechaCreacion;
}


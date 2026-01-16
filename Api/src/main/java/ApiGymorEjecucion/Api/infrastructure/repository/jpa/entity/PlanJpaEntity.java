package ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "planes")
public class PlanJpaEntity {

    @Id
    private String id;

    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private int duracionMeses;
    private boolean activo;
    private int sesionesIncluidas;
    private LocalDateTime fechaCreacion;

}

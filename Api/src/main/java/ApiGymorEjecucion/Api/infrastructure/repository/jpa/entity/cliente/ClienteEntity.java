package ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.cliente;

import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.cliente.embeddable.DireccionEntregaEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clientes")
public class ClienteEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true)
    private String email;

    private String telefono;

    @Column(nullable = false, unique = true)
    private String rut;

    @Column(name = "tipo", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoClienteEntity tipo;

    @Column(nullable = false)
    private boolean activo = true;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    // Lista de direcciones
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "cliente_direcciones",
            joinColumns = @JoinColumn(name = "cliente_id"))
    private List<DireccionEntregaEntity> direcciones = new ArrayList<>();

    // Dirección principal embebida
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "calle", column = @Column(name = "dir_principal_calle")),
            @AttributeOverride(name = "numero", column = @Column(name = "dir_principal_numero")),
            @AttributeOverride(name = "comuna", column = @Column(name = "dir_principal_comuna")),
            @AttributeOverride(name = "ciudad", column = @Column(name = "dir_principal_ciudad")),
            @AttributeOverride(name = "region", column = @Column(name = "dir_principal_region")),
            @AttributeOverride(name = "codigoPostal", column = @Column(name = "dir_principal_codigo_postal")),
            @AttributeOverride(name = "referencia", column = @Column(name = "dir_principal_referencia"))
    })
    private DireccionEntregaEntity direccionPrincipal;

    // Constructores
    public ClienteEntity() {}

    public ClienteEntity(String id, String nombre, String apellido, String email,
                         String telefono, String rut, TipoClienteEntity tipo,
                         LocalDateTime fechaRegistro) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.rut = rut;
        this.tipo = tipo;
        this.fechaRegistro = fechaRegistro;
        this.activo = true;
    }



    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }

    public TipoClienteEntity getTipo() { return tipo; }
    public void setTipo(TipoClienteEntity tipo) { this.tipo = tipo; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public List<DireccionEntregaEntity> getDirecciones() { return direcciones; }
    public void setDirecciones(List<DireccionEntregaEntity> direcciones) {
        this.direcciones = direcciones;
    }

    public DireccionEntregaEntity getDireccionPrincipal() { return direccionPrincipal; }
    public void setDireccionPrincipal(DireccionEntregaEntity direccionPrincipal) {
        this.direccionPrincipal = direccionPrincipal;
    }


    // Value Object embebido

}
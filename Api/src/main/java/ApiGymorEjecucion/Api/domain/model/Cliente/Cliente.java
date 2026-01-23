package ApiGymorEjecucion.Api.domain.model.Cliente;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Entidad: Cliente
 * Representa un cliente del sistema (minorista o mayorista)
 */
public class Cliente {
    private final String id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String rut;
    private TipoCliente tipo;
    private final List<DireccionEntrega> direcciones;
    private DireccionEntrega direccionPrincipal;
    private final LocalDateTime fechaRegistro;
    private boolean activo;

    private Cliente(String id, String nombre, String apellido, String email,
                    String telefono, String rut, TipoCliente tipo) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.rut = rut;
        this.tipo = tipo;
        this.direcciones = new ArrayList<>();
        this.fechaRegistro = LocalDateTime.now();
        this.activo = true;
    }

    public static Cliente crear(String id, String nombre, String apellido,
                                String email, String telefono, String rut,
                                TipoCliente tipo) {

        if (id == null || id.isBlank()) {
            id = "CLI-" + System.currentTimeMillis();
        }
        validarDatos(id, nombre, apellido, email, rut);
        return new Cliente(id, nombre, apellido, email, telefono, rut, tipo);
    }

    private static void validarDatos(String id, String nombre, String apellido,
                                     String email, String rut) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID del cliente es requerido");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es requerido");
        }
        if (apellido == null || apellido.isBlank()) {
            throw new IllegalArgumentException("El apellido es requerido");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email es requerido");
        }
        if (rut == null || rut.isBlank()) {
            throw new IllegalArgumentException("El RUT es requerido");
        }
    }

    // Métodos de negocio
    public void agregarDireccion(DireccionEntrega direccion) {
        if (direccion == null) {
            throw new IllegalArgumentException("La dirección no puede ser nula");
        }
        this.direcciones.add(direccion);

        // Si es la primera dirección, la marca como principal
        if (this.direccionPrincipal == null) {
            this.direccionPrincipal = direccion;
        }
    }

    public void establecerDireccionPrincipal(DireccionEntrega direccion) {
        if (!this.direcciones.contains(direccion)) {
            throw new IllegalArgumentException("La dirección no está registrada para este cliente");
        }
        this.direccionPrincipal = direccion;
    }

    public void desactivar() {
        this.activo = false;
    }

    public void activar() {
        this.activo = true;
    }

    public void cambiarTipo(TipoCliente nuevoTipo) {
        this.tipo = nuevoTipo;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getRut() {
        return rut;
    }

    public TipoCliente getTipo() {
        return tipo;
    }

    public List<DireccionEntrega> getDirecciones() {
        return Collections.unmodifiableList(direcciones);
    }

    public DireccionEntrega getDireccionPrincipal() {
        return direccionPrincipal;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public boolean isActivo() {
        return activo;
    }

    public void actualizarDatosPersonales(String nombre, String apellido, String telefono) {

        if (!this.activo) {
            throw new IllegalStateException("No se puede modificar un cliente inactivo");
        }

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        if (apellido == null || apellido.isBlank()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío");
        }

        if (telefono == null || telefono.isBlank()) {
            throw new IllegalArgumentException("El teléfono no puede estar vacío");
        }

        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(id, cliente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Cliente{id='%s', nombre='%s', tipo=%s}",
                id, getNombreCompleto(), tipo);
    }
}
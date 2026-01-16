package ApiGymorEjecucion.Api.domain.model.usuario;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
@Getter
@Setter

/**
 * Entidad: Usuario
 * Representa un usuario del sistema con roles y permisos
 */
public class Usuario {
    private final String id;
    private String email;
    private String nombre;
    private String apellido;
    private String passwordHash; // NUNCA guardar password en texto plano
    private final Set<Rol> roles;
    private boolean activo;
    private final LocalDateTime fechaCreacion;
    private LocalDateTime ultimoAcceso;

    private Usuario(String id, String email, String nombre, String apellido, String passwordHash) {
        this.id = id;
        this.email = email;
        this.nombre = nombre;
        this.apellido = apellido;
        this.passwordHash = passwordHash;
        this.roles = new HashSet<>();
        this.activo = true;
        this.fechaCreacion = LocalDateTime.now();
    }

    public static Usuario crear(String id, String email, String nombre,
                                String apellido, String passwordHash) {
        validarDatos(id, email, nombre, apellido, passwordHash);
        return new Usuario(id, email, nombre, apellido, passwordHash);
    }

    private static void validarDatos(String id, String email, String nombre,
                                     String apellido, String passwordHash) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID es requerido");
        }
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("Email inválido");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es requerido");
        }
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException("El password hash es requerido");
        }
    }

    // Métodos de negocio
    public void agregarRol(Rol rol) {
        if (rol == null) {
            throw new IllegalArgumentException("El rol no puede ser nulo");
        }
        this.roles.add(rol);
    }

    public void removerRol(Rol rol) {
        this.roles.remove(rol);
    }

    public boolean tieneRol(Rol rol) {
        return this.roles.contains(rol);
    }

    public boolean tienePermiso(Permiso permiso) {
        return roles.stream()
                .anyMatch(rol -> rol.tienePermiso(permiso));
    }

    public void desactivar() {
        this.activo = false;
    }

    public void activar() {
        this.activo = true;
    }

    public void registrarAcceso() {
        this.ultimoAcceso = LocalDateTime.now();
    }

    public void cambiarPassword(String nuevoPasswordHash) {
        if (nuevoPasswordHash == null || nuevoPasswordHash.isBlank()) {
            throw new IllegalArgumentException("El password hash no puede estar vacío");
        }
        this.passwordHash = nuevoPasswordHash;
    }

    // Getters
    public String getId() { return id; }
    public String getEmail() { return email; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getPasswordHash() { return passwordHash; }
    public Set<Rol> getRoles() { return new HashSet<>(roles); }
    public boolean isActivo() { return activo; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public LocalDateTime getUltimoAcceso() { return ultimoAcceso; }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
package ApiGymorEjecucion.Api.domain.model.usuario;


import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Entidad: Rol
 * Agrupa permisos para asignar a usuarios
 */
public class Rol {
    private final String id;
    private String nombre;
    private String descripcion;
    private final Set<Permiso> permisos;

    private Rol(String id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.permisos = new HashSet<>();
    }

    public static Rol crear(String id, String nombre, String descripcion) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID es requerido");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es requerido");
        }
        return new Rol(id, nombre, descripcion);
    }

    public void agregarPermiso(Permiso permiso) {
        if (permiso == null) {
            throw new IllegalArgumentException("El permiso no puede ser nulo");
        }
        this.permisos.add(permiso);
    }

    public void removerPermiso(Permiso permiso) {
        this.permisos.remove(permiso);
    }

    public boolean tienePermiso(Permiso permiso) {
        return this.permisos.contains(permiso);
    }

    // Getters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public Set<Permiso> getPermisos() { return new HashSet<>(permisos); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rol rol = (Rol) o;
        return Objects.equals(id, rol.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
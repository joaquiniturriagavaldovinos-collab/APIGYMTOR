package ApiGymorEjecucion.Api.infrastructure.repository.jpa;

import ApiGymorEjecucion.Api.domain.model.usuario.Rol;
import ApiGymorEjecucion.Api.domain.model.usuario.Usuario;
import ApiGymorEjecucion.Api.domain.repository.UsuarioRepository;
import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.usuario.UsuarioEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UsuarioRepositoryJpa implements UsuarioRepository {

    private final UsuarioJpaRepository jpaRepository;

    public UsuarioRepositoryJpa(UsuarioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return jpaRepository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        UsuarioEntity entity = toEntity(usuario);
        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public boolean existePorEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    private Usuario toDomain(UsuarioEntity entity) {
        Usuario usuario = Usuario.crear(
                entity.getId(),
                entity.getEmail(),
                entity.getNombre(),
                entity.getApellido(),
                entity.getPasswordHash()
        );
        if (!entity.isActivo()) usuario.desactivar();
        if (entity.getUltimoAcceso() != null) usuario.registrarAcceso();

        entity.getRoles().forEach(rolNombre -> {
            Rol rol = Rol.crear(rolNombre, rolNombre, "");
            usuario.agregarRol(rol);
        });

        return usuario;
    }

    private UsuarioEntity toEntity(Usuario usuario) {
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(usuario.getId());
        entity.setEmail(usuario.getEmail());
        entity.setNombre(usuario.getNombre());
        entity.setApellido(usuario.getApellido());
        entity.setPasswordHash(usuario.getPasswordHash());
        entity.setActivo(usuario.isActivo());
        entity.setFechaCreacion(usuario.getFechaCreacion());
        entity.setUltimoAcceso(usuario.getUltimoAcceso());
        usuario.getRoles().forEach(rol -> entity.getRoles().add(rol.getNombre()));
        return entity;
    }
}
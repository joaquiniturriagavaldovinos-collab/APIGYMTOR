package ApiGymorEjecucion.Api.domain.repository;

import ApiGymorEjecucion.Api.domain.model.usuario.Rol;

import java.util.Optional;

public interface RolRepository {

    Optional<Rol> buscarPorNombre(String nombre);

    Rol guardar(Rol rol);
}
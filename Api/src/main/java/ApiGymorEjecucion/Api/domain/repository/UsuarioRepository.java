package ApiGymorEjecucion.Api.domain.repository;

import ApiGymorEjecucion.Api.domain.model.usuario.Usuario;
import java.util.Optional;

public interface UsuarioRepository {
    Optional<Usuario> buscarPorEmail(String email);
    Usuario guardar(Usuario usuario);
    boolean existePorEmail(String email);
}
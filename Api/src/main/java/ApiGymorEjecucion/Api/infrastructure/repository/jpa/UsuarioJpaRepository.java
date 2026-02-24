package ApiGymorEjecucion.Api.infrastructure.repository.jpa;

import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.usuario.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, String> {
    Optional<UsuarioEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
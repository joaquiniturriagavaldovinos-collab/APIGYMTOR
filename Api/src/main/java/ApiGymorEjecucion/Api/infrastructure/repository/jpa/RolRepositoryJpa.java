package ApiGymorEjecucion.Api.infrastructure.repository.jpa;

import ApiGymorEjecucion.Api.domain.model.usuario.Rol;
import ApiGymorEjecucion.Api.domain.repository.RolRepository;
import org.springframework.stereotype.Repository;
import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.rol.RolEntity;
import org.springframework.context.annotation.Primary;

import java.util.Optional;

@Repository
@Primary

public class RolRepositoryJpa implements RolRepository {

    private final RolJpaRepository jpaRepository;

    public RolRepositoryJpa(RolJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

@Override
public Optional<Rol> buscarPorNombre(String nombre) {
    return jpaRepository.findByNombre(nombre)
            .map(entity -> Rol.crear(
                    entity.getId(),
                    entity.getNombre(),
                    entity.getDescripcion()
            ));
}

@Override
public Rol guardar(Rol rol) {
    RolEntity entity = new RolEntity();
    entity.setId(rol.getId());
    entity.setNombre(rol.getNombre());
    entity.setDescripcion(rol.getDescripcion());

    RolEntity saved = jpaRepository.save(entity);

    return Rol.crear(
            saved.getId(),
            saved.getNombre(),
            saved.getDescripcion()
    );
}



}
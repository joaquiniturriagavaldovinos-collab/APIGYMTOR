package ApiGymorEjecucion.Api.infrastructure.repository.jpa;

import ApiGymorEjecucion.Api.domain.model.servicio.ModalidadClase;
import ApiGymorEjecucion.Api.domain.model.servicio.Servicio;
import ApiGymorEjecucion.Api.domain.repository.ServicioRepository;
import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.ServicioEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ServicioRepositoryJpa implements ServicioRepository {

    private final ServicioJpaRepository jpaRepository;

    public ServicioRepositoryJpa(ServicioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Servicio guardar(Servicio servicio) {
        ServicioEntity entity = mapToJpa(servicio);
        ServicioEntity saved = jpaRepository.save(entity);
        return mapToDomain(saved);
    }

    @Override
    public List<Servicio> buscarTodos() {
        return jpaRepository.findAll()
                .stream()
                .map(this::mapToDomain)
                .toList();
    }

    @Override
    public List<Servicio> buscarPorModalidad(ModalidadClase modalidad) {
        return jpaRepository.findByModalidad(modalidad)
                .stream()
                .map(this::mapToDomain)
                .toList();
    }

    @Override
    public Optional<Servicio> buscarPorId(String id) {
        return jpaRepository.findById(id)
                .map(this::mapToDomain);
    }

    @Override
    public List<Servicio> buscarActivos() {
        return jpaRepository.findByActivoTrue()
                .stream()
                .map(this::mapToDomain)
                .toList();
    }

    @Override
    public Optional<Servicio> buscarPorNombre(String nombre) {
        return jpaRepository.findByNombre(nombre)
                .map(this::mapToDomain);
    }


    @Override
    public boolean existePorNombre(String nombre) {
        return jpaRepository.existsByNombre(nombre);
    }

    @Override
    public long contar() {
        return jpaRepository.count();
    }

    @Override
    public boolean eliminar(String id) {
        if (!jpaRepository.existsById(id)) {
            return false;
        }
        jpaRepository.deleteById(id);
        return true;
    }






    /* ===================== MAPPERS ===================== */

    private ServicioEntity mapToJpa(Servicio servicio) {
        ServicioEntity entity = new ServicioEntity();
        entity.setId(servicio.getId());
        entity.setNombre(servicio.getNombre());
        entity.setDescripcion(servicio.getDescripcion());
        entity.setModalidad(servicio.getModalidad());
        entity.setPrecioSesion(servicio.getPrecioSesion());
        entity.setDuracionMinutos(servicio.getDuracionMinutos());
        entity.setCapacidadMaxima(servicio.getCapacidadMaxima());
        entity.setActivo(servicio.isActivo());
        entity.setFechaCreacion(servicio.getFechaCreacion());
        return entity;
    }

    private Servicio mapToDomain(ServicioEntity entity) {
        return Servicio.reconstruir(
                entity.getId(),
                entity.getNombre(),
                entity.getDescripcion(),
                entity.getModalidad(),
                entity.getPrecioSesion(),
                entity.getDuracionMinutos(),
                entity.getCapacidadMaxima(),
                entity.isActivo(),
                entity.getFechaCreacion()
        );
    }
}
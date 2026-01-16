package ApiGymorEjecucion.Api.infrastructure.repository.jpa;

import ApiGymorEjecucion.Api.domain.model.servicio.ModalidadClase;
import ApiGymorEjecucion.Api.domain.model.servicio.Servicio;
import ApiGymorEjecucion.Api.domain.repository.ServicioRepository;
import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.ServicioJpaEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ServicioRepositoryImpl implements ServicioRepository {

    private final ServicioJpaRepository jpaRepository;

    public ServicioRepositoryImpl(ServicioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Servicio guardar(Servicio servicio) {
        ServicioJpaEntity entity = mapToJpa(servicio);
        ServicioJpaEntity saved = jpaRepository.save(entity);
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

    /* ===================== MAPPERS ===================== */

    private ServicioJpaEntity mapToJpa(Servicio servicio) {
        ServicioJpaEntity entity = new ServicioJpaEntity();
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

    private Servicio mapToDomain(ServicioJpaEntity entity) {
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
package ApiGymorEjecucion.Api.infrastructure.repository.jpa;

import ApiGymorEjecucion.Api.domain.model.Cliente.Cliente;
import ApiGymorEjecucion.Api.domain.model.Cliente.DireccionEntrega;
import ApiGymorEjecucion.Api.domain.model.Cliente.TipoCliente;
import ApiGymorEjecucion.Api.domain.repository.ClienteRepository;
import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.ClienteEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador JPA para Cliente (Arquitectura Hexagonal + DDD)
 *
 * Esta implementación se usa en:
 * - LOCAL: PostgreSQL en desarrollo
 * - PROD: PostgreSQL en producción
 *
 * En TESTS se usa ClienteRepositoryInMemory
 */
@Repository
@Primary  // Prioridad sobre InMemory si ambos están activos
@Profile("!test")  // Se activa en todos los perfiles EXCEPTO test
public class ClienteRepositoryJpa implements ClienteRepository {

    private final ClienteJpaRepository jpaRepository;

    public ClienteRepositoryJpa(ClienteJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    // ===== CRUD =====

    @Override
    public Cliente guardar(Cliente cliente) {
        ClienteEntity entity = mapearAEntity(cliente);
        ClienteEntity guardado = jpaRepository.save(entity);
        return mapearADominio(guardado);
    }

    @Override
    public Optional<Cliente> buscarPorId(String id) {
        return jpaRepository.findById(id)
                .map(this::mapearADominio);
    }

    @Override
    public Optional<Cliente> buscarPorRut(String rut) {
        return jpaRepository.findByRut(rut)
                .map(this::mapearADominio);
    }

    @Override
    public Optional<Cliente> buscarPorEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(this::mapearADominio);
    }

    @Override
    public List<Cliente> buscarPorTipo(TipoCliente tipo) {
        ClienteEntity.TipoClienteEntity tipoEntity =
                ClienteEntity.TipoClienteEntity.valueOf(tipo.name());

        return jpaRepository.findByTipo(tipoEntity).stream()
                .map(this::mapearADominio)
                .collect(Collectors.toList());
    }

    @Override
    public List<Cliente> buscarActivos() {
        return jpaRepository.findByActivoTrue().stream()
                .map(this::mapearADominio)
                .collect(Collectors.toList());
    }

    @Override
    public List<Cliente> buscarTodos() {
        return jpaRepository.findAll().stream()
                .map(this::mapearADominio)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existePorRut(String rut) {
        return jpaRepository.existsByRut(rut);
    }

    @Override
    public boolean existePorEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public boolean eliminar(String id) {
        if (jpaRepository.existsById(id)) {
            jpaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public long contar() {
        return jpaRepository.count();
    }

    // ===== MAPPERS: Domain ↔ Entity =====

    private ClienteEntity mapearAEntity(Cliente cliente) {
        ClienteEntity entity = new ClienteEntity(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getApellido(),
                cliente.getEmail(),
                cliente.getTelefono(),
                cliente.getRut(),
                ClienteEntity.TipoClienteEntity.valueOf(cliente.getTipo().name()),
                cliente.getFechaRegistro()
        );

        entity.setActivo(cliente.isActivo());

        // Direcciones
        entity.setDirecciones(
                cliente.getDirecciones().stream()
                        .map(this::mapearDireccionAEntity)
                        .collect(Collectors.toList())
        );

        // Dirección principal
        if (cliente.getDireccionPrincipal() != null) {
            entity.setDireccionPrincipal(
                    mapearDireccionAEntity(cliente.getDireccionPrincipal())
            );
        }

        return entity;
    }

    private Cliente mapearADominio(ClienteEntity entity) {
        Cliente cliente = Cliente.crear(
                entity.getId(),
                entity.getNombre(),
                entity.getApellido(),
                entity.getEmail(),
                entity.getTelefono(),
                entity.getRut(),
                TipoCliente.valueOf(entity.getTipo().name())
        );

        if (!entity.isActivo()) {
            cliente.desactivar();
        }

        // Direcciones
        entity.getDirecciones().forEach(dirEntity ->
                cliente.agregarDireccion(mapearDireccionADominio(dirEntity))
        );

        // Dirección principal
        if (entity.getDireccionPrincipal() != null) {
            DireccionEntrega principal =
                    mapearDireccionADominio(entity.getDireccionPrincipal());

            cliente.establecerDireccionPrincipal(principal);
        }

        return cliente;
    }

    // ===== VALUE OBJECT: Dirección =====

    private ClienteEntity.DireccionEntregaEntity mapearDireccionAEntity(
            DireccionEntrega direccion) {

        return new ClienteEntity.DireccionEntregaEntity(
                direccion.getCalle(),
                direccion.getNumero(),
                direccion.getComuna(),
                direccion.getCiudad(),
                direccion.getRegion(),
                direccion.getCodigoPostal(),
                direccion.getReferencia()
        );
    }

    private DireccionEntrega mapearDireccionADominio(
            ClienteEntity.DireccionEntregaEntity entity) {

        return DireccionEntrega.crear(
                entity.getCalle(),
                entity.getNumero(),
                entity.getComuna(),
                entity.getCiudad(),
                entity.getRegion(),
                entity.getCodigoPostal(),
                entity.getReferencia()
        );
    }
}
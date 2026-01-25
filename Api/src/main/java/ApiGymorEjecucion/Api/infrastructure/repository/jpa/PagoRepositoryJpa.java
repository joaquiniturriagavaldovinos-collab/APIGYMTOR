package ApiGymorEjecucion.Api.infrastructure.repository.jpa;

import ApiGymorEjecucion.Api.domain.model.Pago.EstadoPago;
import ApiGymorEjecucion.Api.domain.model.Pago.Pago;
import ApiGymorEjecucion.Api.domain.repository.PagoRepository;
import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.pago.PagoEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Primary  // Prioridad sobre InMemory si ambos están activos
@Profile("!test")  // Se activa en todos los perfiles EXCEPTO test
public class PagoRepositoryJpa implements PagoRepository {

    private final PagoJpaRepository jpaRepository;

    public PagoRepositoryJpa(PagoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Pago guardar(Pago pago) {
        System.out.println("\n🔧 PagoRepositoryJpa.guardar() - INICIO");
        System.out.println("   Pago ID: " + pago.getId());
        System.out.println("   Estado dominio: " + pago.getEstado());
        System.out.println("   Código dominio: " + pago.getCodigoAutorizacion());

        PagoEntity entity = mapToJpa(pago);

        System.out.println("\n🔧 Después de mapToJpa():");
        System.out.println("   Código en entity: " + entity.getCodigoAutorizacion());

        // ✅ USAR saveAndFlush para persistencia inmediata
        PagoEntity saved = jpaRepository.saveAndFlush(entity);

        System.out.println("\n🔧 Después de saveAndFlush():");
        System.out.println("   ID guardado: " + saved.getId());
        System.out.println("   Estado guardado: " + saved.getEstado());
        System.out.println("   Código guardado: " + saved.getCodigoAutorizacion());

        Pago resultado = mapToDomain(saved);

        System.out.println("\n🔧 Después de mapToDomain():");
        System.out.println("   Código en dominio retornado: " + resultado.getCodigoAutorizacion());
        System.out.println("🔧 PagoRepositoryJpa.guardar() - FIN\n");

        return resultado;
    }
    @Override
    public Optional<Pago> buscarPorId(String id) {
        return jpaRepository.findById(id).map(this::mapToDomain);
    }

    @Override
    public List<Pago> buscarPorPedidoId(String pedidoId) {
        return jpaRepository.findByPedidoId(pedidoId)
                .stream()
                .map(this::mapToDomain)
                .toList();
    }

    @Override
    public Optional<Pago> buscarPorReferenciaPasarela(String referencia) {
        return jpaRepository.findByReferenciaPasarela(referencia)
                .map(this::mapToDomain);
    }

    @Override
    public List<Pago> buscarPorEstado(EstadoPago estado) {
        return jpaRepository.findByEstado(estado)
                .stream()
                .map(this::mapToDomain)
                .toList();
    }

    @Override
    public List<Pago> buscarTodos() {
        return jpaRepository.findAll()
                .stream()
                .map(this::mapToDomain)
                .toList();
    }

    @Override
    public boolean existePorPedidoId(String pedidoId) {
        return jpaRepository.existsByPedidoId(pedidoId);
    }

    @Override
    public boolean eliminar(String id) {
        if (!jpaRepository.existsById(id)) {
            return false;
        }
        jpaRepository.deleteById(id);
        return true;
    }

    @Override
    public long contar() {
        return jpaRepository.count();
    }

    /* ===================== MAPPERS ===================== */

    private PagoEntity mapToJpa(Pago pago) {
        System.out.println("   🔹 mapToJpa() - INICIO");
        PagoEntity entity = new PagoEntity();
        entity.setId(pago.getId());
        entity.setPedidoId(pago.getPedidoId());
        entity.setMonto(pago.getMonto());
        entity.setMetodoPago(pago.getMetodoPago());
        entity.setEstado(pago.getEstado());
        entity.setReferenciaPasarela(pago.getReferenciaPasarela());

        String codigo = pago.getCodigoAutorizacion();
        System.out.println("   🔹 Asignando código: " + codigo);
        entity.setCodigoAutorizacion(codigo);
        System.out.println("   🔹 Código asignado en entity: " + entity.getCodigoAutorizacion());

        entity.setMotivoRechazo(pago.getMotivoRechazo());
        entity.setFechaCreacion(pago.getFechaCreacion());
        entity.setFechaProcesamiento(pago.getFechaProcesamiento());
        entity.setFechaConfirmacion(pago.getFechaConfirmacion());
        System.out.println("   🔹 mapToJpa() - FIN");
        return entity;
    }

    private Pago mapToDomain(PagoEntity entity) {
        return Pago.reconstruir(
                entity.getId(),
                entity.getPedidoId(),
                entity.getMonto(),
                entity.getMetodoPago(),
                entity.getEstado(),
                entity.getReferenciaPasarela(),
                entity.getCodigoAutorizacion(),
                entity.getMotivoRechazo(),
                entity.getFechaCreacion(),
                entity.getFechaProcesamiento(),
                entity.getFechaConfirmacion()
        );
    }
}

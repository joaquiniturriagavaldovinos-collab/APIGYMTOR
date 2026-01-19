package ApiGymorEjecucion.Api.infrastructure.repository.jpa;

import ApiGymorEjecucion.Api.domain.model.Despacho.*;
import ApiGymorEjecucion.Api.domain.repository.DespachoRepository;
import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.despacho.DespachoEntity;
import ApiGymorEjecucion.Api.domain.model.Despacho.DireccionEntrega;
import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.despacho.embeddable.DireccionEntregaEmbeddable;
import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.despacho.embeddable.GuiaDespachoEmbeddable;
import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.despacho.embeddable.TransportistaEmbeddable;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("prod")
public class DespachoRepositoryJpa implements DespachoRepository {

    private final DespachoJpaRepository jpa;

    public DespachoRepositoryJpa(DespachoJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Despacho guardar(Despacho despacho) {
        return toDomain(jpa.save(toEntity(despacho)));
    }

    @Override
    public Optional<Despacho> buscarPorId(String id) {
        return jpa.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Despacho> buscarPorPedidoId(String pedidoId) {
        return jpa.findByPedidoId(pedidoId).map(this::toDomain);
    }

    @Override
    public Optional<Despacho> buscarPorGuia(String numeroGuia) {
        return jpa.findByGuiaDespacho_Numero(numeroGuia).map(this::toDomain);
    }

    @Override
    public List<Despacho> buscarPendientes() {
        return jpa.findByEstado(EstadoDespacho.PENDIENTE)
                .stream().map(this::toDomain).toList();
    }

    @Override
    public List<Despacho> buscarEnTransito() {
        return jpa.findByEstado(EstadoDespacho.EN_TRANSITO)
                .stream().map(this::toDomain).toList();
    }

    @Override
    public List<Despacho> buscarEntregados() {
        return jpa.findByEstado(EstadoDespacho.ENTREGADO)
                .stream().map(this::toDomain).toList();
    }

    @Override
    public List<Despacho> buscarTodos() {
        return jpa.findAll()
                .stream().map(this::toDomain).toList();
    }

    @Override
    public boolean existePorPedidoId(String pedidoId) {
        return jpa.existsByPedidoId(pedidoId);
    }

    @Override
    public boolean eliminar(String id) {
        if (!jpa.existsById(id)) {
            return false;
        }
        jpa.deleteById(id);
        return true;
    }

    @Override
    public long contar() {
        return jpa.count();
    }

    /* ===== MAPPERS ===== */
    private Despacho toDomain(DespachoEntity e) {
        return Despacho.reconstruir(
                e.getId(),
                e.getPedidoId(),
                e.getGuiaDespacho() == null ? null :
                        GuiaDespacho.crear(
                                e.getGuiaDespacho().getNumero(),
                                e.getGuiaDespacho().getUrlTracking()
                        ),
                e.getTransportista() == null ? null :
                        Transportista.crear(
                                e.getTransportista().getNombre(),
                                e.getTransportista().getCodigo(),
                                e.getTransportista().getTelefono()
                        ),
                e.getDireccionEntrega() == null ? null :
                        new DireccionEntrega(
                                e.getDireccionEntrega().getDireccionCompleta()
                        ),
                e.getFechaDespacho(),
                e.getFechaEntregaEstimada(),
                e.getFechaEntregaReal(),
                e.getObservaciones(),
                e.getEstado()
        );
    }

    private DespachoEntity toEntity(Despacho d) {

        DespachoEntity e = new DespachoEntity();
        e.setId(d.getId());
        e.setPedidoId(d.getPedidoId());
        e.setEstado(d.getEstado());

        e.setFechaDespacho(d.getFechaDespacho());
        e.setFechaEntregaEstimada(d.getFechaEntregaEstimada());
        e.setFechaEntregaReal(d.getFechaEntregaReal());
        e.setObservaciones(d.getObservaciones());

        if (d.getGuiaDespacho() != null) {
            e.setGuiaDespacho(
                    new GuiaDespachoEmbeddable(
                            d.getGuiaDespacho().getNumero(),
                            d.getGuiaDespacho().getFechaEmision(),
                            d.getGuiaDespacho().getUrlTracking()
                    )
            );
        }

        if (d.getTransportista() != null) {
            e.setTransportista(
                    new TransportistaEmbeddable(
                            d.getTransportista().getNombre(),
                            d.getTransportista().getCodigo(),
                            d.getTransportista().getTelefono()
                    )
            );
        }

        if (d.getDireccionEntrega() != null) {
            e.setDireccionEntrega(
                    new DireccionEntregaEmbeddable(
                            d.getDireccionEntrega().getDireccionCompleta()
                    )
            );
        }

        return e;
    }
}

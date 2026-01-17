package ApiGymorEjecucion.Api.infrastructure.repository.memory;

import ApiGymorEjecucion.Api.domain.model.Despacho.Despacho;
import ApiGymorEjecucion.Api.domain.repository.DespachoRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Profile({"local", "dev", "test"})
public class DespachoRepositoryInMemory implements DespachoRepository {

    private final Map<String, Despacho> despachos = new ConcurrentHashMap<>();

    @Override
    public Despacho guardar(Despacho despacho) {
        despachos.put(despacho.getId(), despacho);
        return despacho;
    }

    @Override
    public Optional<Despacho> buscarPorId(String id) {
        return Optional.ofNullable(despachos.get(id));
    }

    @Override
    public Optional<Despacho> buscarPorPedidoId(String pedidoId) {
        return despachos.values().stream()
                .filter(d -> d.getPedidoId().equals(pedidoId))
                .findFirst();
    }

    @Override
    public Optional<Despacho> buscarPorGuia(String numeroGuia) {
        return despachos.values().stream()
                .filter(d -> d.getGuiaDespacho() != null)
                .filter(d -> d.getGuiaDespacho().getNumero().equals(numeroGuia))
                .findFirst();
    }

    @Override
    public List<Despacho> buscarPendientes() {
        return despachos.values().stream()
                .filter(d -> !d.estaDespachado())
                .toList();
    }

    @Override
    public List<Despacho> buscarEnTransito() {
        return despachos.values().stream()
                .filter(Despacho::estaEnTransito)
                .toList();
    }

    @Override
    public List<Despacho> buscarEntregados() {
        return despachos.values().stream()
                .filter(Despacho::estaEntregado)
                .toList();
    }

    @Override
    public List<Despacho> buscarTodos() {
        return new ArrayList<>(despachos.values());
    }

    @Override
    public boolean existePorPedidoId(String pedidoId) {
        return despachos.values().stream()
                .anyMatch(d -> d.getPedidoId().equals(pedidoId));
    }

    @Override
    public boolean eliminar(String id) {
        return despachos.remove(id) != null;
    }

    @Override
    public long contar() {
        return despachos.size();
    }
}

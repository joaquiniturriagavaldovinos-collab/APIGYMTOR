package ApiGymorEjecucion.Api.application.usecase.despacho;

import ApiGymorEjecucion.Api.domain.model.Despacho.*;
import ApiGymorEjecucion.Api.domain.repository.DespachoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CrearDespachoUseCase {

    private final DespachoRepository despachoRepository;

    public CrearDespachoUseCase(DespachoRepository despachoRepository) {
        this.despachoRepository = despachoRepository;
    }

    public Despacho ejecutar(String pedidoId, String direccionCompleta) {
        System.out.println("\n📦 CREANDO DESPACHO PARA PEDIDO: " + pedidoId);

        // Validar que no exista ya un despacho para este pedido
        if (despachoRepository.existePorPedidoId(pedidoId)) {
            System.out.println("⚠️ Ya existe un despacho para este pedido. Saltando creación...");
            return despachoRepository.buscarPorPedidoId(pedidoId)
                    .orElseThrow(() -> new IllegalStateException("Error al buscar despacho existente"));
        }

        // 1. Generar ID del despacho
        String despachoId = generarIdDespacho();
        System.out.println("   Despacho ID: " + despachoId);

        // 2. Crear despacho (ahora sin dirección en el constructor simple)
        Despacho despacho = Despacho.crear(despachoId, pedidoId);
        System.out.println("   Estado inicial: " + despacho.getEstado());

        // 3. Generar y asignar guía
        String numeroGuia = generarNumeroGuia();
        String urlTracking = generarUrlTracking(numeroGuia);
        GuiaDespacho guia = GuiaDespacho.crear(numeroGuia, urlTracking);
        System.out.println("   Guía generada: " + numeroGuia);

        // 4. Asignar transportista por defecto
        Transportista transportista = Transportista.crear(
                "Chilexpress",
                "CHEX",
                "+56912345678"
        );
        System.out.println("   Transportista: " + transportista.getNombre());

        // 5. Despachar (esto cambia el estado a EN_TRANSITO)
        despacho.despachar(guia, transportista);
        System.out.println("   Estado después de despachar: " + despacho.getEstado());

        // 6. Establecer fecha estimada (3 días hábiles)
        LocalDateTime fechaEstimada = LocalDateTime.now().plusDays(3);
        despacho.establecerFechaEntregaEstimada(fechaEstimada);
        System.out.println("   Fecha estimada entrega: " + fechaEstimada);

        // 7. Guardar
        Despacho despachoGuardado = despachoRepository.guardar(despacho);
        System.out.println("✅ Despacho creado y guardado exitosamente\n");

        return despachoGuardado;
    }

    private String generarIdDespacho() {
        return "DESP-" + System.currentTimeMillis();
    }

    private String generarNumeroGuia() {
        return "GUIA-" + System.currentTimeMillis();
    }

    private String generarUrlTracking(String numeroGuia) {
        return "https://tracking.gymor.cl/despachos/" + numeroGuia;
    }
}
package ApiGymorEjecucion.Api.application.usecase.despacho;

import ApiGymorEjecucion.Api.application.dto.request.despacho.ActualizarEstadoRequest;
import ApiGymorEjecucion.Api.application.dto.response.despacho.DespachoResponse;
import ApiGymorEjecucion.Api.domain.model.Despacho.Despacho;
import ApiGymorEjecucion.Api.domain.repository.DespachoRepository;
import org.springframework.stereotype.Service;

@Service
public class ActualizarEstadoDespachoUseCase {

    private final DespachoRepository despachoRepository;

    public ActualizarEstadoDespachoUseCase(DespachoRepository despachoRepository) {
        this.despachoRepository = despachoRepository;
    }

    public DespachoResponse ejecutar(String despachoId, ActualizarEstadoRequest request) {

        System.out.println("\n========================================");
        System.out.println("📦 ACTUALIZANDO ESTADO DE DESPACHO");
        System.out.println("========================================");
        System.out.println("Despacho ID: " + despachoId);
        System.out.println("Tipo actualización: " + request.getTipoActualizacion());

        // Validar
        validarRequest(request);

        // Buscar despacho
        Despacho despacho = despachoRepository.buscarPorId(despachoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el despacho con ID: " + despachoId
                ));

        System.out.println("Estado actual: " + despacho.getEstado());

        // Actualizar según tipo de actualización
        switch (request.getTipoActualizacion()) {
            case "ENTREGA_CONFIRMADA":
                System.out.println("✅ Confirmando entrega...");
                despacho.confirmarEntrega();

                // ✅ AGREGAR: Actualizar observaciones si vienen
                if (request.getObservaciones() != null && !request.getObservaciones().isBlank()) {
                    despacho.actualizarObservaciones(request.getObservaciones());
                    System.out.println("📝 Observaciones: " + request.getObservaciones());
                }
                break;

            case "FECHA_ESTIMADA":
                System.out.println("📅 Actualizando fecha estimada...");
                if (request.getFechaEntregaEstimada() == null) {
                    throw new IllegalArgumentException("La fecha estimada es requerida");
                }
                despacho.establecerFechaEntregaEstimada(request.getFechaEntregaEstimada());
                System.out.println("Nueva fecha: " + request.getFechaEntregaEstimada());

                // También actualizar observaciones si vienen
                if (request.getObservaciones() != null && !request.getObservaciones().isBlank()) {
                    despacho.actualizarObservaciones(request.getObservaciones());
                }
                break;

            case "OBSERVACIONES":
                System.out.println("📝 Actualizando observaciones...");
                if (request.getObservaciones() == null || request.getObservaciones().isBlank()) {
                    throw new IllegalArgumentException("Las observaciones son requeridas");
                }
                despacho.actualizarObservaciones(request.getObservaciones());
                System.out.println("Observaciones: " + request.getObservaciones());
                break;

            default:
                throw new IllegalArgumentException(
                        "Tipo de actualización no soportado: " + request.getTipoActualizacion()
                );
        }

        // Persistir
        System.out.println("💾 Guardando cambios...");
        Despacho actualizado = despachoRepository.guardar(despacho);

        System.out.println(" Despacho actualizado:");
        System.out.println("   Estado: " + actualizado.getEstado());
        System.out.println("   Observaciones: " + actualizado.getObservaciones());
        System.out.println("   Fecha entrega real: " + actualizado.getFechaEntregaReal());
        System.out.println("========================================\n");

        // Retornar
        return mapearAResponse(actualizado);
    }

    private void validarRequest(ActualizarEstadoRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }
        if (request.getTipoActualizacion() == null || request.getTipoActualizacion().isBlank()) {
            throw new IllegalArgumentException("El tipo de actualización es requerido");
        }
    }

    // CRÍTICO: Mapper COMPLETO
    private DespachoResponse mapearAResponse(Despacho despacho) {
        DespachoResponse response = new DespachoResponse();

        response.setId(despacho.getId());
        response.setPedidoId(despacho.getPedidoId());

        // Guía completa
        if (despacho.getGuiaDespacho() != null) {
            response.setNumeroGuia(despacho.getGuiaDespacho().getNumero());
            response.setUrlTracking(despacho.getGuiaDespacho().getUrlTracking());
            response.setFechaEmisionGuia(despacho.getGuiaDespacho().getFechaEmision());
        }

        // Transportista completo
        if (despacho.getTransportista() != null) {
            response.setTransportista(despacho.getTransportista().getNombre());
            response.setCodigoTransportista(despacho.getTransportista().getCodigo());
            response.setTelefonoTransportista(despacho.getTransportista().getTelefono());
        }

        // Dirección
        if (despacho.getDireccionEntrega() != null) {
            response.setDireccionCompleta(despacho.getDireccionEntrega().getDireccionCompleta());
        }

        response.setFechaDespacho(despacho.getFechaDespacho());
        response.setFechaEntregaEstimada(despacho.getFechaEntregaEstimada());
        response.setFechaEntregaReal(despacho.getFechaEntregaReal());

        // Observaciones
        response.setObservaciones(despacho.getObservaciones());

        // Flags
        response.setEstaDespachado(despacho.estaDespachado());
        response.setEstaEntregado(despacho.estaEntregado());

        return response;
    }
}
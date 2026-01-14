package ApiGymorEjecucion.Api.application.usecase.cliente;


import ApiGymorEjecucion.Api.domain.model.Cliente.Cliente;
import ApiGymorEjecucion.Api.domain.repository.ClienteRepository;
import org.springframework.stereotype.Service;

/**
 * Caso de Uso: Desactivar Cliente (Soft Delete)
 *
 * NO elimina el cliente de la BD, solo lo marca como inactivo.
 * Esto permite mantener integridad referencial con pedidos históricos.
 */
@Service
public class DesactivarClienteUseCase {

    private final ClienteRepository clienteRepository;

    public DesactivarClienteUseCase(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public void ejecutar(String id) {
        // Validar
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID del cliente es requerido");
        }

        // Buscar
        Cliente cliente = clienteRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el cliente con ID: " + id
                ));

        // Validar que esté activo
        if (!cliente.isActivo()) {
            throw new IllegalStateException(
                    "El cliente ya está desactivado"
            );
        }

        // DOMINIO: Desactivar
        cliente.desactivar();

        // Persistir
        clienteRepository.guardar(cliente);

        // Aquí podrías emitir un evento de dominio:
        // - ClienteDesactivado
        // - Cancelar suscripciones activas
        // - Notificar al cliente
    }
}
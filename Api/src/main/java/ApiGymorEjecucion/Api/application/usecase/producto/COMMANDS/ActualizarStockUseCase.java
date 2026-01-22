package ApiGymorEjecucion.Api.application.usecase.producto.COMMANDS;

import ApiGymorEjecucion.Api.application.dto.response.producto.ActualizarStockResponse;
import ApiGymorEjecucion.Api.domain.exception.StockInsuficienteException;
import ApiGymorEjecucion.Api.domain.model.producto.Producto;
import ApiGymorEjecucion.Api.domain.repository.ProductoRepository;
import org.springframework.stereotype.Service;

/**
 * Caso de Uso: Actualizar Stock
 *
 * Permite aumentar o disminuir el stock de un producto
 */
@Service
public class ActualizarStockUseCase {

    private final ProductoRepository productoRepository;

    public ActualizarStockUseCase(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * Incrementa el stock de un producto (entrada de inventario)
     *
     * @param productoId ID del producto
     * @param cantidad Cantidad a agregar
     * @return Respuesta con nuevo stock
     */
    public ActualizarStockResponse incrementarStock(String productoId, int cantidad) {
        // Validar inputs
        validarInputs(productoId, cantidad);

        // Buscar producto
        Producto producto = productoRepository.buscarPorId(productoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el producto con ID: " + productoId
                ));

        // Incrementar stock en dominio
        producto.incrementarStock(cantidad);

        // Persistir
        Producto productoActualizado = productoRepository.guardar(producto);

        // Retornar response
        return new ActualizarStockResponse(
                productoActualizado.getId(),
                productoActualizado.getNombre(),
                productoActualizado.getStockDisponible(),
                "INCREMENTO",
                cantidad
        );
    }

    /**
     * Decrementa el stock de un producto (venta, reserva)
     *
     * @param productoId ID del producto
     * @param cantidad Cantidad a reducir
     * @return Respuesta con nuevo stock
     */
    public ActualizarStockResponse decrementarStock(String productoId, int cantidad) {
        // Validar inputs
        validarInputs(productoId, cantidad);

        // Buscar producto
        Producto producto = productoRepository.buscarPorId(productoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el producto con ID: " + productoId
                ));

        // Verificar stock suficiente
        if (producto.getStockDisponible() < cantidad) {
            throw new StockInsuficienteException(
                    productoId,
                    producto.getStockDisponible(),
                    cantidad
            );
        }

        // Decrementar stock en dominio
        producto.decrementarStock(cantidad);

        // Persistir
        Producto productoActualizado = productoRepository.guardar(producto);

        // Retornar response
        return new ActualizarStockResponse(
                productoActualizado.getId(),
                productoActualizado.getNombre(),
                productoActualizado.getStockDisponible(),
                "DECREMENTO",
                cantidad
        );
    }

    /**
     * Ajusta el stock a un valor específico (inventario manual)
     *
     * @param productoId ID del producto
     * @param nuevoStock Nuevo valor de stock
     * @return Respuesta con nuevo stock
     */
    public ActualizarStockResponse ajustarStock(String productoId, int nuevoStock) {
        // Validar inputs
        if (productoId == null || productoId.isBlank()) {
            throw new IllegalArgumentException("El ID del producto es requerido");
        }
        if (nuevoStock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }

        // Buscar producto
        Producto producto = productoRepository.buscarPorId(productoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el producto con ID: " + productoId
                ));

        int stockAnterior = producto.getStockDisponible();
        int diferencia = nuevoStock - stockAnterior;

        // Ajustar stock
        if (diferencia > 0) {
            producto.incrementarStock(diferencia);
        } else if (diferencia < 0) {
            producto.decrementarStock(Math.abs(diferencia));
        }

        // Persistir
        Producto productoActualizado = productoRepository.guardar(producto);

        // Retornar response
        return new ActualizarStockResponse(
                productoActualizado.getId(),
                productoActualizado.getNombre(),
                productoActualizado.getStockDisponible(),
                "AJUSTE",
                Math.abs(diferencia)
        );
    }

    private void validarInputs(String productoId, int cantidad) {
        if (productoId == null || productoId.isBlank()) {
            throw new IllegalArgumentException("El ID del producto es requerido");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
    }



}
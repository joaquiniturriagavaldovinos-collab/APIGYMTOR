package ApiGymorEjecucion.Api.domain.model.producto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad: Producto
 * Representa un producto físico del catálogo (disco, máquina, etc.)
 * Diseñado para CQRS Liviano - contiene toda la lógica de negocio
 */
public class Producto {
    private final String id;
    private final String codigo;
    private String nombre;
    private String descripcion;
    private final TipoProducto tipo;
    private BigDecimal precio;
    private Stock stock;
    private boolean activo;
    private final LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    private Producto(String id, String codigo, String nombre, String descripcion,
                     TipoProducto tipo, BigDecimal precio) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.precio = precio;
        this.stock = Stock.crear(0);
        this.activo = true;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    // ===== FACTORY METHODS =====

    /**
     * Crea un nuevo producto (Command)
     */
    public static Producto crear(String id, String codigo, String nombre,
                                 String descripcion, TipoProducto tipo, BigDecimal precio) {
        validarDatosCreacion(id, codigo, nombre, tipo, precio);
        return new Producto(id, codigo, nombre, descripcion, tipo, precio);
    }

    private static void validarDatosCreacion(String id, String codigo, String nombre,
                                             TipoProducto tipo, BigDecimal precio) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID del producto es requerido");
        }
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código del producto es requerido");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del producto es requerido");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de producto es requerido");
        }
        if (precio == null || precio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }
    }

    // ===== COMMANDS - GESTIÓN DE STOCK =====

    /**
     * Establece el stock completo del producto
     */
    public void establecerStock(Stock nuevoStock) {
        if (nuevoStock == null) {
            throw new IllegalArgumentException("El stock no puede ser nulo");
        }
        this.stock = nuevoStock;
        this.fechaActualizacion = LocalDateTime.now();
    }

    /**
     * Incrementa el stock (por reposición)
     */
    public void incrementarStock(int cantidad) {
        validarCantidadPositiva(cantidad, "incrementar");
        this.stock = this.stock.incrementar(cantidad);
        this.fechaActualizacion = LocalDateTime.now();
    }

    /**
     * Decrementa el stock (por venta confirmada)
     */
    public void decrementarStock(int cantidad) {
        validarCantidadPositiva(cantidad, "decrementar");
        validarStockSuficiente(cantidad);
        this.stock = this.stock.decrementar(cantidad);
        this.fechaActualizacion = LocalDateTime.now();
    }

    /**
     * Reserva stock (para carrito de compras, cotización)
     */
    public void reservarStock(int cantidad) {
        validarCantidadPositiva(cantidad, "reservar");
        validarProductoActivo("reservar stock");
        validarStockSuficiente(cantidad);
        this.stock = this.stock.reservar(cantidad);
        this.fechaActualizacion = LocalDateTime.now();
    }

    /**
     * Libera stock previamente reservado (cancelación)
     */
    public void liberarReserva(int cantidad) {
        validarCantidadPositiva(cantidad, "liberar");
        validarReservaSuficiente(cantidad);
        this.stock = this.stock.liberarReserva(cantidad);
        this.fechaActualizacion = LocalDateTime.now();
    }

    // ===== COMMANDS - GESTIÓN DE PRODUCTO =====

    /**
     * Actualiza el precio del producto
     */
    public void actualizarPrecio(BigDecimal nuevoPrecio) {
        if (nuevoPrecio == null || nuevoPrecio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }

        // Regla de negocio: validar cambios drásticos de precio
        if (this.precio != null) {
            BigDecimal diferenciaPorcentual = calcularDiferenciaPorcentual(nuevoPrecio);
            if (diferenciaPorcentual.compareTo(new BigDecimal("50")) > 0) {
                // Log o evento: precio cambió más del 50%
            }
        }

        this.precio = nuevoPrecio;
        this.fechaActualizacion = LocalDateTime.now();
    }

    /**
     * Actualiza información básica del producto
     */
    public void actualizarInformacion(String nuevoNombre, String nuevaDescripcion) {
        boolean cambioRealizado = false;

        if (nuevoNombre != null && !nuevoNombre.isBlank()) {
            if (!nuevoNombre.equals(this.nombre)) {
                this.nombre = nuevoNombre;
                cambioRealizado = true;
            }
        }

        if (nuevaDescripcion != null && !nuevaDescripcion.equals(this.descripcion)) {
            this.descripcion = nuevaDescripcion;
            cambioRealizado = true;
        }

        if (cambioRealizado) {
            this.fechaActualizacion = LocalDateTime.now();
        }
    }

    /**
     * Desactiva el producto (soft delete)
     */
    public void desactivar() {
        if (!this.activo) {
            throw new IllegalStateException("El producto ya está desactivado");
        }

        // Regla de negocio: advertir si hay stock reservado
        if (this.stock.getCantidadReservada() > 0) {
            throw new IllegalStateException(
                    String.format("No se puede desactivar el producto con stock reservado (%d unidades)",
                            this.stock.getCantidadReservada())
            );
        }

        this.activo = false;
        this.fechaActualizacion = LocalDateTime.now();
    }

    /**
     * Activa el producto
     */
    public void activar() {
        if (this.activo) {
            throw new IllegalStateException("El producto ya está activo");
        }
        this.activo = true;
        this.fechaActualizacion = LocalDateTime.now();
    }

    // ===== QUERIES - CONSULTAS DE NEGOCIO =====

    /**
     * Verifica si hay stock disponible suficiente
     */
    public boolean tieneStockDisponible(int cantidadRequerida) {
        return this.stock.tieneStockDisponible(cantidadRequerida);
    }

    /**
     * Obtiene la cantidad disponible actual
     */
    public int getStockDisponible() {
        return this.stock.getCantidadDisponible();
    }

    /**
     * Verifica si el producto está disponible para venta
     */
    public boolean estaDisponibleParaVenta() {
        return this.activo && this.stock.getCantidadDisponible() > 0;
    }

    /**
     * Verifica si el producto requiere despacho especial
     */
    public boolean requiereDespachoEspecial() {
        return this.tipo.requiereDespachoEspecial();
    }

    /**
     * Verifica si el producto es fabricado por GYMOR
     */
    public boolean esFabricadoPorGymor() {
        return this.tipo.esFabricado();
    }

    /**
     * Verifica si el producto tiene stock bajo (menos del 20% del total)
     */
    public boolean tieneStockBajo() {
        if (this.stock.getCantidad() == 0) {
            return false;
        }
        int umbralBajo = (int) Math.ceil(this.stock.getCantidad() * 0.2);
        return this.stock.getCantidadDisponible() <= umbralBajo;
    }

    /**
     * Verifica si el producto está agotado
     */
    public boolean estaAgotado() {
        return this.stock.getCantidadDisponible() == 0;
    }

    /**
     * Calcula el valor total del inventario de este producto
     */
    public BigDecimal calcularValorInventario() {
        return this.precio.multiply(new BigDecimal(this.stock.getCantidad()));
    }

    /**
     * Calcula el valor del stock disponible
     */
    public BigDecimal calcularValorStockDisponible() {
        return this.precio.multiply(new BigDecimal(this.stock.getCantidadDisponible()));
    }

    // ===== VALIDACIONES PRIVADAS =====

    private void validarCantidadPositiva(int cantidad, String operacion) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    String.format("La cantidad a %s debe ser mayor a cero", operacion)
            );
        }
    }

    private void validarProductoActivo(String operacion) {
        if (!this.activo) {
            throw new IllegalStateException(
                    String.format("No se puede %s de un producto inactivo", operacion)
            );
        }
    }

    private void validarStockSuficiente(int cantidadRequerida) {
        if (!this.stock.tieneStockDisponible(cantidadRequerida)) {
            throw new IllegalStateException(
                    String.format("Stock insuficiente. Disponible: %d, Requerido: %d",
                            this.stock.getCantidadDisponible(), cantidadRequerida)
            );
        }
    }

    private void validarReservaSuficiente(int cantidadALiberar) {
        if (this.stock.getCantidadReservada() < cantidadALiberar) {
            throw new IllegalStateException(
                    String.format("No hay suficientes reservas. Reservado: %d, Solicitado: %d",
                            this.stock.getCantidadReservada(), cantidadALiberar)
            );
        }
    }

    private BigDecimal calcularDiferenciaPorcentual(BigDecimal nuevoPrecio) {
        BigDecimal diferencia = nuevoPrecio.subtract(this.precio).abs();
        return diferencia.divide(this.precio, 2, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal("100"));
    }

    // ===== GETTERS =====

    public String getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public TipoProducto getTipo() {
        return tipo;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public Stock getStock() {
        return stock;
    }

    public boolean isActivo() {
        return activo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    // ===== IDENTITY =====

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return Objects.equals(id, producto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format(
                "Producto{id='%s', codigo='%s', nombre='%s', tipo=%s, precio=%s, " +
                        "stock=%d, activo=%s}",
                id, codigo, nombre, tipo, precio,
                stock.getCantidadDisponible(), activo
        );
    }
}
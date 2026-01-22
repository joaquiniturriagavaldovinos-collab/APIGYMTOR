package ApiGymorEjecucion.Api.infrastructure.repository.jpa;

import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.producto.ProductoEntity;
import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.producto.TipoProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoJpaRepository extends JpaRepository<ProductoEntity, String> {

    // Métodos ya existentes
    Optional<ProductoEntity> findByCodigo(String codigo);
    List<ProductoEntity> findByActivoTrue();
    List<ProductoEntity> findByTipo(TipoProductoEntity tipo);
    List<ProductoEntity> findByStock_CantidadDisponibleGreaterThan(int cantidad);
    boolean existsByCodigo(String codigo);

    // ===== MÉTODOS ADICIONALES ÚTILES =====

    /**
     * Busca productos con stock disponible = 0 (agotados)
     */
    @Query("SELECT p FROM ProductoEntity p WHERE p.stock.cantidadDisponible = 0 AND p.activo = true")
    List<ProductoEntity> findProductosAgotados();

    /**
     * Busca productos con stock bajo (menos del 20% del total)
     * Se calcula como: disponible <= (total * 0.2)
     */
    @Query("SELECT p FROM ProductoEntity p WHERE p.activo = true " +
            "AND p.stock.cantidadDisponible > 0 " +
            "AND p.stock.cantidadDisponible <= (p.stock.cantidad * 0.2)")
    List<ProductoEntity> findProductosConStockBajo();

    /**
     * Busca productos con reservas activas
     */
    @Query("SELECT p FROM ProductoEntity p WHERE p.stock.cantidadReservada > 0")
    List<ProductoEntity> findProductosConReservas();

    /**
     * Busca productos por rango de precio
     */
    @Query("SELECT p FROM ProductoEntity p WHERE p.precio BETWEEN :precioMin AND :precioMax")
    List<ProductoEntity> findByPrecioBetween(java.math.BigDecimal precioMin,
                                             java.math.BigDecimal precioMax);

    /**
     * Cuenta productos activos
     */
    long countByActivoTrue();

    /**
     * Cuenta productos por tipo
     */
    long countByTipo(TipoProductoEntity tipo);
}
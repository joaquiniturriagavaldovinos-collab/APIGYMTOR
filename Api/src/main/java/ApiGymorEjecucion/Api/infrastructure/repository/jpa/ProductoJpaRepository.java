package ApiGymorEjecucion.Api.infrastructure.repository.jpa;

import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoJpaRepository extends JpaRepository<ProductoEntity, String> {
    Optional<ProductoEntity> findByCodigo(String codigo);
    List<ProductoEntity> findByActivoTrue();
    List<ProductoEntity> findByTipo(ProductoEntity.TipoProductoEntity tipo);

    List<ProductoEntity> findByStock_CantidadDisponibleGreaterThan(int cantidad);

    boolean existsByCodigo(String codigo);
}
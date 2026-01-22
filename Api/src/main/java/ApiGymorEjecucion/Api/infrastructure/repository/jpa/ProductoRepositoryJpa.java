package ApiGymorEjecucion.Api.infrastructure.repository.jpa;

import ApiGymorEjecucion.Api.domain.model.producto.Producto;
import ApiGymorEjecucion.Api.domain.model.producto.Stock;
import ApiGymorEjecucion.Api.domain.model.producto.TipoProducto;
import ApiGymorEjecucion.Api.domain.repository.ProductoRepository;
import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.producto.ProductoEntity;
import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.producto.TipoProductoEntity;
import ApiGymorEjecucion.Api.infrastructure.repository.jpa.entity.producto.embeddable.StockEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador JPA para Producto (Arquitectura Hexagonal + DDD)
 * Versión actualizada con optimizaciones
 */
@Repository
@Primary
@Profile("!test")
public class ProductoRepositoryJpa implements ProductoRepository {

    private final ProductoJpaRepository jpaRepository;

    public ProductoRepositoryJpa(ProductoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Producto guardar(Producto producto) {
        ProductoEntity entity = mapearAEntity(producto);
        ProductoEntity guardado = jpaRepository.save(entity);
        return mapearADominio(guardado);
    }

    @Override
    public Optional<Producto> buscarPorId(String id) {
        return jpaRepository.findById(id)
                .map(this::mapearADominio);
    }

    @Override
    public Optional<Producto> buscarPorCodigo(String codigo) {
        return jpaRepository.findByCodigo(codigo)
                .map(this::mapearADominio);
    }

    @Override
    public List<Producto> buscarTodos() {
        return jpaRepository.findAll().stream()
                .map(this::mapearADominio)
                .collect(Collectors.toList());
    }

    @Override
    public List<Producto> buscarActivos() {
        return jpaRepository.findByActivoTrue().stream()
                .map(this::mapearADominio)
                .collect(Collectors.toList());
    }

    @Override
    public List<Producto> buscarPorTipo(TipoProducto tipo) {
        TipoProductoEntity tipoEntity = TipoProductoEntity.valueOf(tipo.name());
        return jpaRepository.findByTipo(tipoEntity).stream()
                .map(this::mapearADominio)
                .collect(Collectors.toList());
    }

    @Override
    public boolean eliminar(String id) {
        if (jpaRepository.existsById(id)) {
            jpaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean existePorCodigo(String codigo) {
        return jpaRepository.existsByCodigo(codigo);
    }

    @Override
    public long contar() {
        return jpaRepository.count();
    }

    @Override
    public List<Producto> buscarConStock() {
        return jpaRepository
                .findByStock_CantidadDisponibleGreaterThan(0)
                .stream()
                .map(this::mapearADominio)
                .collect(Collectors.toList());
    }

    // ===== MÉTODOS ADICIONALES (Opcionales) =====

    /**
     * Busca productos agotados usando query optimizada
     */
    public List<Producto> buscarAgotados() {
        return jpaRepository.findProductosAgotados().stream()
                .map(this::mapearADominio)
                .collect(Collectors.toList());
    }

    /**
     * Busca productos con stock bajo usando query optimizada
     */
    public List<Producto> buscarConStockBajo() {
        return jpaRepository.findProductosConStockBajo().stream()
                .map(this::mapearADominio)
                .collect(Collectors.toList());
    }

    // ===== MAPPERS: Entity ↔ Domain =====

    private ProductoEntity mapearAEntity(Producto producto) {
        ProductoEntity entity = new ProductoEntity(
                producto.getId(),
                producto.getCodigo(),
                producto.getNombre(),
                producto.getDescripcion(),
                TipoProductoEntity.valueOf(producto.getTipo().name()),
                producto.getPrecio(),
                producto.getFechaCreacion()
        );

        entity.setActivo(producto.isActivo());
        entity.setFechaActualizacion(producto.getFechaActualizacion());

        // Mapear stock si existe
        if (producto.getStock() != null) {
            Stock stock = producto.getStock();
            entity.setStock(new StockEntity(
                    stock.getCantidad(),
                    stock.getCantidadReservada(),
                    stock.getCantidadDisponible()
            ));
        }

        return entity;
    }

    private Producto mapearADominio(ProductoEntity entity) {
        // Reconstruir producto usando el factory method
        Producto producto = Producto.crear(
                entity.getId(),
                entity.getCodigo(),
                entity.getNombre(),
                entity.getDescripcion(),
                TipoProducto.valueOf(entity.getTipo().name()),
                entity.getPrecio()
        );

        // Restaurar stock si existe
        if (entity.getStock() != null) {
            StockEntity stockEntity = entity.getStock();
            Stock stock = Stock.crearConReservas(
                    stockEntity.getCantidad(),
                    stockEntity.getCantidadReservada()
            );
            producto.establecerStock(stock);
        }

        // Restaurar estado de activo
        if (!entity.isActivo()) {
            producto.desactivar();
        }

        return producto;
    }
}
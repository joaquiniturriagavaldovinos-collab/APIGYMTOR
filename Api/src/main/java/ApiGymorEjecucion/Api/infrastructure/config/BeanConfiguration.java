package ApiGymorEjecucion.Api.infrastructure.config;

import ApiGymorEjecucion.Api.domain.repository.*;
import ApiGymorEjecucion.Api.infrastructure.repository.memory.*;
import ApiGymorEjecucion.Api.infrastructure.repository.jpa.PedidoRepositoryJpa;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configuración de beans de Spring.
 * Define qué implementaciones de repositorios usar según el ambiente.
 */
@Configuration
@ComponentScan(basePackages = "ApiGymorEjecucion.Api")
public class BeanConfiguration {

    // ============ REPOSITORIOS PARA LOCAL (en memoria) ============

    @Bean
    @Profile("local")
    public PedidoRepository pedidoRepositoryLocal() {
        return new PedidoRepositoryInMemory();
    }

    @Bean
    @Profile("local")
    public ClienteRepository clienteRepositoryLocal() {
        return new ClienteRepositoryInMemory();
    }

    @Bean
    @Profile("local")
    public ProductoRepository productoRepositoryLocal() {
        return new ProductoRepositoryInMemory();
    }

    // ============ REPOSITORIOS PARA PRODUCCIÓN (base de datos) ============

    @Bean
    @Profile("prod")
    public PedidoRepository pedidoRepositoryProd(PedidoRepositoryJpa jpaRepo) {
        // Aquí necesitarás crear un adaptador que convierta entre
        // tu domain model (Pedido) y tu JPA entity (PedidoEntity)
        // Por ahora retorno el in-memory hasta que implementes JPA
        return new PedidoRepositoryInMemory();
        // TODO: return new PedidoRepositoryJpaAdapter(jpaRepo);
    }

    @Bean
    @Profile("prod")
    public ClienteRepository clienteRepositoryProd() {
        // TODO: Implementar versión con JPA
        return new ClienteRepositoryInMemory();
    }

    @Bean
    @Profile("prod")
    public ProductoRepository productoRepositoryProd() {
        // TODO: Implementar versión con JPA
        return new ProductoRepositoryInMemory();
    }
}
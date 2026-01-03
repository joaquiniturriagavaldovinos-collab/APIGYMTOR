package ApiGymorEjecucion.Api.infrastructure.config;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de beans de Spring.
 *
 * Spring Boot detecta automáticamente los componentes anotados con:
 * - @Service (casos de uso)
 * - @Repository (repositorios)
 * - @RestController (controllers)
 *
 * Esta clase puede usarse para configuraciones adicionales.
 */
@Configuration
@ComponentScan(basePackages = {
        "com.gymor.domain",
        "com.gymor.application",
        "com.gymor.infrastructure",
        "com.gymor.presentation"
})
public class BeanConfiguration {

    // Aquí puedes definir beans adicionales si es necesario
    // Por ejemplo: configuración de seguridad, CORS, etc.
}
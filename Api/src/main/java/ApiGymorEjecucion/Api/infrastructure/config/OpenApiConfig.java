package ApiGymorEjecucion.Api.infrastructure.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "GYMOR API",
                version = "v1",
                description = "API para gestión de clientes, pedidos, pagos y despachos"
        )
)
public class OpenApiConfig {
}
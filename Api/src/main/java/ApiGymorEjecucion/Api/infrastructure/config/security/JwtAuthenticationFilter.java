package ApiGymorEjecucion.Api.infrastructure.config.security;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("prod")
@Component
public class JwtAuthenticationFilter {  }

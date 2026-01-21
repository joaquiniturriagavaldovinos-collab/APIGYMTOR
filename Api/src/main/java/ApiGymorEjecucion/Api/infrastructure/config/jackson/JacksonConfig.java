package ApiGymorEjecucion.Api.infrastructure.config.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * Configuración de Jackson para serialización JSON
 *
 * Previene:
 * - Ciclos infinitos en referencias bidireccionales
 * - Problemas con fechas (LocalDateTime, LocalDate)
 * - Serialización de propiedades vacías
 */
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();

        // ===== FECHAS =====
        // Serializa LocalDateTime como ISO-8601 (2026-01-16T10:30:00)
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // ===== PREVENIR CICLOS INFINITOS =====
        // Si detecta ciclo, lanza excepción en vez de loop infinito
        objectMapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, true);

        // No serializa beans vacíos (opcional)
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // ===== PROPIEDADES NULL =====
        // No incluye campos null en el JSON (respuestas más limpias)
        // objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return objectMapper;
    }
}
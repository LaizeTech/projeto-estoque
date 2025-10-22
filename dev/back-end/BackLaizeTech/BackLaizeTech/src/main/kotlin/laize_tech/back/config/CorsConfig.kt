package laize_tech.back.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsConfig : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOriginPatterns("*") // Aceita qualquer origem
            .allowedMethods("*") // Aceita qualquer método HTTP
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3600)
    }
}
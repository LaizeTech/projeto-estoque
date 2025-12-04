package laize_tech.back.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.nio.file.Paths

@Configuration
class StaticResourceConfig : WebMvcConfigurer {

    @Value("\${file.upload-dir}")
    private lateinit var uploadDir: String

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        // 1. Resolve o caminho absoluto e garante que ele termine com uma barra.
        // O .toUri().toString() garante o formato correto para o Spring Boot.
        val absolutePathUri = Paths.get(uploadDir).toAbsolutePath().toUri().toString()

        // 2. Mapeia a URL /uploads/imagens/** para o caminho absoluto
        registry.addResourceHandler("/uploads/imagens/**")
            .addResourceLocations(absolutePathUri)

        // Log para verificação
        println("DEBUG: Mapeamento de Recursos Estáticos: /uploads/imagens/** -> $absolutePathUri")
    }
}
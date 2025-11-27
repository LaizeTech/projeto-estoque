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
        val resolvedPath = Paths.get(uploadDir).toAbsolutePath()
        val resourceLocation = "file:" + resolvedPath.toString() + "/"

        println("Servindo arquivos estáticos de: $resourceLocation") // Log para depuração

        registry.addResourceHandler("/uploads/imagens/**")
            .addResourceLocations(resourceLocation)
    }
}
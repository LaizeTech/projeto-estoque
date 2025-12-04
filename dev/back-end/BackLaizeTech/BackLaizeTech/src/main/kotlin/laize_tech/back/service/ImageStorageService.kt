package laize_tech.back.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*

@Service
class ImageStorageService(
    @Value("\${file.upload-dir}") private val uploadDir: String
) {
    @Throws(IOException::class)
    fun salvarImagem(file: MultipartFile?): String? {
        if (file == null || file.isEmpty) return null

        val nomeArquivo = "${UUID.randomUUID()}_${file.originalFilename}"
        val caminho: Path = Paths.get(uploadDir).resolve(nomeArquivo)

        // NOVO LOG DE VERIFICAÇÃO - Adicione esta linha
        println("DEBUG: Caminho Absoluto de Salvamento: ${caminho.toAbsolutePath()}")

        Files.createDirectories(caminho.parent)
        Files.copy(file.inputStream, caminho, StandardCopyOption.REPLACE_EXISTING)

        return nomeArquivo
    }
}
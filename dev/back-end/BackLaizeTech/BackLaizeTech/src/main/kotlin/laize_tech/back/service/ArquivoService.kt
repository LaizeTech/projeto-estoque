package laize_tech.back.service

import laize_tech.back.entity.Arquivo
import laize_tech.back.repository.ArquivoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ArquivoService(
    private val arquivoRepository: ArquivoRepository,
    @Autowired(required = false) private val storageService: StorageService?
) {

    fun salvarArquivo(file: MultipartFile): Arquivo {
        val url = if (storageService != null) {
            storageService.uploadFile(file)
        } else {
            "mock-url-${file.originalFilename}" 
        }
        
        val arquivo = Arquivo(
            nomeOriginal = file.originalFilename ?: "desconhecido",
            tipo = file.contentType ?: "desconhecido",
            url = url
        )
        return arquivoRepository.save(arquivo)
    }

    fun listarArquivos(): List<Arquivo> = arquivoRepository.findAll()

    fun buscarArquivo(id: Int): Arquivo = arquivoRepository.findById(id)
        .orElseThrow { NoSuchElementException("Arquivo não encontrado") }

    fun deletarArquivo(id: Int) = arquivoRepository.deleteById(id)
}

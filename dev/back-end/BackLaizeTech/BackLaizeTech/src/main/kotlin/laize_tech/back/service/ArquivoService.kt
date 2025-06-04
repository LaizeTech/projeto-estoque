package laize_tech.back.service

import laize_tech.back.entity.Arquivo
import laize_tech.back.repository.ArquivoRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ArquivoService(
    private val arquivoRepository: ArquivoRepository,
    private val storageService: StorageService
) {

    fun salvarArquivo(file: MultipartFile): Arquivo {
        val url = storageService.uploadFile(file)
        val arquivo = Arquivo(
            nomeOriginal = file.originalFilename ?: "desconhecido",
            tipo = file.contentType ?: "desconhecido",
            url = url
        )
        return arquivoRepository.save(arquivo)
    }

    fun listarArquivos(): List<Arquivo> = arquivoRepository.findAll()

    fun buscarArquivo(id: Long): Arquivo = arquivoRepository.findById(id)
        .orElseThrow { NoSuchElementException("Arquivo não encontrado") }

    fun deletarArquivo(id: Long) = arquivoRepository.deleteById(id)
}

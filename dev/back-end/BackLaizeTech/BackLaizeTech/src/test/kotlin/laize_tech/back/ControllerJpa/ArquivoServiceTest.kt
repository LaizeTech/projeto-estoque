import laize_tech.back.entity.Arquivo
import laize_tech.back.repository.ArquivoRepository
import laize_tech.back.service.ArquivoService
import laize_tech.back.service.StorageService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.mock.web.MockMultipartFile
import java.util.*

class ArquivoServiceTest {
    private val arquivoRepository = mock(ArquivoRepository::class.java)
    private val storageService = mock(StorageService::class.java)
    private val service = ArquivoService(arquivoRepository, storageService)

    @Test
    fun `deve salvar arquivo com sucesso`() {
        val multipartFile = MockMultipartFile(
            "file",
            "test.txt",
            "text/plain",
            "test content".toByteArray()
        )
        val url = "https://raw.s3.amazonaws.com/uploads/test.txt"
        val arquivo = Arquivo(
            id = 1,
            nomeOriginal = "test.txt",
            tipo = "text/plain",
            url = url
        )

        `when`(storageService.uploadFile(multipartFile)).thenReturn(url)
        `when`(arquivoRepository.save(any())).thenReturn(arquivo)

        val resultado = service.salvarArquivo(multipartFile)

        assertEquals(arquivo, resultado)
        verify(storageService).uploadFile(multipartFile)
        verify(arquivoRepository).save(any())
    }

    @Test
    fun `deve listar todos os arquivos`() {
        val arquivos = listOf(
            Arquivo(1, "arquivo1.txt", "text/plain", "url1"),
            Arquivo(2, "arquivo2.txt", "text/plain", "url2")
        )

        `when`(arquivoRepository.findAll()).thenReturn(arquivos)

        val resultado = service.listarArquivos()

        assertEquals(arquivos, resultado)
        verify(arquivoRepository).findAll()
    }

    @Test
    fun `deve buscar arquivo por id com sucesso`() {
        val arquivo = Arquivo(1, "arquivo.txt", "text/plain", "url")

        `when`(arquivoRepository.findById(1)).thenReturn(Optional.of(arquivo))

        val resultado = service.buscarArquivo(1)

        assertEquals(arquivo, resultado)
        verify(arquivoRepository).findById(1)
    }

    @Test
    fun `deve lançar exceção quando arquivo não encontrado`() {
        `when`(arquivoRepository.findById(99)).thenReturn(Optional.empty())

        assertThrows(NoSuchElementException::class.java) {
            service.buscarArquivo(99)
        }
    }

    @Test
    fun `deve deletar arquivo com sucesso`() {
        doNothing().`when`(arquivoRepository).deleteById(1)

        service.deletarArquivo(1)

        verify(arquivoRepository).deleteById(1)
    }
}
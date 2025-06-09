import laize_tech.back.ControllerJpa.ArquivoController
import laize_tech.back.entity.Arquivo
import laize_tech.back.service.ArquivoService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockMultipartFile

class ArquivoControllerTest {
    private val arquivoService = mock(ArquivoService::class.java)
    private val controller = ArquivoController(arquivoService)

    @Test
    fun `deve fazer upload de arquivo com sucesso`() {
        val multipartFile = MockMultipartFile(
            "file",
            "test.txt",
            "text/plain",
            "test content".toByteArray()
        )
        val arquivo = Arquivo(1, "test.txt", "text/plain", "url")

        `when`(arquivoService.salvarArquivo(multipartFile)).thenReturn(arquivo)

        val resultado = controller.uploadArquivo(multipartFile)

        assertEquals(arquivo, resultado)
        verify(arquivoService).salvarArquivo(multipartFile)
    }

    @Test
    fun `deve listar todos os arquivos`() {
        val arquivos = listOf(
            Arquivo(1, "arquivo1.txt", "text/plain", "url1"),
            Arquivo(2, "arquivo2.txt", "text/plain", "url2")
        )

        `when`(arquivoService.listarArquivos()).thenReturn(arquivos)

        val resultado = controller.listarArquivos()

        assertEquals(arquivos, resultado)
        verify(arquivoService).listarArquivos()
    }

    @Test
    fun `deve buscar arquivo por id`() {
        val arquivo = Arquivo(1, "arquivo.txt", "text/plain", "url")

        `when`(arquivoService.buscarArquivo(1)).thenReturn(arquivo)

        val resultado = controller.buscarArquivo(1)

        assertEquals(arquivo, resultado)
        verify(arquivoService).buscarArquivo(1)
    }

    @Test
    fun `deve deletar arquivo`() {
        doNothing().`when`(arquivoService).deletarArquivo(1)

        controller.deletarArquivo(1)

        verify(arquivoService).deletarArquivo(1)
    }
}
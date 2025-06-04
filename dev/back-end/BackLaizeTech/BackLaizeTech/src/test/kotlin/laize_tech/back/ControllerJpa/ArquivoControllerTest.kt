package laize_tech.back.ControllerJpa

import laize_tech.back.entity.Arquivo
import laize_tech.back.service.ArquivoService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import org.springframework.web.multipart.MultipartFile

class ArquivoControllerTest {

    private val arquivoService: ArquivoService = mock()
    private val arquivoController = ArquivoController(arquivoService)

    @Nested
    inner class UploadArquivo {

        @Test
        fun retornaArquivoCriadoAoFazerUpload() {
            val mockFile: MultipartFile = mock()
            val expectedArquivo = Arquivo(1L, "file.txt", "text/plain", "url")
            whenever(arquivoService.salvarArquivo(mockFile)).thenReturn(expectedArquivo)

            val result = arquivoController.uploadArquivo(mockFile)

            assertEquals(expectedArquivo, result)
        }

        @Test
        fun lancaExcecaoParaArquivoInvalido() {
            val mockFile: MultipartFile = mock()
            whenever(arquivoService.salvarArquivo(mockFile)).thenThrow(IllegalArgumentException("Invalid file"))

            val exception = assertThrows<IllegalArgumentException> {
                arquivoController.uploadArquivo(mockFile)
            }

            assertEquals("Invalid file", exception.message)
        }
    }

    @Nested
    inner class ListarArquivos {

        @Test
        fun retornaListaDeArquivos() {
            val arquivos = listOf(
                Arquivo(1L, "file1.txt", "text/plain", "url1"),
                Arquivo(2L, "file2.txt", "text/plain", "url2")
            )
            whenever(arquivoService.listarArquivos()).thenReturn(arquivos)

            val result = arquivoController.listarArquivos()

            assertEquals(arquivos, result)
        }

        @Test
        fun retornaListaVaziaQuandoNaoExistemArquivos() {
            whenever(arquivoService.listarArquivos()).thenReturn(emptyList())

            val result = arquivoController.listarArquivos()

            assertTrue(result.isEmpty())
        }
    }

    @Nested
    inner class BuscarArquivo {

        @Test
        fun retornaArquivoParaIdValido() {
            val arquivo = Arquivo(1L, "file.txt", "text/plain", "url")
            whenever(arquivoService.buscarArquivo(1L)).thenReturn(arquivo)

            val result = arquivoController.buscarArquivo(1L)

            assertEquals(arquivo, result)
        }

        @Test
        fun lancaExcecaoParaIdInvalido() {
            whenever(arquivoService.buscarArquivo(99L)).thenThrow(NoSuchElementException("Arquivo não encontrado"))

            val exception = assertThrows<NoSuchElementException> {
                arquivoController.buscarArquivo(99L)
            }

            assertEquals("Arquivo não encontrado", exception.message)
        }
    }

    @Nested
    inner class DeletarArquivo {

        @Test
        fun deletaArquivoParaIdValido() {
            doNothing().whenever(arquivoService).deletarArquivo(1L)

            arquivoController.deletarArquivo(1L)

            verify(arquivoService, times(1)).deletarArquivo(1L)
        }

        @Test
        fun lancaExcecaoParaIdInvalido() {
            doThrow(NoSuchElementException("Arquivo não encontrado")).whenever(arquivoService).deletarArquivo(99L)

            val exception = assertThrows<NoSuchElementException> {
                arquivoController.deletarArquivo(99L)
            }

            assertEquals("Arquivo não encontrado", exception.message)
        }
    }

    @Nested
    inner class AtualizarArquivo {

        @Test
        fun atualizaArquivoParaIdValido() {
            val arquivoAtualizado = Arquivo(1L, "updated_file.txt", "text/plain", "url")
            whenever(arquivoService.atualizarArquivo(eq(1L), any())).thenReturn(arquivoAtualizado)

            val result = arquivoController.atualizarArquivo(1L, arquivoAtualizado)

            assertEquals(arquivoAtualizado, result)
        }

        @Test
        fun lancaExcecaoAoAtualizarArquivoComIdInvalido() {
            val arquivoAtualizado = Arquivo(99L, "updated_file.txt", "text/plain", "url")
            whenever(arquivoService.atualizarArquivo(eq(99L), any()))
                .thenThrow(NoSuchElementException("Arquivo não encontrado"))

            val exception = assertThrows<NoSuchElementException> {
                arquivoController.atualizarArquivo(99L, arquivoAtualizado)
            }

            assertEquals("Arquivo não encontrado", exception.message)
        }

        @Test
        fun lancaExcecaoAoAtualizarArquivoComDadosInvalidos() {
            val arquivoAtualizado = Arquivo(1L, "", "text/plain", "url")
            whenever(arquivoService.atualizarArquivo(eq(1L), any()))
                .thenThrow(IllegalArgumentException("Dados inválidos"))

            val exception = assertThrows<IllegalArgumentException> {
                arquivoController.atualizarArquivo(1L, arquivoAtualizado)
            }

            assertEquals("Dados inválidos", exception.message)
        }
    }
}

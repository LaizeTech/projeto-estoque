import laize_tech.back.ControllerJpa.ItensSaidaJpaController
import laize_tech.back.ControllerJpa.ProdutoJpaController
import laize_tech.back.dto.ProdutoDTO
import laize_tech.back.entity.Categoria
import laize_tech.back.entity.Produto
import laize_tech.back.exceptions.IdNaoEncontradoException
import laize_tech.back.repository.CategoriaRepository
import laize_tech.back.repository.ProdutoRepository
import laize_tech.back.service.UploadService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockMultipartFile
import java.time.LocalDateTime
import java.util.*

class ProdutoJpaControllerTest {

    private val produtoRepository = mock(ProdutoRepository::class.java)
    private val categoriaRepository = mock(CategoriaRepository::class.java)
    private val uploadService = mock(UploadService::class.java)
    private val itensSaidaJpaController = mock(ItensSaidaJpaController::class.java)
    private val controller = ProdutoJpaController(
        produtoRepository,
        categoriaRepository,
        uploadService,
        itensSaidaJpaController,
        itensSaidaJpaController
    )

    @Test
    fun `retorna lista de produtos quando existem registros`() {
        val produtos = listOf(
            Produto(
                idProduto = 1L,
                categoria = Categoria(),
                nomeProduto = "Produto 1",
                quantidadeProduto = 10,
                sku = "SKU1",
                statusAtivo = true,
                dtRegistro = LocalDateTime.now(),
                quantidade = 10
            ),
            Produto(
                idProduto = 2L,
                categoria = Categoria(),
                nomeProduto = "Produto 2",
                quantidadeProduto = 20,
                sku = "SKU2",
                statusAtivo = true,
                dtRegistro = LocalDateTime.now(),
                quantidade = 20
            )
        )
        `when`(produtoRepository.findAll()).thenReturn(produtos)

        val response = controller.get()

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(produtos, response.body)
    }

    @Test
    fun `retorna no content quando nao existem produtos`() {
        `when`(produtoRepository.findAll()).thenReturn(emptyList())

        val response = controller.get()

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        assertNull(response.body)
    }

    @Test
    fun `retorna not found ao criar produto com categoria inexistente`() {
        val dto = ProdutoDTO(
            idCategoria = 99L,
            nomeProduto = "Novo Produto",
            quantidadeProduto = 10,
            sku = "SKU123",
            statusAtivo = true
        )

        `when`(categoriaRepository.findById(99)).thenReturn(Optional.empty())

        val exception = assertThrows(IdNaoEncontradoException::class.java) {
            controller.post(dto)
        }

        assertEquals("Não foi possível encontrar Categoria com o ID 99", exception.message)
    }

    @Test
    fun `atualiza produto existente com sucesso`() {
        val id = 1
        val dto = ProdutoDTO(
            idCategoria = 1L,
            nomeProduto = "Produto Atualizado",
            quantidadeProduto = 20,
            sku = "SKU456",
            statusAtivo = true
        )
        val categoria = Categoria(1, "Categoria Test")
        val produtoExistente = Produto(
            idProduto = 1L,
            categoria = categoria,
            nomeProduto = "Produto Original",
            quantidadeProduto = 10,
            sku = "SKU123",
            statusAtivo = true,
            dtRegistro = LocalDateTime.now(),
            quantidade = 10
        )

        `when`(produtoRepository.findById(id.toLong())).thenReturn(Optional.of(produtoExistente))
        `when`(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria))
        `when`(produtoRepository.save(any())).thenReturn(produtoExistente.copy(
            nomeProduto = "Produto Atualizado",
            quantidadeProduto = 20,
            sku = "SKU456"
        ))

        val response = controller.put(id, dto)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("Produto Atualizado", (response.body as Produto).nomeProduto)
    }

    @Test
    fun `retorna bad request ao atualizar produto com nome em branco`() {
        val id = 1
        val dto = ProdutoDTO(
            idCategoria = 1L,
            nomeProduto = "",
            quantidadeProduto = 20,
            sku = "SKU456",
            statusAtivo = true
        )
        val produtoExistente = Produto(
            idProduto = 1L,
            categoria = Categoria(),
            nomeProduto = "Produto Original",
            quantidadeProduto = 10,
            sku = "SKU123",
            statusAtivo = true,
            dtRegistro = LocalDateTime.now(),
            quantidade = 10
        )

        `when`(produtoRepository.findById(id.toLong())).thenReturn(Optional.of(produtoExistente))

        val response = controller.put(id, dto)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals("Nome do produto não pode estar em branco", response.body)
    }

    @Test
    fun `remove produto existente com sucesso`() {
        val id = 1L
        `when`(produtoRepository.existsById(id)).thenReturn(true)
        doNothing().`when`(produtoRepository).deleteById(id)

        val response = controller.delete(id)

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
    }

    @Test
    fun `retorna not found ao remover produto inexistente`() {
        val id = 99L
        `when`(produtoRepository.existsById(id)).thenReturn(false)

        val response = controller.delete(id)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertEquals("Não foi possível deletar o produto com id $id", response.body)
    }

    @Test
    fun `upload csv com sucesso`() {
        val file = MockMultipartFile(
            "file",
            "test.csv",
            "text/csv",
            "content".toByteArray()
        )
        val produtos = listOf(
            Produto(
                idProduto = 1L,
                categoria = Categoria(),
                nomeProduto = "Produto 1",
                quantidadeProduto = 10,
                sku = "SKU1",
                statusAtivo = true,
                dtRegistro = LocalDateTime.now(),
                quantidade = 10
            )
        )

        `when`(uploadService.uploadFileAndProcess(file)).thenReturn(produtos)

        val response = controller.uploadCsv(file)

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(produtos, response.body)
    }

    @Test
    fun `retorna bad request ao enviar arquivo vazio`() {
        val file = MockMultipartFile(
            "file",
            "test.csv",
            "text/csv",
            ByteArray(0)
        )

        val response = controller.uploadCsv(file)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    @Test
    fun `retorna unsupported media type ao enviar arquivo não csv`() {
        val file = MockMultipartFile(
            "file",
            "test.txt",
            "text/plain",
            "content".toByteArray()
        )

        val response = controller.uploadCsv(file)

        assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE, response.statusCode)
    }
}
package laize_tech.back.ControllerJpa

import laize_tech.back.ControllerJpa.CompraProdutoJpaController
import laize_tech.back.dto.CompraProdutoDTO
import laize_tech.back.entity.Categoria
import laize_tech.back.entity.CompraProduto
import laize_tech.back.entity.Produto
import laize_tech.back.exceptions.IdNaoEncontradoException
import laize_tech.back.repository.CompraProdutoRepository
import laize_tech.back.repository.ProdutoRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

class CompraProdutoJpaControllerTest {

    private val compraProdutoRepository = mock(CompraProdutoRepository::class.java)
    private val produtoRepository = mock(ProdutoRepository::class.java)
    private val controller = CompraProdutoJpaController(compraProdutoRepository, produtoRepository)

    @BeforeEach
    fun configurar() {
        val categoria = Categoria(1, "Categoria Teste")
        val produto = Produto(
            idProduto = 1,
            categoria = categoria,
            nomeProduto = "Produto Teste",
            quantidadeProduto = 50,
            statusAtivo = true,
            caminhoImagem = null,
            dtRegistro = LocalDateTime.now()
        )

        val compraProduto = listOf(
            CompraProduto(
                idCompraProduto = 1,
                fornecedor = "fornecedor teste",
                precoCompra = BigDecimal("10.50"),
                dtCompra = LocalDateTime.now(),
                quantidadeProduto = 10,
                produto = produto
            )
        )

    private val compraProdutoRepository = mock(CompraProdutoRepository::class.java)
    private val produtoRepository = mock(ProdutoRepository::class.java)
    private val controller = CompraProdutoJpaController(compraProdutoRepository, produtoRepository)

    private fun criarProdutoMock() = Produto(
        idProduto = 1L,
        categoria = Categoria(1, "Categoria Test"),
        nomeProduto = "Produto Test",
        quantidadeProduto = 100,
        caminhoImagem = "/images/produto-test.jpg",
        statusAtivo = true,
        dtRegistro = LocalDateTime.now()
    )

    @Test
    fun `retorna lista de compras quando existem registros`() {
        val produto = criarProdutoMock()
        val compras = listOf(
            CompraProduto(
                idCompraProduto = 1,
                fornecedor = "Fornecedor 1",
                precoCompra = 100.0,
                dtCompra = LocalDate.now(),
                quantidadeProduto = 10,
                produto = produto
            )
        )
        `when`(compraProdutoRepository.findAll()).thenReturn(compras)

        val response = controller.get()

        assertEquals(HttpStatus.OK.value(), response.statusCode.value())
        assertEquals(compras, response.body)
    }

    @Test
    fun `retorna no content quando nao existem compras`() {
        `when`(compraProdutoRepository.findAll()).thenReturn(emptyList())

        val response = controller.get()

        assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode.value())
        assertNull(response.body)
    }

    @Test
    fun `cria nova compra com sucesso`() {
        val produto = criarProdutoMock()
        val dto = CompraProdutoDTO(
            idProduto = 1,
            fornecedor = "Fornecedor Test",
            precoCompra = 100.0,
            dtCompra = LocalDate.now(),
            quantidadeProduto = 10
        )
        val compra = CompraProduto(
            fornecedor = dto.fornecedor,
            precoCompra = dto.precoCompra,
            dtCompra = dto.dtCompra!!,
            quantidadeProduto = dto.quantidadeProduto,
            produto = produto
        )

        `when`(produtoRepository.findById(1L)).thenReturn(Optional.of(produto))
        `when`(compraProdutoRepository.save(any())).thenReturn(compra)

        val response = controller.adicionarCompra(dto)

        assertEquals(HttpStatus.CREATED.value(), response.statusCode.value())
        assertEquals(compra, response.body)
    }

    @Test
    fun `retorna erro ao criar compra com produto inexistente`() {
        val dto = CompraProdutoDTO(
            idProduto = 99,
            fornecedor = "Fornecedor Test",
            precoCompra = 100.0,
            dtCompra = LocalDate.now(),
            quantidadeProduto = 10
        )

        `when`(produtoRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows(IdNaoEncontradoException::class.java) {
            controller.adicionarCompra(dto)
        }
    }

    @Test
    fun `atualiza compra existente com sucesso`() {
        val id = 1
        val produto = criarProdutoMock()
        val compraAtualizada = CompraProduto(
            idCompraProduto = id,
            fornecedor = "Fornecedor Atualizado",
            precoCompra = 150.0,
            dtCompra = LocalDate.now(),
            quantidadeProduto = 20,
            produto = produto
        )

        val compraExistente = compraAtualizada.copy()

        `when`(compraProdutoRepository.existsById(id)).thenReturn(true)
        `when`(compraProdutoRepository.findById(id)).thenReturn(Optional.of(compraExistente))
        `when`(compraProdutoRepository.save(any())).thenReturn(compraAtualizada)

        val response = controller.put(id, compraAtualizada)

        assertEquals(HttpStatus.OK.value(), response.statusCode.value())
        assertEquals(compraAtualizada, response.body)
    }

    @Test
    fun `retorna not found ao atualizar compra inexistente`() {
        val id = 99
        val produto = criarProdutoMock()
        val compraAtualizada = CompraProduto(
            idCompraProduto = id,
            fornecedor = "Fornecedor Test",
            precoCompra = 100.0,
            dtCompra = LocalDate.now(),
            quantidadeProduto = 10,
            produto = produto
        )

        `when`(compraProdutoRepository.existsById(id)).thenReturn(false)

        val response = controller.put(id, compraAtualizada)

        assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode.value())
        assertNull(response.body)
    }

    @Test
    fun `remove compra existente com sucesso`() {
        val id = 1
        `when`(compraProdutoRepository.existsById(id)).thenReturn(true)
        doNothing().`when`(compraProdutoRepository).deleteById(id)

        val response = controller.delete(id)

        assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode.value())
        assertNull(response.body)
    }

    @Test
    fun `retorna not found ao remover compra inexistente`() {
        val id = 99
        `when`(compraProdutoRepository.existsById(id)).thenReturn(false)

        val response = controller.delete(id)

        assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode.value())
        assertEquals("Não foi possível deletar a compra com id $id", response.body)
    }
}
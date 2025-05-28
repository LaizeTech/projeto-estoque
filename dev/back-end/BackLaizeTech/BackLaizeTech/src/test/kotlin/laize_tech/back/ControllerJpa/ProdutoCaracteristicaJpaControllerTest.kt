package laize_tech.back.ControllerJpa

import laize_tech.back.dto.ProdutoCaracteristicaDTO
import laize_tech.back.entity.Caracteristica
import laize_tech.back.entity.Produto
import laize_tech.back.entity.ProdutoCaracteristica
import laize_tech.back.entity.TipoCaracteristica
import laize_tech.back.exceptions.IdNaoEncontradoException
import laize_tech.back.repository.CaracteristicaRepository
import laize_tech.back.repository.ProdutoCaracteristicaRepository
import laize_tech.back.repository.ProdutoRepository
import laize_tech.back.repository.TipoCaracteristicaRepository
import org.mockito.kotlin.any
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.function.Executable
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import java.util.*

class ProdutoCaracteristicaJpaControllerTest {

    val repositorio: ProdutoCaracteristicaRepository = mock(ProdutoCaracteristicaRepository::class.java)

    val tipoCaracteristicaRepository: TipoCaracteristicaRepository = mock(TipoCaracteristicaRepository::class.java)
    val caracteristicaRepository: CaracteristicaRepository = mock(CaracteristicaRepository::class.java)
    val produtoRepository: ProdutoRepository = mock(ProdutoRepository::class.java)

    val controller = ProdutoCaracteristicaJpaController(
        repositorio,
        tipoCaracteristicaRepository,
        caracteristicaRepository,
        produtoRepository
    )

    val tipoCaracteristica = mock(TipoCaracteristica::class.java)
    val caracteristica = mock(Caracteristica::class.java)
    val produto = mock(Produto::class.java)

    @BeforeEach
    fun setup() {
        val produtos = listOf(ProdutoCaracteristica(idProdutoCaracteristica = 1, quantidadeProdutoCaracteristica = 10))

        `when`(repositorio.existsById(1)).thenReturn(true)
        `when`(repositorio.existsById(99)).thenReturn(false)

        `when`(repositorio.findByCaracteristica_Id(1)).thenReturn(produtos)
        `when`(repositorio.findByCaracteristica_Id(99)).thenReturn(emptyList())
    }

    @Test
    @DisplayName("get all quando não há dados no repositório deve retornar status 204")
    fun getProdutosWhenRepositoryIsEmptyReturnsNoContent() {
        `when`(repositorio.findAll()).thenReturn(emptyList())

        val response = controller.get()

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
    }

    @Test
    @DisplayName("get all quando há dados no repositório deve retornar status 200 com a lista de produtos")
    fun getProdutosWhenRepositoryHasDataReturnsOkWithProdutos() {
        `when`(repositorio.findAll()).thenReturn(mutableListOf(mock(ProdutoCaracteristica::class.java)))

        val response = controller.get()

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(1, response.body?.size)
    }

    @Test
    @DisplayName("getProdutoPorCaracteristica quando o código é válido deve retornar status 200 com a lista de produtos")
    fun getProdutoPorCaracteristicaWithValidCodigoReturnsProdutos() {
        val response = controller.getProdutoPorCaracteristica(1)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(1, response.body?.size
        )
    }

    @Test
    @DisplayName("getProdutoPorCaracteristica quando o código não existe deve lançar exceção")
    fun getProdutoPorCaracteristicaWithNonExistentCodigoThrowsException() {
        val exception = assertThrows(IdNaoEncontradoException::class.java, Executable {
            controller.getProdutoPorCaracteristica(99)
        })

        assertEquals("Não foi possível encontrar Caracteristica com o ID 99", exception.message)
    }

    @Test
    @DisplayName("post quando todos os dados são válidos deve retornar status 201 com o produto criado")
    fun postWithValidDataReturnsCreatedWithProduto() {
        val novoProduto = ProdutoCaracteristicaDTO(
            idTipoCaracteristica = 1,
            idCaracteristica = 1,
            idProduto = 1,
            quantidadeProduto = 10
        )

        val produtoCaracteristica = ProdutoCaracteristica(
            tipoCaracteristica = tipoCaracteristica,
            caracteristica = caracteristica,
            produto = produto,
            quantidadeProdutoCaracteristica = 10
        )

        `when`(tipoCaracteristicaRepository.findById(1)).thenReturn(Optional.of(tipoCaracteristica))
        `when`(caracteristicaRepository.findById(1)).thenReturn(Optional.of(caracteristica))
        `when`(produtoRepository.findById(1)).thenReturn(Optional.of(produto))
        `when`(repositorio.save(any())).thenReturn(produtoCaracteristica)

        val response = controller.post(novoProduto)

        assertEquals(201, response.statusCodeValue)
        assertEquals(produtoCaracteristica, response.body)
    }

    @Test
    @DisplayName("post quando o idTipoCaracteristica não existe deve lançar IdNaoEncontradoException")
    fun postWithNonExistentTipoCaracteristicaThrowsException() {
        val novoProduto = ProdutoCaracteristicaDTO(
            idTipoCaracteristica = 99,
            idCaracteristica = 1,
            idProduto = 1,
            quantidadeProduto = 10
        )

        `when`(tipoCaracteristicaRepository.findById(99)).thenReturn(Optional.empty())

        val exception = assertThrows(IdNaoEncontradoException::class.java, Executable {
            controller.post(novoProduto)
        })

        assertEquals("Não foi possível encontrar TipoCaracteristica com o ID 99", exception.message)
    }

    @Test
    @DisplayName("post quando o idCaracteristica não existe deve lançar IdNaoEncontradoException")
    fun postWithNonExistentCaracteristicaThrowsException() {
        val novoProduto = ProdutoCaracteristicaDTO(
            idTipoCaracteristica = 1,
            idCaracteristica = 99,
            idProduto = 1,
            quantidadeProduto = 10
        )
        val tipoCaracteristica = mock(TipoCaracteristica::class.java)

        `when`(tipoCaracteristicaRepository.findById(1)).thenReturn(Optional.of(tipoCaracteristica))
        `when`(caracteristicaRepository.findById(99)).thenReturn(Optional.empty())

        val exception = assertThrows(IdNaoEncontradoException::class.java, Executable {
            controller.post(novoProduto)
        })

        assertEquals("Não foi possível encontrar Caracteristica com o ID 99", exception.message)
    }

    @Test
    @DisplayName("post quando o idProduto não existe deve lançar IdNaoEncontradoException")
    fun postWithNonExistentProdutoThrowsException() {
        val novoProduto = ProdutoCaracteristicaDTO(
            idProduto = 99,
            idTipoCaracteristica = 1,
            idCaracteristica = 1,
            quantidadeProduto = 10
        )

        val exception = assertThrows(IdNaoEncontradoException::class.java, Executable {
            `when`(tipoCaracteristicaRepository.findById(1)).thenReturn(Optional.of(tipoCaracteristica))
            `when`(caracteristicaRepository.findById(1)).thenReturn(Optional.of(caracteristica))
            `when`(produtoRepository.findById(99)).thenReturn(Optional.empty())
            controller.post(novoProduto)
        })

        assertEquals("Não foi possível encontrar Produto com o ID 99", exception.message)
    }

    @Test
    @DisplayName("put quando o código é válido deve atualizar e retornar o produto atualizado")
    fun putWithValidCodigoUpdatesAndReturnsProduto() {
        val codigo = 1
        val novoProduto = ProdutoCaracteristicaDTO(
            idTipoCaracteristica = 1,
            idCaracteristica = 1,
            idProduto = 1,
            quantidadeProduto = 20
        )
        val existente = ProdutoCaracteristica(
            idProdutoCaracteristica = codigo,
            tipoCaracteristica = tipoCaracteristica,
            caracteristica = caracteristica,
            produto = produto,
            quantidadeProdutoCaracteristica = 10
        )

        `when`(repositorio.findById(codigo)).thenReturn(Optional.of(existente))
        `when`(tipoCaracteristicaRepository.findById(1)).thenReturn(Optional.of(tipoCaracteristica))
        `when`(caracteristicaRepository.findById(1)).thenReturn(Optional.of(caracteristica))
        `when`(produtoRepository.findById(1)).thenReturn(Optional.of(produto))
        `when`(repositorio.save(any())).thenReturn(existente)

        val response = controller.put(codigo, novoProduto)

        assertEquals(200, response.statusCodeValue)
        assertEquals(20, response.body?.quantidadeProdutoCaracteristica)
    }

    @Test
    @DisplayName("put quando o código não existe deve retornar status 404")
    fun putWithNonExistentCodigoReturnsNotFound() {
        val codigo = 99
        val novoProduto = ProdutoCaracteristicaDTO(
            idTipoCaracteristica = 1,
            idCaracteristica = 1,
            idProduto = 1,
            quantidadeProduto = 10
        )

        `when`(repositorio.findById(codigo)).thenReturn(Optional.empty())

        val response = controller.put(codigo, novoProduto)

        assertEquals(404, response.statusCodeValue)
    }

    @Test
    @DisplayName("put quando o idTipoCaracteristica não existe deve lançar IdNaoEncontradoException")
    fun putWithNonExistentTipoCaracteristicaThrowsException() {
        val codigo = 1
        val novoProduto = ProdutoCaracteristicaDTO(
            idTipoCaracteristica = 99,
            idCaracteristica = 1,
            idProduto = 1,
            quantidadeProduto = 10
        )
        val existente = ProdutoCaracteristica(
            idProdutoCaracteristica = codigo,
            tipoCaracteristica = tipoCaracteristica,
            caracteristica = caracteristica,
            produto = produto,
            quantidadeProdutoCaracteristica = 10
        )

        `when`(repositorio.findById(codigo)).thenReturn(Optional.of(existente))
        `when`(tipoCaracteristicaRepository.findById(99)).thenReturn(Optional.empty())

        val exception = assertThrows(IdNaoEncontradoException::class.java, Executable {
            controller.put(codigo, novoProduto)
        })

        assertEquals("Não foi possível encontrar TipoCaracteristica com o ID 99", exception.message)
    }

    @Test
    @DisplayName("put quando o idCaracteristica não existe deve lançar IdNaoEncontradoException")
    fun putWithNonExistentCaracteristicaThrowsException() {
        val codigo = 1
        val novoProduto = ProdutoCaracteristicaDTO(
            idTipoCaracteristica = 1,
            idCaracteristica = 99,
            idProduto = 1,
            quantidadeProduto = 10
        )
        val existente = ProdutoCaracteristica(
            idProdutoCaracteristica = codigo,
            tipoCaracteristica = tipoCaracteristica,
            caracteristica = caracteristica,
            produto = produto,
            quantidadeProdutoCaracteristica = 10
        )

        `when`(repositorio.findById(codigo)).thenReturn(Optional.of(existente))
        `when`(tipoCaracteristicaRepository.findById(1)).thenReturn(Optional.of(tipoCaracteristica))
        `when`(caracteristicaRepository.findById(99)).thenReturn(Optional.empty())

        val exception = assertThrows(IdNaoEncontradoException::class.java, Executable {
            controller.put(codigo, novoProduto)
        })

        assertEquals("Não foi possível encontrar Caracteristica com o ID 99", exception.message)
    }

    @Test
    @DisplayName("put quando o idProduto não existe deve lançar IdNaoEncontradoException")
    fun putWithNonExistentProdutoThrowsException() {
        val codigo = 1
        val novoProduto = ProdutoCaracteristicaDTO(
            idTipoCaracteristica = 1,
            idCaracteristica = 1,
            idProduto = 99,
            quantidadeProduto = 10
        )
        val existente = ProdutoCaracteristica(
            idProdutoCaracteristica = codigo,
            tipoCaracteristica = tipoCaracteristica,
            caracteristica = caracteristica,
            produto = produto,
            quantidadeProdutoCaracteristica = 10
        )

        `when`(repositorio.findById(codigo)).thenReturn(Optional.of(existente))
        `when`(tipoCaracteristicaRepository.findById(1)).thenReturn(Optional.of(tipoCaracteristica))
        `when`(caracteristicaRepository.findById(1)).thenReturn(Optional.of(caracteristica))
        `when`(produtoRepository.findById(99)).thenReturn(Optional.empty())

        val exception = assertThrows(IdNaoEncontradoException::class.java, Executable {
            controller.put(codigo, novoProduto)
        })

        assertEquals("Não foi possível encontrar Produto com o ID 99", exception.message)
    }

    @Test
    @DisplayName("delete quando o código existe deve excluir e retornar status 204")
    fun deleteWithExistingCodigoDeletesAndReturnsNoContent() {
        val codigo = 1

        val response = controller.delete(codigo)

        assertEquals(204, response.statusCodeValue)
        verify(repositorio).deleteById(codigo)
    }

    @Test
    @DisplayName("delete quando o código não existe deve retornar status 404")
    fun deleteWithNonExistentCodigoReturnsNotFound() {
        val codigo = 99

        val response = controller.delete(codigo)

        assertEquals(404, response.statusCodeValue)
        verify(repositorio, never()).deleteById(codigo)
    }

    @Test
    @DisplayName("patchQuantidade quando o código é válido deve atualizar e retornar o produto atualizado")
    fun patchQuantidadeWithValidCodigoUpdatesAndReturnsProduto() {
        val codigo = 1
        val quantidade = 15
        val produto = ProdutoCaracteristica(
            idProdutoCaracteristica = codigo,
            tipoCaracteristica = tipoCaracteristica,
            caracteristica = caracteristica,
            produto = produto,
            quantidadeProdutoCaracteristica = 10
        )

        `when`(repositorio.findById(codigo)).thenReturn(Optional.of(produto))
        `when`(repositorio.save(any())).thenReturn(produto)

        val response = controller.patchQuantidade(codigo, quantidade)

        assertEquals(200, response.statusCodeValue)
        assertEquals(15, response.body?.quantidadeProdutoCaracteristica)
    }

    @Test
    @DisplayName("patchQuantidade quando o código não existe deve retornar status 404")
    fun patchQuantidadeWithNonExistentCodigoReturnsNotFound() {
        val codigo = 99
        val quantidade = 15

        `when`(repositorio.findById(codigo)).thenReturn(Optional.empty())

        val response = controller.patchQuantidade(codigo, quantidade)

        assertEquals(404, response.statusCodeValue)
    }
}
package laize_tech.back.ControllerJpa


import laize_tech.back.dto.PlataformaProdutoDTO
import laize_tech.back.entity.*
import laize_tech.back.repository.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import java.util.*

class PlataformaProdutoJpaControllerTest {

    val repositorio: PlataformaProdutoRepository = mock(PlataformaProdutoRepository::class.java)
    val plataformaRepository: PlataformaRepository = mock(PlataformaRepository::class.java)
    val produtoRepository: ProdutoRepository = mock(ProdutoRepository::class.java)
    val caracteristicaRepository: CaracteristicaRepository = mock(CaracteristicaRepository::class.java)
    val tipoCaracteristicaRepository: TipoCaracteristicaRepository = mock(TipoCaracteristicaRepository::class.java)
    val produtoCaracteristicaRepository: ProdutoCaracteristicaRepository = mock(ProdutoCaracteristicaRepository::class.java)

    val controller = PlataformaProdutoJpaController(
        repositorio,
        plataformaRepository,
        produtoRepository,
        caracteristicaRepository,
        tipoCaracteristicaRepository,
        produtoCaracteristicaRepository
    )

    val plataforma = mock(Plataforma::class.java)
    val produto = mock(Produto::class.java)
    val caracteristica = mock(Caracteristica::class.java)
    val tipoCaracteristica = mock(TipoCaracteristica::class.java)
    val produtoCaracteristica = mock(ProdutoCaracteristica::class.java)

    @BeforeEach
    fun setup() {
        val relacoes = listOf(PlataformaProduto(idPlataformaProduto = 1, quantidadeProdutoPlataforma = 10))
        `when`(repositorio.findAll()).thenReturn(relacoes)
        `when`(repositorio.existsById(1)).thenReturn(true)
        `when`(repositorio.existsById(99)).thenReturn(false)
    }

    @Test
    @DisplayName("GET - quando não há dados deve retornar 204")
    fun getWhenEmptyReturns204() {
        `when`(repositorio.findAll()).thenReturn(emptyList())
        val response = controller.getAll()
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
    }

    @Test
    @DisplayName("GET - quando há dados deve retornar 200")
    fun getWhenHasDataReturns200() {
        val response = controller.getAll()
        assertEquals(HttpStatus.OK, response.statusCode)
        assertFalse(response.body!!.isEmpty())
    }

    @Test
    @DisplayName("POST - com dados válidos deve retornar 201")
    fun postWithValidDataReturns201() {
        val dto = PlataformaProdutoDTO(1, 1, 1, 1, 1, 20)

        `when`(plataformaRepository.findById(1)).thenReturn(Optional.of(plataforma))
        `when`(produtoRepository.findById(1)).thenReturn(Optional.of(produto))
        `when`(caracteristicaRepository.findById(1)).thenReturn(Optional.of(caracteristica))
        `when`(tipoCaracteristicaRepository.findById(1)).thenReturn(Optional.of(tipoCaracteristica))
        `when`(produtoCaracteristicaRepository.findById(1)).thenReturn(Optional.of(produtoCaracteristica))

        val entidade = PlataformaProduto(1, plataforma, produtoCaracteristica, caracteristica, tipoCaracteristica, produto, 20)
        `when`(repositorio.save(any())).thenReturn(entidade)

        val response = controller.create(dto)

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(20, response.body?.quantidadeProdutoPlataforma)
    }

    @Test
    @DisplayName("POST - com ID inválido deve retornar 400")
    fun postWithInvalidIdReturns400() {
        val dto = PlataformaProdutoDTO(99, 1, 1, 1, 1, 20)
        `when`(plataformaRepository.findById(99)).thenReturn(Optional.empty())

        val response = controller.create(dto)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    @Test
    @DisplayName("PUT - com dados válidos deve retornar 200")
    fun putWithValidDataReturns200() {
        val dto = PlataformaProdutoDTO(1, 1, 1, 1, 1, 30)
        val existente = PlataformaProduto(1, plataforma, produtoCaracteristica, caracteristica, tipoCaracteristica, produto, 10)

        `when`(repositorio.findById(1)).thenReturn(Optional.of(existente))
        `when`(plataformaRepository.findById(1)).thenReturn(Optional.of(plataforma))
        `when`(produtoRepository.findById(1)).thenReturn(Optional.of(produto))
        `when`(caracteristicaRepository.findById(1)).thenReturn(Optional.of(caracteristica))
        `when`(tipoCaracteristicaRepository.findById(1)).thenReturn(Optional.of(tipoCaracteristica))
        `when`(produtoCaracteristicaRepository.findById(1)).thenReturn(Optional.of(produtoCaracteristica))
        `when`(repositorio.save(any())).thenReturn(existente)

        val response = controller.update(1, dto)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(30, response.body?.quantidadeProdutoPlataforma)
    }

    @Test
    @DisplayName("PUT - quando ID não existe deve retornar 404")
    fun putWithNonExistentIdReturns404() {
        val dto = PlataformaProdutoDTO(1, 1, 1, 1, 1, 10)
        `when`(repositorio.findById(99)).thenReturn(Optional.empty())

        val response = controller.update(99, dto)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    @DisplayName("DELETE - com ID existente deve retornar 204")
    fun deleteWithExistingIdReturns204() {
        val response = controller.delete(1)
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        verify(repositorio).deleteById(1)
    }

    @Test
    @DisplayName("DELETE - com ID inexistente deve retornar 404")
    fun deleteWithNonExistentIdReturns404() {
        val response = controller.delete(99)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        verify(repositorio, never()).deleteById(99)
    }

    @Test
    @DisplayName("PATCH - Atualizando quantidade com ID válido deve retornar 200")
    fun patchWithValidIdReturns200() {
        val existente = PlataformaProduto(idPlataformaProduto = 1, quantidadeProdutoPlataforma = 10)

        `when`(repositorio.findById(1)).thenReturn(Optional.of(existente))
        `when`(repositorio.save(any())).thenReturn(existente.apply { quantidadeProdutoPlataforma = 20 })

        val response = controller.updateQuantidade(1, 20)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(20, response.body?.quantidadeProdutoPlataforma)
    }

    @Test
    @DisplayName("PATCH - Atualizando quantidade com ID inexistente deve retornar 404")
    fun patchWithNonExistentIdReturns404() {
        `when`(repositorio.findById(99)).thenReturn(Optional.empty())
        val response = controller.updateQuantidade(99, 20)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }
}

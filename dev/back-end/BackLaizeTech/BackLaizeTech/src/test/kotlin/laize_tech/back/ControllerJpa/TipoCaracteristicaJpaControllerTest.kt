import laize_tech.back.ControllerJpa.TipoCaracteristicaJpaController
import laize_tech.back.entity.TipoCaracteristica
import laize_tech.back.repository.TipoCaracteristicaRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import java.util.*

class TipoCaracteristicaJpaControllerTest {

    private val tipoCaracteristicaRepository = mock(TipoCaracteristicaRepository::class.java)
    private val controller = TipoCaracteristicaJpaController(tipoCaracteristicaRepository)

    @Test
    fun `cria tipo de caracteristica e retorna criado`() {
        val nova = TipoCaracteristica(null, "Nova")
        val salva = TipoCaracteristica(1, "Nova")
        `when`(tipoCaracteristicaRepository.save(nova)).thenReturn(salva)

        val response = controller.create(nova)

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(salva, response.body)
    }

    @Test
    fun `atualiza tipo de caracteristica quando id existe`() {
        val id = 1L
        val atualizada = TipoCaracteristica(null, "Atualizada")
        val existente = TipoCaracteristica(1, "Antiga")
        `when`(tipoCaracteristicaRepository.findById(id)).thenReturn(Optional.of(existente))
        `when`(tipoCaracteristicaRepository.save(existente)).thenReturn(existente.apply {
            nomeTipoCaracteristica = atualizada.nomeTipoCaracteristica
        })

        val response = controller.update(id, atualizada)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("Atualizada", response.body?.nomeTipoCaracteristica)
    }

    @Test
    fun `retorna not found ao atualizar tipo de caracteristica com id inexistente`() {
        val id = 2L
        val atualizada = TipoCaracteristica(null, "Atualizada")
        `when`(tipoCaracteristicaRepository.findById(id)).thenReturn(Optional.empty())

        val response = controller.update(id, atualizada)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun `retorna lista de tipos de caracteristicas quando existem registros`() {
        val lista = listOf(
            TipoCaracteristica(1, "A"),
            TipoCaracteristica(2, "B")
        )
        `when`(tipoCaracteristicaRepository.findAll()).thenReturn(lista)

        val response = controller.getAll()

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(lista, response.body)
    }

    @Test
    fun `retorna no content quando nao existem tipos de caracteristicas`() {
        `when`(tipoCaracteristicaRepository.findAll()).thenReturn(emptyList())

        val response = controller.getAll()

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        assertNull(response.body)
    }

    @Test
    fun `remove tipo de caracteristica quando id existe`() {
        val id = 1L
        `when`(tipoCaracteristicaRepository.existsById(id)).thenReturn(true)
        doNothing().`when`(tipoCaracteristicaRepository).deleteById(id)

        val response = controller.delete(id)

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
    }

    @Test
    fun `retorna not found ao remover tipo de caracteristica com id inexistente`() {
        val id = 99L
        `when`(tipoCaracteristicaRepository.existsById(id)).thenReturn(false)

        val response = controller.delete(id)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun `atualiza tipo de caracteristica com nome vazio e retorna ok`() {
        val id = 3L
        val atualizada = TipoCaracteristica(null, "")
        val existente = TipoCaracteristica(3, "NomeAntigo")
        `when`(tipoCaracteristicaRepository.findById(id)).thenReturn(Optional.of(existente))
        `when`(tipoCaracteristicaRepository.save(existente)).thenReturn(existente.apply {
            nomeTipoCaracteristica = atualizada.nomeTipoCaracteristica
        })

        val response = controller.update(id, atualizada)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("", response.body?.nomeTipoCaracteristica)
    }

    @Test
    fun `cria tipo de caracteristica com nome nulo`() {
        val nova = TipoCaracteristica(null, null)
        val salva = TipoCaracteristica(2, null)
        `when`(tipoCaracteristicaRepository.save(nova)).thenReturn(salva)

        val response = controller.create(nova)

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(salva, response.body)
    }
}
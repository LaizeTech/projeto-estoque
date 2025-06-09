import laize_tech.back.ControllerJpa.CaracteristicaJpaController
import laize_tech.back.dto.CaracteristicaDTO
import laize_tech.back.entity.Caracteristica
import laize_tech.back.entity.TipoCaracteristica
import laize_tech.back.exceptions.IdNaoEncontradoException
import laize_tech.back.repository.CaracteristicaRepository
import laize_tech.back.repository.TipoCaracteristicaRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import java.util.*

class CaracteristicaJpaControllerTest {

    private val caracteristicaRepository = mock(CaracteristicaRepository::class.java)
    private val tipoCaracteristicaRepository = mock(TipoCaracteristicaRepository::class.java)
    private val controller = CaracteristicaJpaController(caracteristicaRepository, tipoCaracteristicaRepository)

    private fun criarTipoCaracteristicaMock() = TipoCaracteristica(1, "Tipo Test")
    private fun criarCaracteristicaMock() = Caracteristica(1, "Caracteristica Test", criarTipoCaracteristicaMock())

    @Test
    fun `retorna lista de caracteristicas quando existem registros`() {
        val caracteristicas = listOf(criarCaracteristicaMock())
        `when`(caracteristicaRepository.findAll()).thenReturn(caracteristicas)

        val response = controller.getAll()

        assertEquals(HttpStatus.OK.value(), response.statusCode.value())
        assertEquals(caracteristicas, response.body)
    }

    @Test
    fun `retorna no content quando nao existem caracteristicas`() {
        `when`(caracteristicaRepository.findAll()).thenReturn(emptyList())

        val response = controller.getAll()

        assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode.value())
        assertNull(response.body)
    }

    @Test
    fun `cria nova caracteristica com sucesso`() {
        val tipoCaracteristica = criarTipoCaracteristicaMock()
        val dto = CaracteristicaDTO(
            idTipoCaracteristica = 1,
            nomeCaracteristica = "Nova Caracteristica"
        )
        val caracteristicaSalva = Caracteristica(
            id = 1,
            nomeCaracteristica = dto.nomeCaracteristica,
            tipoCaracteristica = tipoCaracteristica
        )

        `when`(tipoCaracteristicaRepository.findById(1)).thenReturn(Optional.of(tipoCaracteristica))
        `when`(caracteristicaRepository.save(any())).thenReturn(caracteristicaSalva)

        val response = controller.post(dto)

        assertEquals(HttpStatus.CREATED.value(), response.statusCode.value())
        assertEquals(caracteristicaSalva, response.body)
    }

    @Test
    fun `retorna erro ao criar caracteristica com tipo inexistente`() {
        val dto = CaracteristicaDTO(
            idTipoCaracteristica = 99,
            nomeCaracteristica = "Nova Caracteristica"
        )

        `when`(tipoCaracteristicaRepository.findById(99)).thenReturn(Optional.empty())

        assertThrows(IdNaoEncontradoException::class.java) {
            controller.post(dto)
        }
    }

    @Test
    fun `retorna not found ao atualizar caracteristica inexistente`() {
        val id = 99
        val caracteristicaAtualizada = criarCaracteristicaMock()

        `when`(caracteristicaRepository.findById(id)).thenReturn(Optional.empty())

        val response = controller.update(id, caracteristicaAtualizada)

        assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode.value())
        assertNull(response.body)
    }

    @Test
    fun `remove caracteristica existente com sucesso`() {
        val id = 1
        `when`(caracteristicaRepository.existsById(id)).thenReturn(true)
        doNothing().`when`(caracteristicaRepository).deleteById(id)

        val response = controller.delete(id)

        assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode.value())
        assertNull(response.body)
    }

    @Test
    fun `retorna not found ao remover caracteristica inexistente`() {
        val id = 99
        `when`(caracteristicaRepository.existsById(id)).thenReturn(false)

        val response = controller.delete(id)

        assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode.value())
        assertNull(response.body)
    }
}
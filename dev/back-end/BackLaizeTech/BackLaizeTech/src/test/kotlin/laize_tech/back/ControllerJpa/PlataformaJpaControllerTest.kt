import laize_tech.back.ControllerJpa.PlataformaJpaController
import laize_tech.back.dto.PlataformaDTO
import laize_tech.back.entity.Empresa
import laize_tech.back.entity.Plataforma
import laize_tech.back.exceptions.IdNaoEncontradoException
import laize_tech.back.repository.EmpresaRepository
import laize_tech.back.repository.PlataformaRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import java.util.*

class PlataformaJpaControllerTest {

    private val plataformaRepository = mock(PlataformaRepository::class.java)
    private val empresaRepository = mock(EmpresaRepository::class.java)
    private val controller = PlataformaJpaController(plataformaRepository, empresaRepository)

    @Test
    fun `retorna lista de plataformas quando existem registros`() {
        val plataformas = listOf(
            Plataforma(1, "Plataforma 1", true, Empresa(1, "Empresa 1", "12345678901234")),
            Plataforma(2, "Plataforma 2", true, Empresa(2, "Empresa 2", "12345678901235"))
        )
        `when`(plataformaRepository.findAll()).thenReturn(plataformas)

        val response = controller.get()

        assertEquals(HttpStatus.OK.value(), response.statusCode.value())
        assertEquals(plataformas, response.body)
    }

    @Test
    fun `retorna no content quando nao existem plataformas`() {
        `when`(plataformaRepository.findAll()).thenReturn(emptyList())

        val response = controller.get()

        assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode.value())
        assertNull(response.body)
    }

    @Test
    fun `cria nova plataforma com sucesso`() {
        val empresa = Empresa(1, "Empresa Test", "12345678901234")
        val dto = PlataformaDTO(
            nomePlataforma = "Nova Plataforma",
            status = true,
            idEmpresa = 1
        )
        val plataforma = Plataforma(
            idPlataforma = 1,
            nomePlataforma = "Nova Plataforma",
            status = true,
            empresa = empresa
        )

        `when`(empresaRepository.findById(1)).thenReturn(Optional.of(empresa))
        `when`(plataformaRepository.save(any())).thenReturn(plataforma)

        val response = controller.post(dto)

        assertEquals(HttpStatus.CREATED.value(), response.statusCode.value())
        assertEquals(plataforma, response.body)
    }

    @Test
    fun `retorna not found ao criar plataforma com empresa inexistente`() {
        val dto = PlataformaDTO(
            nomePlataforma = "Nova Plataforma",
            status = true,
            idEmpresa = 99
        )

        `when`(empresaRepository.findById(99)).thenReturn(Optional.empty())

        val exception = assertThrows(IdNaoEncontradoException::class.java) {
            controller.post(dto)
        }

        assertEquals("Não foi possível encontrar Empresa com o ID 99", exception.message)
    }

    @Test
    fun `atualiza plataforma existente com sucesso`() {
        val id = 1
        val empresa = Empresa(1, "Empresa Test", "12345678901234")
        val dto = PlataformaDTO(
            nomePlataforma = "Plataforma Atualizada",
            status = false,
            idEmpresa = 1
        )
        val plataformaExistente = Plataforma(1, "Plataforma Original", true, empresa)
        val plataformaAtualizada = Plataforma(1, "Plataforma Atualizada", false, empresa)

        `when`(plataformaRepository.findById(id)).thenReturn(Optional.of(plataformaExistente))
        `when`(empresaRepository.findById(1)).thenReturn(Optional.of(empresa))
        `when`(plataformaRepository.save(any())).thenReturn(plataformaAtualizada)

        val response = controller.put(id, dto)

        assertEquals(HttpStatus.OK.value(), response.statusCode.value())
        assertEquals(plataformaAtualizada, response.body)
    }

    @Test
    fun `retorna bad request ao atualizar plataforma com nome em branco`() {
        val id = 1
        val dto = PlataformaDTO(
            nomePlataforma = "",
            status = true,
            idEmpresa = 1
        )
        val plataformaExistente = Plataforma(1, "Plataforma Original", true, Empresa())

        `when`(plataformaRepository.findById(id)).thenReturn(Optional.of(plataformaExistente))

        val response = controller.put(id, dto)

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode.value())
        assertEquals("Nome da plataforma não pode estar em branco", response.body)
    }

    @Test
    fun `retorna not found ao atualizar plataforma inexistente`() {
        val id = 99
        val dto = PlataformaDTO(
            nomePlataforma = "Plataforma Test",
            status = true,
            idEmpresa = 1
        )

        `when`(plataformaRepository.findById(id)).thenReturn(Optional.empty())

        val exception = assertThrows(IdNaoEncontradoException::class.java) {
            controller.put(id, dto)
        }

        assertEquals("Não foi possível encontrar Plataforma com o ID 99", exception.message)
    }

    @Test
    fun `remove plataforma existente com sucesso`() {
        val id = 1
        `when`(plataformaRepository.existsById(id)).thenReturn(true)
        doNothing().`when`(plataformaRepository).deleteById(id)

        val response = controller.delete(id)

        assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode.value())
    }

    @Test
    fun `retorna not found ao remover plataforma inexistente`() {
        val id = 99
        `when`(plataformaRepository.existsById(id)).thenReturn(false)

        val response = controller.delete(id)

        assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode.value())
    }
}
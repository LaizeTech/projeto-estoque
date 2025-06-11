import laize_tech.back.ControllerJpa.SaidaJpaController
import laize_tech.back.dto.SaidaDTO
import laize_tech.back.entity.*
import laize_tech.back.exceptions.IdNaoEncontradoException
import laize_tech.back.repository.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import java.time.LocalDate
import java.util.*

class SaidaJpaControllerTest {

    private val saidaRepository = mock(SaidaRepository::class.java)
    private val tipoSaidaRepository = mock(TipoSaidaRepository::class.java)
    private val empresaRepository = mock(EmpresaRepository::class.java)
    private val plataformaRepository = mock(PlataformaRepository::class.java)
    private val statusVendaRepository = mock(StatusVendaRepository::class.java)
    private val controller = SaidaJpaController(
        saidaRepository,
        tipoSaidaRepository,
        empresaRepository,
        plataformaRepository,
        statusVendaRepository
    )

    @Test
    fun `retorna lista de saidas quando existem registros`() {
        val lista = listOf(Saida(), Saida())
        `when`(saidaRepository.findAll()).thenReturn(lista)

        val response = controller.get()

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(lista, response.body)
    }

    @Test
    fun `retorna no content quando nao existem saidas`() {
        `when`(saidaRepository.findAll()).thenReturn(emptyList())

        val response = controller.get()

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        assertNull(response.body)
    }

    @Test
    fun `cria nova saida com sucesso`() {
        val dto = SaidaDTO(
            idTipoSaida = 1,
            idEmpresa = 1,
            idPlataforma = 1,
            dtVenda = "2023-08-20",
            precoVenda = 14.99,
            totalTaxa = 100.0,
            totalDesconto = 10.0,
            idStatusVenda = 1
        )
        val tipoSaida = TipoSaida(1, "Venda")
        val empresa = Empresa(1, "Empresa A", "12345678901234")
        val plataforma = Plataforma(1, "Plataforma A", true, empresa)
        val statusVenda = StatusVenda(1, "Aprovado")
        val saida = Saida(null, tipoSaida, empresa, plataforma, LocalDate.parse("2023-08-20"), 14.99, 100.0, 10.0, statusVenda)

        `when`(tipoSaidaRepository.findById(1)).thenReturn(Optional.of(tipoSaida))
        `when`(empresaRepository.findById(1)).thenReturn(Optional.of(empresa))
        `when`(plataformaRepository.findById(1)).thenReturn(Optional.of(plataforma))
        `when`(statusVendaRepository.findById(1)).thenReturn(Optional.of(statusVenda))
        `when`(saidaRepository.save(any(Saida::class.java))).thenReturn(saida)

        val response = controller.post(dto)

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(saida, response.body)
    }

    @Test
    fun `retorna not found ao criar saida com tipoSaida inexistente`() {
        val dto = SaidaDTO(
            idTipoSaida = 99,
            idEmpresa = 1,
            idPlataforma = 1,
            dtVenda = "2023-11-13",
            precoVenda = 32.99,
            totalTaxa = 100.0,
            totalDesconto = 10.0,
            idStatusVenda = 1
        )
        `when`(tipoSaidaRepository.findById(99)).thenReturn(Optional.empty())

        val exception = assertThrows(IdNaoEncontradoException::class.java) {
            controller.post(dto)
        }

        assertEquals("Não foi possível encontrar TipoSaida com o ID 99", exception.message)
    }

    @Test
    fun `retorna not found ao criar saida com empresa inexistente`() {
        val dto = SaidaDTO(
            idTipoSaida = 1,
            idEmpresa = 99,
            idPlataforma = 1,
            dtVenda = "2023-06-02",
            precoVenda = 180.45,
            totalTaxa = 100.0,
            totalDesconto = 10.0,
            idStatusVenda = 1
        )
        val tipoSaida = TipoSaida(1, "Venda")
        `when`(tipoSaidaRepository.findById(1)).thenReturn(Optional.of(tipoSaida))
        `when`(empresaRepository.findById(99)).thenReturn(Optional.empty())

        val exception = assertThrows(IdNaoEncontradoException::class.java) {
            controller.post(dto)
        }

        assertEquals("Não foi possível encontrar Empresa com o ID 99", exception.message)
    }

    @Test
    fun `retorna not found ao criar saida com plataforma inexistente`() {
        val dto = SaidaDTO(
            idTipoSaida = 1,
            idEmpresa = 1,
            idPlataforma = 99,
            dtVenda = "2023-09-02",
            precoVenda = 23.4,
            totalTaxa = 100.0,
            totalDesconto = 10.0,
            idStatusVenda = 1
        )
        val tipoSaida = TipoSaida(1, "Venda")
        val empresa = Empresa(1, "Empresa A", "12345678901234")
        `when`(tipoSaidaRepository.findById(1)).thenReturn(Optional.of(tipoSaida))
        `when`(empresaRepository.findById(1)).thenReturn(Optional.of(empresa))
        `when`(plataformaRepository.findById(99)).thenReturn(Optional.empty())

        val exception = assertThrows(IdNaoEncontradoException::class.java) {
            controller.post(dto)
        }

        assertEquals("Não foi possível encontrar Plataforma com o ID 99", exception.message)
    }

    @Test
    fun `retorna not found ao criar saida com statusVenda inexistente`() {
        val dto = SaidaDTO(
            idTipoSaida = 1,
            idEmpresa = 1,
            idPlataforma = 1,
            dtVenda = "2023-10-01",
            precoVenda = 17.99,
            totalTaxa = 100.0,
            totalDesconto = 10.0,
            idStatusVenda = 99
        )
        val tipoSaida = TipoSaida(1, "Venda")
        val empresa = Empresa(1, "Empresa A", "12345678901234")
        val plataforma = Plataforma(1, "Plataforma A", true, empresa)
        `when`(tipoSaidaRepository.findById(1)).thenReturn(Optional.of(tipoSaida))
        `when`(empresaRepository.findById(1)).thenReturn(Optional.of(empresa))
        `when`(plataformaRepository.findById(1)).thenReturn(Optional.of(plataforma))
        `when`(statusVendaRepository.findById(99)).thenReturn(Optional.empty())

        val exception = assertThrows(IdNaoEncontradoException::class.java) {
            controller.post(dto)
        }

        assertEquals("Não foi possível encontrar StatusVenda com o ID 99", exception.message)
    }

    @Test
    fun `atualiza saida existente com sucesso`() {
        val id = 1
        val dto = SaidaDTO(
            idTipoSaida = 1,
            idEmpresa = 1,
            idPlataforma = 1,
            dtVenda = "2023-09-02",
            precoVenda = 80.5,
            totalTaxa = 150.0,
            totalDesconto = 15.0,
            idStatusVenda = 1
        )
        val tipoSaida = TipoSaida(1, "Venda")
        val empresa = Empresa(1, "Empresa A", "12345678901234")
        val plataforma = Plataforma(1, "Plataforma A", true, empresa)
        val statusVenda = StatusVenda(1, "Aprovado")
        val saidaExistente = Saida(id, tipoSaida, empresa, plataforma, LocalDate.parse("2023-09-01"), 80.5, 150.0, 15.0, statusVenda)

        `when`(saidaRepository.findById(id)).thenReturn(Optional.of(saidaExistente))
        `when`(tipoSaidaRepository.findById(1)).thenReturn(Optional.of(tipoSaida))
        `when`(empresaRepository.findById(1)).thenReturn(Optional.of(empresa))
        `when`(plataformaRepository.findById(1)).thenReturn(Optional.of(plataforma))
        `when`(statusVendaRepository.findById(1)).thenReturn(Optional.of(statusVenda))
        `when`(saidaRepository.save(any(Saida::class.java))).thenReturn(saidaExistente)

        val response = controller.put(id, dto)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(80.5, (response.body as Saida).precoVenda)
    }

    @Test
    fun `retorna not found ao atualizar saida inexistente`() {
        val id = 99
        val dto = SaidaDTO(
            idTipoSaida = 1,
            idEmpresa = 1,
            idPlataforma = 1,
            dtVenda = "2023-10-01",
            precoVenda = 25.5,
            totalTaxa = 150.0,
            totalDesconto = 15.0,
            idStatusVenda = 1
        )
        `when`(saidaRepository.findById(id)).thenReturn(Optional.empty())

        val exception = assertThrows(IdNaoEncontradoException::class.java) {
            controller.put(id, dto)
        }

        assertEquals("Não foi possível encontrar Saída com o ID 99", exception.message)
    }

    @Test
    fun `remove saida existente com sucesso`() {
        val id = 1
        `when`(saidaRepository.existsById(id)).thenReturn(true)
        doNothing().`when`(saidaRepository).deleteById(id)

        val response = controller.delete(id)

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
    }

    @Test
    fun `retorna not found ao remover saida inexistente`() {
        val id = 99
        `when`(saidaRepository.existsById(id)).thenReturn(false)

        val response = controller.delete(id)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }
}
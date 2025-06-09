import laize_tech.back.ControllerJpa.EmpresaJpaController
import laize_tech.back.dto.EmpresaDTO
import laize_tech.back.entity.Empresa
import laize_tech.back.repository.EmpresaRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class EmpresaJpaControllerTest {

    private lateinit var empresaJpaController: EmpresaJpaController
    private lateinit var empresaRepository: EmpresaRepository

    @BeforeEach
    fun setUp() {
        empresaRepository = mock(EmpresaRepository::class.java)
        empresaJpaController = EmpresaJpaController(empresaRepository)
    }

    @Test
    fun `create deve retornar 400 quando nome for nulo ou vazio`() {
        val empresaDTO = EmpresaDTO(null, "12345678901234")
        val response = empresaJpaController.create(empresaDTO)
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals("Os campos nome e CNPJ não podem estar vazios ou nulos!", response.body)
    }

    @Test
    fun `create deve retornar 400 quando cnpj for nulo ou vazio`() {
        val empresaDTO = EmpresaDTO("Empresa X", "")
        val response = empresaJpaController.create(empresaDTO)
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals("Os campos nome e CNPJ não podem estar vazios ou nulos!", response.body)
    }

    @Test
    fun `create deve retornar 409 quando cnpj ja estiver cadastrado`() {
        val empresaDTO = EmpresaDTO("Empresa X", "12345678901234")
        `when`(empresaRepository.existsByCnpj("12345678901234")).thenReturn(true)

        val response = empresaJpaController.create(empresaDTO)

        assertEquals(HttpStatus.CONFLICT, response.statusCode)
        assertEquals("Já existe uma empresa cadastrada com esse CNPJ!", response.body)
    }

    @Test
    fun `create deve retornar 201 quando empresa for criada com sucesso`() {
        val empresaDTO = EmpresaDTO("Empresa X", "12345678901234")
        `when`(empresaRepository.existsByCnpj("12345678901234")).thenReturn(false)

        val empresaSalva = Empresa(1, "Empresa X", "12345678901234")
        `when`(empresaRepository.save(any(Empresa::class.java))).thenReturn(empresaSalva)

        val response = empresaJpaController.create(empresaDTO)

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertNotNull(response.body)
        val dto = response.body as EmpresaDTO
        assertEquals(empresaDTO.nomeEmpresa, dto.nomeEmpresa)
        assertEquals(empresaDTO.cnpj, dto.cnpj)
    }

    @Test
    fun `create deve retornar 500 quando ocorre erro ao salvar empresa`() {
        val empresaDTO = EmpresaDTO("Empresa X", "12345678901234")
        `when`(empresaRepository.existsByCnpj("12345678901234")).thenReturn(false)
        `when`(empresaRepository.save(any(Empresa::class.java))).thenThrow(RuntimeException("Erro ao salvar"))

        val response = empresaJpaController.create(empresaDTO)

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode)
        assertEquals("Erro ao salvar empresa", response.body)
    }
}
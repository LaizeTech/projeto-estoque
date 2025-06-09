import laize_tech.back.ControllerJpa.EmpresaJpaController
import laize_tech.back.entity.Empresa
import laize_tech.back.repository.EmpresaRepository
import org.junit.jupiter.api.Assertions.*
<<<<<<< HEAD
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus

class EmpresaJpaControllerTest {

    private val repositorio = mock(EmpresaRepository::class.java)
    private val controller = EmpresaJpaController(repositorio)

    @Test
    fun `retorna lista de empresas quando existem registros`() {
        val empresas = listOf(
            Empresa(1, "Empresa 1", "12345678901234"),
            Empresa(2, "Empresa 2", "56789012345678")
        )
        `when`(repositorio.findAll()).thenReturn(empresas)

        val response = controller.get()

        assertEquals(HttpStatus.OK.value(), response.statusCode.value())
        assertEquals(empresas, response.body)
    }

    @Test
    fun `retorna no content quando nao existem empresas`() {
        `when`(repositorio.findAll()).thenReturn(emptyList())

        val response = controller.get()

        assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode.value())
        assertNull(response.body)
    }

    @Test
    fun `retorna empresa quando cnpj existe`() {
        val cnpj = "12345678901234"
        val empresa = Empresa(1, "Empresa Test", cnpj)
        `when`(repositorio.findByCnpj(cnpj)).thenReturn(empresa)

        val response = controller.getByCnpj(cnpj)

        assertEquals(HttpStatus.OK.value(), response.statusCode.value())
        assertEquals(empresa, response.body)
    }

    @Test
    fun `retorna not found quando cnpj nao existe`() {
        val cnpj = "99999999999999"
        `when`(repositorio.findByCnpj(cnpj)).thenThrow(RuntimeException())

        val response = controller.getByCnpj(cnpj)

        assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode.value())
        assertNull(response.body)
    }

    @Test
    fun `retorna lista de empresas quando nome existe`() {
        val nome = "empresa"
        val empresas = listOf(
            Empresa(1, "Empresa 1", "12345678901234"),
            Empresa(2, "Empresa 2", "56789012345678")
        )
        `when`(repositorio.findByNomeEmpresaContainingIgnoreCase(nome)).thenReturn(empresas)

        val response = controller.getByNome(nome)

        assertEquals(HttpStatus.OK.value(), response.statusCode.value())
        assertEquals(empresas, response.body)
    }

    @Test
    fun `retorna no content quando nome nao existe`() {
        val nome = "inexistente"
        `when`(repositorio.findByNomeEmpresaContainingIgnoreCase(nome)).thenReturn(emptyList())

        val response = controller.getByNome(nome)

        assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode.value())
        assertNull(response.body)
=======
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import java.util.*

class EmpresaJpaControllerTest {

    private lateinit var empresaJpaController: EmpresaJpaController
    private lateinit var empresaRepository: EmpresaRepository

    @BeforeEach
    fun setUp() {
        empresaRepository = mock(EmpresaRepository::class.java)
        empresaJpaController = EmpresaJpaController(empresaRepository)
    }

    @Test
    fun `post deve retornar 400 quando nome for nulo ou vazio`() {
        val empresaDTO = EmpresaDTO(null, "12345678901234")
        val response = empresaJpaController.post(empresaDTO)
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals("Os campos nome e CNPJ não podem estar vazios ou nulos!", response.body)
    }

    @Test
    fun `post deve retornar 400 quando cnpj for nulo ou vazio`() {
        val empresaDTO = EmpresaDTO("Empresa X", "")
        val response = empresaJpaController.post(empresaDTO)
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals("Os campos nome e CNPJ não podem estar vazios ou nulos!", response.body)
    }

    @Test
    fun `post deve retornar 409 quando cnpj ja estiver cadastrado`() {
        val empresaDTO = EmpresaDTO("Empresa X", "12345678901234")
        `when`(empresaRepository.findAll()).thenReturn(listOf(Empresa(1, "Outra Empresa", "12345678901234")))

        val response = empresaJpaController.post(empresaDTO)

        assertEquals(HttpStatus.CONFLICT, response.statusCode)
        assertEquals("Já existe uma empresa cadastrada com esse CNPJ!", response.body)
    }

    @Test
    fun `post deve retornar 201 quando empresa for criada com sucesso`() {
        val empresaDTO = EmpresaDTO("Empresa X", "12345678901234")
        `when`(empresaRepository.findAll()).thenReturn(emptyList())
        val empresaSalva = Empresa(1, "Empresa X", "12345678901234")
        `when`(empresaRepository.save(any(Empresa::class.java))).thenReturn(empresaSalva)

        val response = empresaJpaController.post(empresaDTO)

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertNotNull(response.body)
        assertEquals(empresaSalva, response.body)
    }

    @Test
    fun `post deve retornar 500 quando ocorre erro ao salvar empresa`() {
        val empresaDTO = EmpresaDTO("Empresa X", "12345678901234")
        `when`(empresaRepository.findAll()).thenReturn(emptyList())
        `when`(empresaRepository.save(any(Empresa::class.java))).thenThrow(RuntimeException("Erro ao salvar"))

        val response = empresaJpaController.post(empresaDTO)

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode)
        assertEquals("Erro ao salvar empresa", response.body)
>>>>>>> 6c4a9919785d3f1967fd55cf16923d819d02eb61
    }
}
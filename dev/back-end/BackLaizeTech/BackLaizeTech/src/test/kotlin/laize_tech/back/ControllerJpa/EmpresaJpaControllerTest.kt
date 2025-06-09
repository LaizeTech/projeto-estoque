import laize_tech.back.ControllerJpa.EmpresaJpaController
import laize_tech.back.entity.Empresa
import laize_tech.back.repository.EmpresaRepository
import org.junit.jupiter.api.Assertions.*
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
    }
}
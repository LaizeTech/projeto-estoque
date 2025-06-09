package laize_tech.back.ControllerJpa

import laize_tech.back.entity.StatusVenda
import laize_tech.back.repository.StatusVendaRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus

class StatusVendaJpaControllerTest {

    private val repositorio = mock(StatusVendaRepository::class.java)
    private val controller = StatusVendaJpaController(repositorio)

    @Test
    fun `retorna lista de status quando existem registros`() {
        val lista = listOf(StatusVenda(1, "Aprovado"), StatusVenda(2, "Pendente"))
        `when`(repositorio.findAll()).thenReturn(lista)

        val response = controller.get()

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(lista, response.body)
    }

    @Test
    fun `retorna no content quando nao existem registros`() {
        `when`(repositorio.findAll()).thenReturn(emptyList())

        val response = controller.get()

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        assertNull(response.body)
    }

    @Test
    fun `cria status de venda e retorna criado`() {
        val novo = StatusVenda(null, "Novo")
        val salvo = StatusVenda(1, "Novo")
        `when`(repositorio.save(novo)).thenReturn(salvo)

        val response = controller.post(novo)

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(salvo, response.body)
    }

    @Test
    fun `atualiza status de venda quando id existe`() {
        val id = 1
        val atualizado = StatusVenda(null, "Atualizado")
        val salvo = StatusVenda(id, "Atualizado")
        `when`(repositorio.existsById(id)).thenReturn(true)
        `when`(repositorio.save(any(StatusVenda::class.java))).thenReturn(salvo)

        val response = controller.put(id, atualizado)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(salvo, response.body)
    }

    @Test
    fun `retorna not found ao atualizar status de venda com id inexistente`() {
        val id = 99
        val atualizado = StatusVenda(null, "Atualizado")
        `when`(repositorio.existsById(id)).thenReturn(false)

        val response = controller.put(id, atualizado)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun `remove status de venda quando id existe`() {
        val id = 1
        `when`(repositorio.existsById(id)).thenReturn(true)
        doNothing().`when`(repositorio).deleteById(id)

        val response = controller.delete(id)

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
    }

    @Test
    fun `retorna not found ao remover status de venda com id inexistente`() {
        val id = 99
        `when`(repositorio.existsById(id)).thenReturn(false)

        val response = controller.delete(id)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }
}
package laize_tech.back.ControllerJpa

import laize_tech.back.entity.TipoSaida
import laize_tech.back.repository.TipoSaidaRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.ResponseEntity

class TipoSaidaJpaControllerTest {

    private val repositorio = mock(TipoSaidaRepository::class.java)
    private val controller = TipoSaidaJpaController(repositorio)

    @Test
    fun `deve retornar lista vazia quando nao existirem tipos de saida`() {
        `when`(repositorio.findAll()).thenReturn(emptyList())

        val response = controller.get()

        assertEquals(204, response.statusCodeValue)
        assertTrue(response.body == null)
    }

    @Test
    fun `deve retornar lista de tipos de saida quando existirem registros`() {
        val tiposSaida = listOf(TipoSaida(1, "Tipo1"), TipoSaida(2, "Tipo2"))
        `when`(repositorio.findAll()).thenReturn(tiposSaida)

        val response = controller.get()

        assertEquals(200, response.statusCodeValue)
        assertEquals(tiposSaida, response.body)
    }

    @Test
    fun `deve salvar novo tipo de saida e retornar criado`() {
        val novoTipoSaida = TipoSaida(null, "Novo Tipo")
        val tipoSaidaSalvo = TipoSaida(1, "Novo Tipo")
        `when`(repositorio.save(novoTipoSaida)).thenReturn(tipoSaidaSalvo)

        val response = controller.post(novoTipoSaida)

        assertEquals(201, response.statusCodeValue)
        assertEquals(tipoSaidaSalvo, response.body)
    }

    @Test
    fun `deve atualizar tipo de saida quando id existir`() {
        val id = 1
        val tipoSaidaAtualizado = TipoSaida(null, "Atualizado")
        val tipoSaidaSalvo = TipoSaida(id, "Atualizado")
        `when`(repositorio.existsById(id)).thenReturn(true)
        `when`(repositorio.save(tipoSaidaAtualizado)).thenReturn(tipoSaidaSalvo)

        val response = controller.put(id, tipoSaidaAtualizado)

        assertEquals(200, response.statusCodeValue)
        assertEquals(tipoSaidaSalvo, response.body)
    }

    @Test
    fun `deve retornar nao encontrado ao atualizar tipo de saida com id inexistente`() {
        val id = 1
        val tipoSaidaAtualizado = TipoSaida(null, "Atualizado")
        `when`(repositorio.existsById(id)).thenReturn(false)

        val response = controller.put(id, tipoSaidaAtualizado)

        assertEquals(404, response.statusCodeValue)
        assertEquals("", response.body)
    }

    @Test
    fun `deve remover tipo de saida quando id existir`() {
        val id = 1
        `when`(repositorio.existsById(id)).thenReturn(true)
        doNothing().`when`(repositorio).deleteById(id)

        val response = controller.delete(id)

        assertEquals(204, response.statusCodeValue)
        assertTrue(response.body == null)
    }

    @Test
    fun `deve retornar nao encontrado ao remover tipo de saida com id inexistente`() {
        val id = 1
        `when`(repositorio.existsById(id)).thenReturn(false)

        val response = controller.delete(id)

        assertEquals(404, response.statusCodeValue)
        assertEquals("", response.body)
    }
}
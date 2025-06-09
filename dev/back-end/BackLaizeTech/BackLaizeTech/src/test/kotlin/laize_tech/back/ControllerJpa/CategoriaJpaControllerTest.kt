import laize_tech.back.ControllerJpa.CategoriaJpaController
import laize_tech.back.entity.Categoria
import laize_tech.back.repository.CategoriaRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus

class CategoriaJpaControllerTest {

    private val repositorio = mock(CategoriaRepository::class.java)
    private val controller = CategoriaJpaController(repositorio)

    @Test
    fun `retorna lista de categorias quando existem registros`() {
        val categorias = listOf(
            Categoria(1, "Categoria 1"),
            Categoria(2, "Categoria 2")
        )
        `when`(repositorio.findAll()).thenReturn(categorias)

        val response = controller.get()

        assertEquals(HttpStatus.OK.value(), response.statusCode.value())
        assertEquals(categorias, response.body)
    }

    @Test
    fun `retorna no content quando nao existem categorias`() {
        `when`(repositorio.findAll()).thenReturn(emptyList())

        val response = controller.get()

        assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode.value())
        assertNull(response.body)
    }

    @Test
    fun `cria nova categoria com sucesso`() {
        val novaCategoria = Categoria(null, "Nova Categoria")
        val categoriaSalva = Categoria(1, "Nova Categoria")

        `when`(repositorio.findAll()).thenReturn(emptyList())
        `when`(repositorio.save(novaCategoria)).thenReturn(categoriaSalva)

        val response = controller.post(novaCategoria)

        assertEquals(HttpStatus.CREATED.value(), response.statusCode.value())
        assertEquals(categoriaSalva, response.body)
    }

    @Test
    fun `retorna bad request ao criar categoria com nome em branco`() {
        val novaCategoria = Categoria(null, "  ")

        val response = controller.post(novaCategoria)

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode.value())
        assertEquals("", response.body)
    }

    @Test
    fun `retorna conflict ao criar categoria com nome duplicado`() {
        val categoriaExistente = Categoria(1, "Categoria Test")
        val novaCategoria = Categoria(null, "Categoria Test")

        `when`(repositorio.findAll()).thenReturn(listOf(categoriaExistente))

        val response = controller.post(novaCategoria)

        assertEquals(HttpStatus.CONFLICT.value(), response.statusCode.value())
        assertEquals("", response.body)
    }

    @Test
    fun `atualiza categoria existente com sucesso`() {
        val id = 1
        val categoriaAtualizada = Categoria(id, "Categoria Atualizada")

        `when`(repositorio.existsById(id)).thenReturn(true)
        `when`(repositorio.findAll()).thenReturn(emptyList())
        `when`(repositorio.save(categoriaAtualizada)).thenReturn(categoriaAtualizada)

        val response = controller.put(id, categoriaAtualizada)

        assertEquals(HttpStatus.OK.value(), response.statusCode.value())
        assertEquals(categoriaAtualizada, response.body)
    }

    @Test
    fun `retorna not found ao atualizar categoria inexistente`() {
        val id = 99
        val categoriaAtualizada = Categoria(id, "Categoria Test")

        `when`(repositorio.existsById(id)).thenReturn(false)

        val response = controller.put(id, categoriaAtualizada)

        assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode.value())
        assertEquals("", response.body)
    }

    @Test
    fun `retorna bad request ao atualizar categoria com nome em branco`() {
        val id = 1
        val categoriaAtualizada = Categoria(id, "  ")

        `when`(repositorio.existsById(id)).thenReturn(true)

        val response = controller.put(id, categoriaAtualizada)

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode.value())
        assertEquals("", response.body)
    }

    @Test
    fun `retorna conflict ao atualizar categoria com nome duplicado`() {
        val id = 1
        val categoriaExistente = Categoria(2, "Categoria Test")
        val categoriaAtualizada = Categoria(id, "Categoria Test")

        `when`(repositorio.existsById(id)).thenReturn(true)
        `when`(repositorio.findAll()).thenReturn(listOf(categoriaExistente))

        val response = controller.put(id, categoriaAtualizada)

        assertEquals(HttpStatus.CONFLICT.value(), response.statusCode.value())
        assertEquals("", response.body)
    }

    @Test
    fun `remove categoria existente com sucesso`() {
        val id = 1
        `when`(repositorio.existsById(id)).thenReturn(true)
        doNothing().`when`(repositorio).deleteById(id)

        val response = controller.delete(id)

        assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode.value())
        assertNull(response.body)
    }

    @Test
    fun `retorna not found ao remover categoria inexistente`() {
        val id = 99
        `when`(repositorio.existsById(id)).thenReturn(false)

        val response = controller.delete(id)

        assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode.value())
        assertEquals("", response.body)
    }
}
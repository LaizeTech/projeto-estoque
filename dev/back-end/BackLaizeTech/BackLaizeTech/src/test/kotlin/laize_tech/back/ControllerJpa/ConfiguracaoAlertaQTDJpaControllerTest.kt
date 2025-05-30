package laize_tech.back.ControllerJpa

import laize_tech.back.dto.ConfiguracaoAlertasQTDDTO
import laize_tech.back.entity.ConfiguracaoAlertasQTD
import laize_tech.back.repository.ConfiguracaoAlertasQTDRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.ArgumentMatchers.any

class ConfiguracaoAlertaQTDJpaControllerTest {

    private val repository: ConfiguracaoAlertasQTDRepository = mock(ConfiguracaoAlertasQTDRepository::class.java)
    private val controller = ConfiguracaoAlertaQTDJpaController(repository)


        @BeforeEach
        fun setUp() {
            val defaultEntity = ConfiguracaoAlertasQTD(
                idConfiguracaoAlertasQTD = 1L,
                quantidadeAmarelo = 10,
                quantidadeVermelha = 20,
                quantidadeVioleta = 30
            )
            `when`(repository.findById(1L)).thenReturn(java.util.Optional.of(defaultEntity))
            `when`(repository.existsById(1L)).thenReturn(true)
            `when`(repository.save(any(ConfiguracaoAlertasQTD::class.java))).thenReturn(defaultEntity)
        }


    @Test
    @DisplayName("Deve retornar o DTO quando a entidade existe no banco de dados")
    fun getByIdReturnsDTOWhenEntityExists() {
        val result = controller.getById(1L)

        assertNotNull(result)
        assertEquals(1L, result?.idConfiguracaoAlertasQTD)
        assertEquals(10, result?.quantidadeAmarelo)
        assertEquals(20, result?.quantidadeVermelha)
        assertEquals(30, result?.quantidadeVioleta)
    }

    @Test
    @DisplayName("Deve retornar null quando a entidade não existe no banco de dados")
    fun getByIdReturnsNullWhenEntityDoesNotExist() {
        `when`(repository.findById(2L)).thenReturn(java.util.Optional.empty())

        val result = controller.getById(2L)

        assertNull(result)
    }

    @Test
    @DisplayName("Deve retornar o DTO atualizado quando a entidade existe no banco de dados")
    fun updateReturnsUpdatedDTOWhenEntityExists() {
        val dto = ConfiguracaoAlertasQTDDTO(
            quantidadeAmarelo = 15,
            quantidadeVermelha = 25,
            quantidadeVioleta = 35
        )
        val updatedEntity = ConfiguracaoAlertasQTD(
            idConfiguracaoAlertasQTD = 1L,
            quantidadeAmarelo = 15,
            quantidadeVermelha = 25,
            quantidadeVioleta = 35
        )
        `when`(repository.save(any(ConfiguracaoAlertasQTD::class.java))).thenReturn(updatedEntity)

        val result = controller.update(1L, dto)

        assertNotNull(result)
        assertEquals(1L, result?.idConfiguracaoAlertasQTD)
        assertEquals(15, result?.quantidadeAmarelo)
        assertEquals(25, result?.quantidadeVermelha)
        assertEquals(35, result?.quantidadeVioleta)
    }

    @Test
    @DisplayName("Deve retornar null quando a entidade não existe no banco de dados")
    fun updateReturnsNullWhenEntityDoesNotExist() {
        val dto = ConfiguracaoAlertasQTDDTO(
            quantidadeAmarelo = 15,
            quantidadeVermelha = 25,
            quantidadeVioleta = 35
        )
        `when`(repository.existsById(2L)).thenReturn(false)

        val result = controller.update(2L, dto)

        assertNull(result)
    }

    @Test
    @DisplayName("Deve criar e retornar o DTO quando a entidade é criada com sucesso")
    fun deleteRemovesEntityWhenItExists() {
        controller.delete(1L)

        org.mockito.Mockito.verify(repository).deleteById(1L)
    }

    @Test
    @DisplayName("Deve não fazer nada quando a entidade não existe no banco de dados")
    fun deleteDoesNothingWhenEntityDoesNotExist() {
        `when`(repository.existsById(2L)).thenReturn(false)

        controller.delete(2L)

        org.mockito.Mockito.verify(repository, org.mockito.Mockito.never()).deleteById(2L)
    }
    }
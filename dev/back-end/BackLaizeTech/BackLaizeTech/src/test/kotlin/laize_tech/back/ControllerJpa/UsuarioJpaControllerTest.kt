import laize_tech.back.ControllerJpa.UsuarioJpaController
import laize_tech.back.dto.UsuarioDTO
import laize_tech.back.entity.Empresa
import laize_tech.back.entity.Usuario
import laize_tech.back.repository.EmpresaRepository
import laize_tech.back.repository.UsuarioRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import java.util.*

class UsuarioJpaControllerTest {

 private lateinit var usuarioJpaController: UsuarioJpaController
 private lateinit var usuarioRepository: UsuarioRepository
 private lateinit var empresaRepository: EmpresaRepository

 @BeforeEach
 fun setUp() {
  usuarioRepository = mock(UsuarioRepository::class.java)
  empresaRepository = mock(EmpresaRepository::class.java)
  usuarioJpaController = UsuarioJpaController(usuarioRepository, empresaRepository)
 }

 @Test
 fun `post deve retornar 400 quando nome for nulo ou vazio`() {
  val novoUsuarioDTO = UsuarioDTO(null, "email", "senha", true, 1)
  val response = usuarioJpaController.post(novoUsuarioDTO)
  assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
  assertEquals("Os campos nome, senha e email não podem estar vazios ou nulos!", response.body)
 }

 @Test
 fun `post deve retornar 400 quando senha for nula ou vazia`() {
  val novoUsuarioDTO = UsuarioDTO("nome", "email", null, true, 1)
  val response = usuarioJpaController.post(novoUsuarioDTO)
  assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
  assertEquals("Os campos nome, senha e email não podem estar vazios ou nulos!", response.body)
 }

 @Test
 fun `post deve retornar 400 quando email for nulo ou vazio`() {
  val novoUsuarioDTO = UsuarioDTO("nome", null, "senha", true, 1)
  val response = usuarioJpaController.post(novoUsuarioDTO)
  assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
  assertEquals("Os campos nome, senha e email não podem estar vazios ou nulos!", response.body)
 }

 @Test
 fun `post deve retornar 400 quando idEmpresa for nulo`() {
  val novoUsuarioDTO = UsuarioDTO("nome", "email", "senha", true, null)
  val response = usuarioJpaController.post(novoUsuarioDTO)
  assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
 }

 @Test
 fun `post deve retornar 409 quando email ja existe`() {
  val novoUsuarioDTO = UsuarioDTO("nome", "email", "senha", true, 1)
  val empresa = Empresa(1, "nome", "cnpj")
  `when`(usuarioRepository.findAll()).thenReturn(listOf(Usuario(1, "nome", "email", "senha", true, empresa)))
  val response = usuarioJpaController.post(novoUsuarioDTO)
  assertEquals(HttpStatus.CONFLICT, response.statusCode)
  assertEquals("Já existe um usuário cadastrado com esse e-mail!", response.body)
 }

 @Test
 fun `post deve retornar 400 quando empresa nao existe`() {
  val novoUsuarioDTO = UsuarioDTO("nome", "email", "senha", true, 1)
  `when`(empresaRepository.findById(1)).thenReturn(Optional.empty())
  val response = usuarioJpaController.post(novoUsuarioDTO)
  assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
  assertEquals("Empresa com o ID 1 não encontrada", response.body)
 }

 @Test
 fun `post deve retornar 201 quando usuario for criado com sucesso e acessoFinanceiro for true`() {
  val novoUsuarioDTO = UsuarioDTO("nome", "email", "senha", true, 1)
  val empresa = Empresa(1, "nome", "cnpj")
  `when`(empresaRepository.findById(1)).thenReturn(Optional.of(empresa))
  `when`(usuarioRepository.findAll()).thenReturn(emptyList())
  val usuarioSalvo = Usuario(1, "nome", "email", "senha", true, empresa)
  `when`(usuarioRepository.save(any(Usuario::class.java))).thenReturn(usuarioSalvo)

  val response = usuarioJpaController.post(novoUsuarioDTO)

  assertEquals(HttpStatus.CREATED, response.statusCode)
  assertNotNull(response.body)
  assertEquals(usuarioSalvo, response.body)
  assertEquals(true, usuarioSalvo.acessoFinanceiro)
 }

 @Test
 fun `post deve retornar 201 quando usuario for criado com sucesso e acessoFinanceiro for false`() {
  val novoUsuarioDTO = UsuarioDTO("nome", "email", "senha", false, 1)
  val empresa = Empresa(1, "nome", "cnpj")
  `when`(empresaRepository.findById(1)).thenReturn(Optional.of(empresa))
  `when`(usuarioRepository.findAll()).thenReturn(emptyList())
  val usuarioSalvo = Usuario(1, "nome", "email", "senha", false, empresa)
  `when`(usuarioRepository.save(any(Usuario::class.java))).thenReturn(usuarioSalvo)

  val response = usuarioJpaController.post(novoUsuarioDTO)

  assertEquals(HttpStatus.CREATED, response.statusCode)
  assertNotNull(response.body)
  assertEquals(usuarioSalvo, response.body)
  assertEquals(false, usuarioSalvo.acessoFinanceiro)
 }

 @Test
 fun `post deve retornar 500 quando ocorre um erro ao salvar o usuario`() {
  val novoUsuarioDTO = UsuarioDTO("nome", "email", "senha", true, 1)
  val empresa = Empresa(1, "nome", "cnpj")
  `when`(empresaRepository.findById(1)).thenReturn(Optional.of(empresa))
  `when`(usuarioRepository.findAll()).thenReturn(emptyList())
  `when`(usuarioRepository.save(any(Usuario::class.java))).thenThrow(RuntimeException("Erro ao salvar"))

  val response = usuarioJpaController.post(novoUsuarioDTO)

  assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode)
  assertEquals("Erro ao salvar usuário", response.body)
 }

 @Test
 fun `post deve salvar usuario com os dados corretos`() {
  val novoUsuarioDTO = UsuarioDTO("nome", "email", "senha", true, 1)
  val empresa = Empresa(1, "nome", "cnpj")
  `when`(empresaRepository.findById(1)).thenReturn(Optional.of(empresa))
  `when`(usuarioRepository.findAll()).thenReturn(emptyList())
  val usuarioSalvo = Usuario(1, "nome", "email", "senha", true, empresa)
  `when`(usuarioRepository.save(any(Usuario::class.java))).thenReturn(usuarioSalvo)

  usuarioJpaController.post(novoUsuarioDTO)

  verify(usuarioRepository).save(argThat<Usuario> {
   it.nome == novoUsuarioDTO.nome &&
           it.email == novoUsuarioDTO.email &&
           it.senha == novoUsuarioDTO.senha &&
           it.acessoFinanceiro == (novoUsuarioDTO.acessoFinanceiro ?: false) &&
           it.empresa == empresa
  })
 }
}
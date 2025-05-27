import laize_tech.back.ControllerJpa.UsuarioJpaController
import laize_tech.back.dto.ListagemUsuarioDTO
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

// Testes unitários para a UsuarioJpaController.

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

 // Testes da função put:

 @Test
 fun `put deve retornar 404 quando usuario nao existe`() {
  `when`(usuarioRepository.existsById(1)).thenReturn(false)
  val usuarioDTO = UsuarioDTO("nome", "email", "senha", true, 1)
  val response = usuarioJpaController.put(1, usuarioDTO)
  assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
  assertEquals("Usuário com o ID 1 não encontrado.", response.body)
 }

 @Test
 fun `put deve retornar 400 quando nome for nulo ou vazio`() {
  `when`(usuarioRepository.existsById(1)).thenReturn(true)
  val usuarioDTO = UsuarioDTO(null, "email", "senha", true, 1)
  val response = usuarioJpaController.put(1, usuarioDTO)
  assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
  assertEquals("Os campos nome, senha e email não podem estar vazios ou nulos!", response.body)
 }

 @Test
 fun `put deve retornar 400 quando senha for nula ou vazia`() {
  `when`(usuarioRepository.existsById(1)).thenReturn(true)
  val usuarioDTO = UsuarioDTO("nome", "email", null, true, 1)
  val response = usuarioJpaController.put(1, usuarioDTO)
  assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
  assertEquals("Os campos nome, senha e email não podem estar vazios ou nulos!", response.body)
 }

 @Test
 fun `put deve retornar 400 quando email for nulo ou vazio`() {
  `when`(usuarioRepository.existsById(1)).thenReturn(true)
  val usuarioDTO = UsuarioDTO("nome", null, "senha", true, 1)
  val response = usuarioJpaController.put(1, usuarioDTO)
  assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
  assertEquals("Os campos nome, senha e email não podem estar vazios ou nulos!", response.body)
 }

 @Test
 fun `put deve retornar 400 quando idEmpresa for nulo`() {
  `when`(usuarioRepository.existsById(1)).thenReturn(true)
  val usuarioDTO = UsuarioDTO("nome", "email", "senha", true, null)
  val response = usuarioJpaController.put(1, usuarioDTO)
  assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
 }

 @Test
 fun `put deve retornar 409 quando email ja existe para outro usuario`() {
  `when`(usuarioRepository.existsById(1)).thenReturn(true)
  val usuarioDTO = UsuarioDTO("nome", "email", "senha", true, 1)
  val empresa = Empresa(1, "nome", "cnpj")
  val usuarioExistente = Usuario(2, "outro", "email", "senha", true, empresa)
  `when`(usuarioRepository.findAll()).thenReturn(listOf(usuarioExistente))
  val response = usuarioJpaController.put(1, usuarioDTO)
  assertEquals(HttpStatus.CONFLICT, response.statusCode)
  assertEquals("Já existe um usuário cadastrado com o e-mail 'email'.", response.body)
 }

 @Test
 fun `put deve retornar 400 quando empresa nao existe`() {
  `when`(usuarioRepository.existsById(1)).thenReturn(true)
  val usuarioDTO = UsuarioDTO("nome", "email", "senha", true, 1)
  `when`(empresaRepository.findById(1)).thenReturn(Optional.empty())
  val response = usuarioJpaController.put(1, usuarioDTO)
  assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
  assertEquals("Empresa com o ID 1 não encontrada", response.body)
 }

 @Test
 fun `put deve retornar 200 quando usuario for atualizado com sucesso e acessoFinanceiro for true`() {
  `when`(usuarioRepository.existsById(1)).thenReturn(true)
  val usuarioDTO = UsuarioDTO("nome", "email", "senha", true, 1)
  val empresa = Empresa(1, "nome", "cnpj")
  val usuarioExistente = Usuario(1, "nomeAntigo", "emailAntigo", "senhaAntiga", false, empresa)
  `when`(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioExistente))
  `when`(empresaRepository.findById(1)).thenReturn(Optional.of(empresa))
  `when`(usuarioRepository.findAll()).thenReturn(emptyList())
  val usuarioAtualizado = Usuario(1, "nome", "email", "senha", true, empresa)
  `when`(usuarioRepository.save(any(Usuario::class.java))).thenReturn(usuarioAtualizado)

  val response = usuarioJpaController.put(1, usuarioDTO)

  assertEquals(HttpStatus.OK, response.statusCode)
  assertNotNull(response.body)
  assertEquals(usuarioAtualizado, response.body)
  assertEquals(true, usuarioAtualizado.acessoFinanceiro)
 }

 @Test
 fun `put deve retornar 200 quando usuario for atualizado com sucesso e acessoFinanceiro for false`() {
  `when`(usuarioRepository.existsById(1)).thenReturn(true)
  val usuarioDTO = UsuarioDTO("nome", "email", "senha", false, 1)
  val empresa = Empresa(1, "nome", "cnpj")
  val usuarioExistente = Usuario(1, "nomeAntigo", "emailAntigo", "senhaAntiga", true, empresa)
  `when`(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioExistente))
  `when`(empresaRepository.findById(1)).thenReturn(Optional.of(empresa))
  `when`(usuarioRepository.findAll()).thenReturn(emptyList())
  val usuarioAtualizado = Usuario(1, "nome", "email", "senha", false, empresa)
  `when`(usuarioRepository.save(any(Usuario::class.java))).thenReturn(usuarioAtualizado)

  val response = usuarioJpaController.put(1, usuarioDTO)

  assertEquals(HttpStatus.OK, response.statusCode)
  assertNotNull(response.body)
  assertEquals(usuarioAtualizado, response.body)
  assertEquals(false, usuarioAtualizado.acessoFinanceiro)
 }

 @Test
 fun `put deve retornar 500 quando ocorre um erro ao salvar o usuario`() {
  `when`(usuarioRepository.existsById(1)).thenReturn(true)
  val usuarioDTO = UsuarioDTO("nome", "email", "senha", true, 1)
  val empresa = Empresa(1, "nome", "cnpj")
  val usuarioExistente = Usuario(1, "nomeAntigo", "emailAntigo", "senhaAntiga", false, empresa)
  `when`(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioExistente))
  `when`(empresaRepository.findById(1)).thenReturn(Optional.of(empresa))
  `when`(usuarioRepository.findAll()).thenReturn(emptyList())
  `when`(usuarioRepository.save(any(Usuario::class.java))).thenThrow(RuntimeException("Erro ao salvar"))

  val response = usuarioJpaController.put(1, usuarioDTO)

  assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode)
  assertEquals("Erro ao salvar usuário", response.body)
 }

 @Test
 fun `put deve atualizar usuario com os dados corretos`() {
  `when`(usuarioRepository.existsById(1)).thenReturn(true)
  val usuarioDTO = UsuarioDTO("nome", "email", "senha", true, 1)
  val empresa = Empresa(1, "nome", "cnpj")
  val usuarioExistente = Usuario(1, "nomeAntigo", "emailAntigo", "senhaAntiga", false, empresa)
  `when`(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioExistente))
  `when`(empresaRepository.findById(1)).thenReturn(Optional.of(empresa))
  `when`(usuarioRepository.findAll()).thenReturn(emptyList())
  `when`(usuarioRepository.save(any(Usuario::class.java))).thenReturn(usuarioExistente)

  usuarioJpaController.put(1, usuarioDTO)

  verify(usuarioRepository).save(argThat<Usuario> {
   it.nome == usuarioDTO.nome &&
           it.email == usuarioDTO.email &&
           it.senha == usuarioDTO.senha &&
           it.acessoFinanceiro == (usuarioDTO.acessoFinanceiro ?: false) &&
           it.empresa == empresa
  })
 }

// ----------
//Testes da função Post:
//  ----------

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

 // ----------
//Testes da função Delete:
//  ----------

 @Test
 fun `delete deve retornar 204 quando usuario for deletado com sucesso`() {
  // Arrange
  val id = 1
  `when`(usuarioRepository.existsById(id)).thenReturn(true)

  // Act
  val response = usuarioJpaController.delete(id)

  // Assert
  assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
  verify(usuarioRepository, times(1)).deleteById(id)
 }

 @Test
 fun `delete deve retornar 404 quando usuario nao for encontrado`() {
  // Arrange
  val id = 1
  `when`(usuarioRepository.existsById(id)).thenReturn(false)

  // Act
  val response = usuarioJpaController.delete(id)

  // Assert
  assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
  assertEquals("Usuário com o ID $id não encontrado.", response.body)
  verify(usuarioRepository, never()).deleteById(id)
 }

 // ----------
//Testes da função Get (Todos os usuarios):
//  ----------

 @Test
 fun `get deve retornar 200 e a lista de usuarios quando houver usuarios cadastrados`() {
  // Arrange
  val empresa = Empresa(1, "Empresa A", "123456789")
  val usuarios = listOf(
   Usuario(1, "Nome 1", "email1@example.com", "senha1", true, empresa),
   Usuario(2, "Nome 2", "email2@example.com", "senha2", false, empresa)
  )
  `when`(usuarioRepository.findAll()).thenReturn(usuarios)

  // Act
  val response = usuarioJpaController.get()

  // Assert
  assertEquals(HttpStatus.OK, response.statusCode)
  val body = response.body
  assert(body is List<*>)
  val listagemUsuarios = body as List<ListagemUsuarioDTO>
  assertEquals(2, listagemUsuarios.size)
  assertEquals("Nome 1", listagemUsuarios[0].nome)
  assertEquals("email1@example.com", listagemUsuarios[0].email)
  assertEquals(true, listagemUsuarios[0].acessoFinanceiro)
  assertEquals(1, listagemUsuarios[0].idEmpresa)
 }

 @Test
 fun `get deve retornar 204 quando nao houver usuarios cadastrados`() {
  // Arrange
  `when`(usuarioRepository.findAll()).thenReturn(emptyList())

  // Act
  val response = usuarioJpaController.get()

  // Assert
  assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
 }

 // ----------
//Testes da função Get (Usuarios por ID):
//  ----------

 @Test
 fun `get por id deve retornar 200 e o usuario quando o usuario for encontrado`() {
  // Arrange
  val id = 1
  val empresa = Empresa(1, "Empresa A", "123456789")
  val usuario = Usuario(1, "Nome", "email@example.com", "senha", true, empresa)
  `when`(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario))

  // Act
  val response = usuarioJpaController.get(id)

  // Assert
  assertEquals(HttpStatus.OK, response.statusCode)
  val body = response.body
  assert(body is ListagemUsuarioDTO)
  val listagemUsuario = body as ListagemUsuarioDTO
  assertEquals("Nome", listagemUsuario.nome)
  assertEquals("email@example.com", listagemUsuario.email)
  assertEquals(true, listagemUsuario.acessoFinanceiro)
  assertEquals(1, listagemUsuario.idEmpresa)
 }

 @Test
 fun `get por id deve retornar 404 quando o usuario nao for encontrado`() {
  // Arrange
  val id = 1
  `when`(usuarioRepository.findById(id)).thenReturn(Optional.empty())

  // Act
  val response = usuarioJpaController.get(id)

  // Assert
  assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
  assertEquals("Usuário com o ID $id não encontrado.", response.body)
 }

 // ----------
//Testes da função Get (Usuarios por NOME):
//  ----------

 @Test
 fun `getByNome deve retornar 200 e a lista de usuarios quando houver usuarios com o nome especificado`() {
  // Arrange
  val nome = "NomeTeste"
  val empresa = Empresa(1, "Empresa A", "123456789")
  val usuarios = listOf(
   Usuario(1, nome, "email1@example.com", "senha1", true, empresa),
   Usuario(2, nome, "email2@example.com", "senha2", false, empresa)
  )
  `when`(usuarioRepository.findByNome(nome)).thenReturn(usuarios)

  // Act
  val response = usuarioJpaController.getByNome(nome)

  // Assert
  assertEquals(HttpStatus.OK, response.statusCode)
  val body = response.body
  assert(body is List<*>)
  val listagemUsuarios = body as List<ListagemUsuarioDTO>
  assertEquals(2, listagemUsuarios.size)
  assertEquals(nome, listagemUsuarios[0].nome)
  assertEquals("email1@example.com", listagemUsuarios[0].email)
  assertEquals(true, listagemUsuarios[0].acessoFinanceiro)
  assertEquals(1, listagemUsuarios[0].idEmpresa)
 }

 @Test
 fun `getByNome deve retornar 204 quando nao houver usuarios com o nome especificado`() {
  // Arrange
  val nome = "NomeTeste"
  `when`(usuarioRepository.findByNome(nome)).thenReturn(emptyList())

  // Act
  val response = usuarioJpaController.getByNome(nome)

  // Assert
  assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
 }

 // ----------
//Testes da função Get (Usuarios por Acesso Financeiro):
//  ----------

 @Test
 fun `getByAcessoFinanceiro deve retornar 200 e a lista de usuarios quando houver usuarios com o acessoFinanceiro especificado`() {
  // Arrange
  val acessoFinanceiro = true
  val empresa = Empresa(1, "Empresa A", "123456789")
  val usuarios = listOf(
   Usuario(1, "Nome 1", "email1@example.com", "senha1", acessoFinanceiro, empresa),
   Usuario(2, "Nome 2", "email2@example.com", "senha2", acessoFinanceiro, empresa)
  )
  `when`(usuarioRepository.findByAcessoFinanceiro(acessoFinanceiro)).thenReturn(usuarios)

  // Act
  val response = usuarioJpaController.getByAcessoFinanceiro(acessoFinanceiro)

  // Assert
  assertEquals(HttpStatus.OK, response.statusCode)
  val body = response.body
  assert(body is List<*>)
  val listagemUsuarios = body as List<ListagemUsuarioDTO>
  assertEquals(2, listagemUsuarios.size)
  assertEquals("Nome 1", listagemUsuarios[0].nome)
  assertEquals("email1@example.com", listagemUsuarios[0].email)
  assertEquals(acessoFinanceiro, listagemUsuarios[0].acessoFinanceiro)
  assertEquals(1, listagemUsuarios[0].idEmpresa)
 }

 @Test
 fun `getByAcessoFinanceiro deve retornar 204 quando nao houver usuarios com o acessoFinanceiro especificado`() {
  // Arrange
  val acessoFinanceiro = true
  `when`(usuarioRepository.findByAcessoFinanceiro(acessoFinanceiro)).thenReturn(emptyList())

  // Act
  val response = usuarioJpaController.getByAcessoFinanceiro(acessoFinanceiro)

  // Assert
  assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
 }

}
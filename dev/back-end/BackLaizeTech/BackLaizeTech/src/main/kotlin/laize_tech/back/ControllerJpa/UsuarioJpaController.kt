package laize_tech.back.ControllerJpa

import jakarta.validation.Valid
import laize_tech.back.dto.ListagemUsuarioDTO
import laize_tech.back.dto.UsuarioDTO
import laize_tech.back.entity.Usuario
import laize_tech.back.repository.EmpresaRepository
import laize_tech.back.repository.UsuarioRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/usuarios")
class UsuarioJpaController(
    val repositorio: UsuarioRepository,
    val empresaRepository: EmpresaRepository
) {

    @GetMapping
    fun get(): ResponseEntity<List<ListagemUsuarioDTO>> {
        val usuarios = repositorio.findAll()
        val listagemUsuarios = usuarios.map { usuario ->
            ListagemUsuarioDTO(
                nome = usuario.nome,
                email = usuario.email,
                acessoFinanceiro = usuario.acessoFinanceiro,
                idEmpresa = usuario.empresa.idEmpresa
            )
        }
        return if (listagemUsuarios.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(listagemUsuarios)
        }
    }
    
    @PostMapping("/adicionar")
    fun post(@RequestBody @Valid novoUsuarioDTO: UsuarioDTO): ResponseEntity<Any> {
        if (novoUsuarioDTO.nome.isNullOrBlank() || novoUsuarioDTO.senha.isNullOrBlank() || novoUsuarioDTO.email.isNullOrBlank() || novoUsuarioDTO.idEmpresa == null) {
            return ResponseEntity.status(400).body("Os campos nome, senha e email não podem estar vazios ou nulos!")
        }

        if (repositorio.findAll().any { it.email == novoUsuarioDTO.email }) {
            return ResponseEntity.status(409).body("Já existe um usuário cadastrado com esse e-mail!")
        }

        val empresa = empresaRepository.findById(novoUsuarioDTO.idEmpresa).orElse(null)
            ?: return ResponseEntity.status(400).body("Empresa com o ID ${novoUsuarioDTO.idEmpresa} não encontrada")

        val novoUsuario = Usuario(
            idUsuario = 0,
            nome = novoUsuarioDTO.nome,
            email = novoUsuarioDTO.email,
            senha = novoUsuarioDTO.senha,
            acessoFinanceiro = novoUsuarioDTO.acessoFinanceiro ?: false,
            empresa = empresa
        )
        val usuarioSalvo = repositorio.save(novoUsuario)
        return ResponseEntity.status(201).body(usuarioSalvo)
    }

    @PutMapping("/{id}")
    fun put(@PathVariable id: Int, @RequestBody usuarioAtualizado: Usuario): ResponseEntity<Any> {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.status(404).body("Usuário com o ID $id não encontrado.")
        }

        if (usuarioAtualizado.nome.isBlank() || usuarioAtualizado.senha.isBlank() || usuarioAtualizado.email.isBlank()) {
            return ResponseEntity.status(400).body("Os campos nome, senha e email não podem estar vazios ou nulos!")
        }

        if (repositorio.findAll().any { it.email == usuarioAtualizado.email && it.idUsuario != id.toLong() }) {
            return ResponseEntity.status(409).body("Já existe um usuário cadastrado com o e-mail '${usuarioAtualizado.email}'.")
        }

        usuarioAtualizado.idUsuario = id.toLong()
        val usuarioSalvo = repositorio.save(usuarioAtualizado)
        return ResponseEntity.status(200).body(usuarioSalvo)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<String> {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id)
            return ResponseEntity.status(204).build()
        }
        return ResponseEntity.status(404).body("Usuário com o ID $id não encontrado.")
    }
}
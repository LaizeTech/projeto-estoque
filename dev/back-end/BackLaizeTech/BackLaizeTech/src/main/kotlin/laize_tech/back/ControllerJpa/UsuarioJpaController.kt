package laize_tech.back.ControllerJpa

import jakarta.validation.Valid
import laize_tech.back.dto.UsuarioDTO
import laize_tech.back.entity.Usuario
import laize_tech.back.repository.UsuarioRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/usuarios")
class UsuarioJpaController(val repositorio: UsuarioRepository) {

    @GetMapping
    fun get(): ResponseEntity<List<UsuarioDTO>> {
        val usuarios = repositorio.findUsuarioDTOs()
        return if (usuarios.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(usuarios)
        }
    }

    @GetMapping("/por-nome")
    fun getByNome(@RequestParam nomeFragmento: String): ResponseEntity<List<UsuarioDTO>> {
        val usuarios = repositorio.findByNomeContainingIgnoreCase(nomeFragmento)
        return if (usuarios.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            val usuariosDTO = usuarios.map { UsuarioDTO(it.nome, it.email, it.acessoFinanceiro) }
            ResponseEntity.status(200).body(usuariosDTO)
        }
    }

    @GetMapping("/com-acesso-financeiro")
    fun getUsuariosComAcessoFinanceiro(): ResponseEntity<List<UsuarioDTO>> {
        val usuarios = repositorio.findByAcessoFinanceiroTrue()
        return if (usuarios.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            val usuariosDTO = usuarios.map { UsuarioDTO(it.nome, it.email, it.acessoFinanceiro) }
            ResponseEntity.status(200).body(usuariosDTO)
        }
    }

    @GetMapping("/por-email")
    fun getByEmail(@RequestParam email: String): ResponseEntity<UsuarioDTO> {
        val usuarioDTO = repositorio.findUsuarioDTOByEmail(email)
        return if (usuarioDTO == null) {
            ResponseEntity.status(404).body(usuarioDTO)
        } else {
            ResponseEntity.status(200).body(usuarioDTO)
        }
    }

    @PostMapping("/adicionar")
    fun post(@RequestBody @Valid novoUsuario: Usuario): ResponseEntity<Any> {
        if (novoUsuario.nome.isBlank() || novoUsuario.senha.isBlank() || novoUsuario.email.isBlank()) {
            return ResponseEntity.status(400).body("Os campos nome, senha e email não podem estar vazios ou nulos!")
        }

        if (repositorio.findAll().any { it.email == novoUsuario.email }) {
            return ResponseEntity.status(409).body("Já existe um usuário cadastrado com esse e-mail!")
        }

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
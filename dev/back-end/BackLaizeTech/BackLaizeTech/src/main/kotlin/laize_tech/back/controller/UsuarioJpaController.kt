package laize_tech.back.controller

import jakarta.validation.Valid
import laize_tech.back.dto.UsuarioDTO
import laize_tech.back.entity.Usuario
import laize_tech.back.repository.UsuarioRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/usuarios")
class UsuarioJpaController (val repositorio: UsuarioRepository) {

    @GetMapping
    fun get(): ResponseEntity<List<UsuarioDTO>> {
        val usuarios = repositorio.findUsuarioDTOs()

        return if (usuarios.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(usuarios)
        }
    }


    @PostMapping ("/adicionar")
    fun post(@RequestBody @Valid novoUsuario: Usuario):
            ResponseEntity<Usuario> {

        val usuarios = repositorio.save(novoUsuario)
        return ResponseEntity.status(201).body(usuarios)
    }

    @PutMapping("/{id}")
    fun put (@PathVariable id:Int, @RequestBody usuarioAtualizado: Usuario): ResponseEntity<Usuario>{

        if (!repositorio.existsById(id)){
            return ResponseEntity.status(404).build()
        }

        usuarioAtualizado.id = id
        val usuarios = repositorio.save(usuarioAtualizado)
        return ResponseEntity.status(200).body(usuarios)
    }

    @DeleteMapping ("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<String> {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id)
            return ResponseEntity.status(204).build()
        }
        val mensagem = "Não foi possível deletar o usuário com id $id"
        return ResponseEntity.status(404).body(mensagem)
    }
}
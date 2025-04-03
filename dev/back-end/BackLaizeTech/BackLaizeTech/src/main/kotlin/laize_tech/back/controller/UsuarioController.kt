package laize_tech.back.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/usuario")
class UsuarioController {

    val lista_usuario = mutableListOf<Usuario>(
        Usuario("Ayrton Casa", "ayrton.casa@sptech.school", "111111", false, "3045u89"),
        Usuario("Gabriel Gomes", "gabriel.gcorrea@sptech.school", "2222222", false, "0925485")
    )

    @GetMapping("/listar")
    fun listarUsuarios(): ResponseEntity<List<Usuario>> {

        if(lista_usuario.isEmpty()){
            return ResponseEntity.status(204).build()
        }else{
            return ResponseEntity.status(200).body(lista_usuario)
        }

    }

    @GetMapping("/listar/{id}")
    fun listarUsuariosID(@PathVariable id: Int): ResponseEntity<Usuario> {
        if(id in 0..lista_usuario.size - 1){
            return ResponseEntity.status(200).body(lista_usuario[id])
        }else{
            return ResponseEntity.status(404).build()
        }
    }

    @PostMapping
    fun criarUsuario(@RequestBody novoUsuario: Usuario): ResponseEntity<Usuario>{
        lista_usuario.add(novoUsuario)
        return ResponseEntity.status(201).body(novoUsuario)
    }

    @DeleteMapping("/deletar/{id}")
    fun deletarUsuario(@PathVariable id: Int): ResponseEntity<Usuario>{
        if (id in 0..lista_usuario.size - 1){
            lista_usuario.removeAt(id)
            return ResponseEntity.status(200).build()
        }else{
            return ResponseEntity.status(400).build()
        }
    }

    @PutMapping("/atualizar/{id}")
    fun atualizarUsuario(@PathVariable id: Int, @RequestBody usuarioAtualizado: Usuario): ResponseEntity<Usuario> {
        if (id in 0..lista_usuario.size - 1) {
            lista_usuario[id] = usuarioAtualizado
            return ResponseEntity.status(200).body(usuarioAtualizado)
        } else {
            return ResponseEntity.status(400).build()
        }
    }

    @PatchMapping("/atualizar-telefone/{id}/{novoTelefone}")
    fun atualizarCampoUsuario(@PathVariable id: Int, @PathVariable novoTelefone: String): ResponseEntity<Usuario>{
        if (id in 0..lista_usuario.size - 1){
            lista_usuario[id].telefone = novoTelefone
            return ResponseEntity.status(200).body(lista_usuario[id])
        }else{
            return ResponseEntity.status(400).build()
        }
    }

    }



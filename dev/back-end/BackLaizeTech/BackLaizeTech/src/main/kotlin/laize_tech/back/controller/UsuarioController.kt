package laize_tech.back.controller

import laize_tech.back.dto.LoginDTO
import laize_tech.back.service.JwtUtil
import laize_tech.back.service.UsuarioService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/usuario")
class UsuarioController(val usuarioService: UsuarioService) {

    val lista_usuario = mutableListOf<Usuario>(
        Usuario("Ayrton Casa", "ayrton.casa@sptech.school", "111111", false, "3045u89"),
        Usuario("Gabriel Gomes", "gabriel.gcorrea@sptech.school", "2222222", false, "0925485")
    )

    @GetMapping("/listar")
    fun listarUsuarios(): ResponseEntity<List<Usuario>> {
        return if (lista_usuario.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(lista_usuario)
        }
    }

    @GetMapping("/listar/{id}")
    fun listarUsuariosID(@PathVariable id: Int): ResponseEntity<Usuario> {
        return if (id in 0..lista_usuario.size - 1) {
            ResponseEntity.status(200).body(lista_usuario[id])
        } else {
            ResponseEntity.status(404).build()
        }
    }

    @PostMapping
    fun criarUsuario(@RequestBody novoUsuario: Usuario): ResponseEntity<Usuario> {
        lista_usuario.add(novoUsuario)
        return ResponseEntity.status(201).body(novoUsuario)
    }

    @DeleteMapping("/deletar/{id}")
    fun deletarUsuario(@PathVariable id: Int): ResponseEntity<Usuario> {
        return if (id in 0..lista_usuario.size - 1) {
            lista_usuario.removeAt(id)
            ResponseEntity.status(200).build()
        } else {
            ResponseEntity.status(400).build()
        }
    }

    @PutMapping("/atualizar/{id}")
    fun atualizarUsuario(@PathVariable id: Int, @RequestBody usuarioAtualizado: Usuario): ResponseEntity<Usuario> {
        return if (id in 0..lista_usuario.size - 1) {
            lista_usuario[id] = usuarioAtualizado
            ResponseEntity.status(200).body(usuarioAtualizado)
        } else {
            ResponseEntity.status(400).build()
        }
    }

    @PatchMapping("/atualizar-telefone/{id}/{novoTelefone}")
    fun atualizarCampoUsuario(@PathVariable id: Int, @PathVariable novoTelefone: String): ResponseEntity<Usuario> {
        return if (id in 0..lista_usuario.size - 1) {
            lista_usuario[id].telefone = novoTelefone
            ResponseEntity.status(200).body(lista_usuario[id])
        } else {
            ResponseEntity.status(400).build()
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody loginDTO: LoginDTO): ResponseEntity<String> {
        var token = usuarioService.login(loginDTO.email, loginDTO.senha)
        var acesso = JwtUtil().chave

        return if (token != null) {
            acesso = token
            print("token atualizado")
            println(acesso.toString())
            ResponseEntity.ok(token)

        } else {
            ResponseEntity.status(401).body("Credenciais inválidas") // Retorna 401 em caso de falha
        }
    }
}

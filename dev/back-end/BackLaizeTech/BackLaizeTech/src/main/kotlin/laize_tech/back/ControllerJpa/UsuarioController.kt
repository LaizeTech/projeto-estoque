//package laize_tech.back.controller
//
//import org.springframework.http.ResponseEntity
//import org.springframework.web.bind.annotation.*
//
//@RestController
//@RequestMapping("/usuarios")
//class UsuarioController {
//
//    val listaUsuario = mutableListOf(
//        Usuario("Ayrton Casa", "ayrton.casa@sptech.school", false, "3045u89", false),
//        Usuario("Gabriel Gomes", "gabriel.gcorrea@sptech.school", false, "0925485", false)
//    )
//
//    // Endpoint para listar todos os usuários
//    @GetMapping("/listar")
//    fun listarUsuarios(): ResponseEntity<Any> {
//        val usuarioLogado = listaUsuario.any { it.status }
//        if (!usuarioLogado) {
//            return ResponseEntity.status(401).body("Você precisa estar logado para acessar esta funcionalidade.")
//        }
//        return if (listaUsuario.isEmpty()) {
//            ResponseEntity.status(204).build()
//        } else {
//            ResponseEntity.status(200).body(listaUsuario)
//        }
//    }
//
//    // Endpoint para listar um usuário por ID
//    @GetMapping("/listar/{id}")
//    fun listarUsuariosID(@PathVariable id: Int): ResponseEntity<Usuario> {
//        return if (id in listaUsuario.indices) {
//            ResponseEntity.status(200).body(listaUsuario[id])
//        } else {
//            ResponseEntity.status(404).build()
//        }
//    }
//
//    // Endpoint para criar um novo usuário
//    @PostMapping
//    fun criarUsuario(@RequestBody novoUsuario: Usuario): ResponseEntity<Usuario> {
//        listaUsuario.add(novoUsuario)
//        return ResponseEntity.status(201).body(novoUsuario)
//    }
//
//    // Endpoint para deletar um usuário por ID
//    @DeleteMapping("/deletar/{id}")
//    fun deletarUsuario(@PathVariable id: Int): ResponseEntity<Void> {
//        return if (id in listaUsuario.indices) {
//            listaUsuario.removeAt(id)
//            ResponseEntity.status(200).build()
//        } else {
//            ResponseEntity.status(400).build()
//        }
//    }
//
//    // Endpoint para atualizar um usuário por ID
//    @PutMapping("/atualizar/{id}")
//    fun atualizarUsuario(@PathVariable id: Int, @RequestBody usuarioAtualizado: Usuario): ResponseEntity<Usuario> {
//        return if (id in listaUsuario.indices) {
//            listaUsuario[id] = usuarioAtualizado
//            ResponseEntity.status(200).body(usuarioAtualizado)
//        } else {
//            ResponseEntity.status(400).build()
//        }
//    }
//
//    @PostMapping("/login")
//    fun login(@RequestBody usuarioRequest: Usuario): ResponseEntity<String> {
//        val usuario = listaUsuario.find { it.email == usuarioRequest.email }
//
//        return if (usuario != null && usuario.senha == usuarioRequest.senha) {
//            usuario.status = true
//            ResponseEntity.ok("Login realizado com sucesso! Status do usuário alterado para 'true'.")
//        } else {
//            ResponseEntity.status(401).body("Email ou senha incorretos.")
//        }
//    }
//
//    @PostMapping("/logout")
//    fun logout(@RequestBody dados: Usuario): ResponseEntity<String> {
//        val usuario = listaUsuario.find { it.email == dados.email }
//
//        return if (usuario != null && usuario.status) {
//            usuario.status = false
//            ResponseEntity.ok("Logout realizado com sucesso.")
//        } else {
//            ResponseEntity.status(400).body("Usuário não encontrado ou já deslogado.")
//        }
//    }
//
//
//}

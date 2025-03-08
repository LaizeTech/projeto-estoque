package laize_tech.back

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/usuario")
class UsuarioController {

    val lista_usuario = mutableListOf<Usuario>(
        Usuario("Ayrton Casa", "ayrton.casa@sptech.school", "111111", false),
        Usuario("Gabriel Gomes", "gabriel.gcorrea@sptech.school", "2222222", false)
    )

    @GetMapping("/listar")
    fun listarUsuarios(): ResponseEntity<List<Usuario>> {

        if(lista_usuario.isEmpty()){
            return ResponseEntity.status(204).build()
        }else{
            return ResponseEntity.status(200).body(lista_usuario)
        }

    }

    @PostMapping
    fun criarUsuario(@RequestBody novoUsuario: Usuario): ResponseEntity<Usuario>{
        lista_usuario.add(novoUsuario)
        return ResponseEntity.status(201).body(novoUsuario)
    }


}
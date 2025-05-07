package laize_tech.back.service

import laize_tech.back.controller.Usuario
import org.springframework.stereotype.Service

@Service
class UsuarioService(val jwtUtil: JwtUtil) {

    val listaUsuario = mutableListOf<Usuario>(
        Usuario("Ayrton Casa", "ayrton.casa@sptech.school", "111111", false, "3045u89"),
        Usuario("Gabriel Gomes", "gabriel.gcorrea@sptech.school", "2222222", false, "0925485")
    )

    fun login(email: String, senha: String): String? {
        val usuario = listaUsuario.find { it.email == email }

        return if (usuario != null && usuario.senha == senha) {
            // Gera o token JWT se a senha for válida
            jwtUtil.gerarToken(email)
        } else {
            null // Retorna null se o login falhar
        }
    }
}
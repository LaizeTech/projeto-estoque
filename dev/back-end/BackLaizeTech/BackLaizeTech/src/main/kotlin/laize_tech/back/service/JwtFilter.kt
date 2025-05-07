package laize_tech.back.service

import jakarta.servlet.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component

@Component
class JwtFilter(val jwtUtil: JwtUtil) : Filter {

    private val rotasPublicas = listOf("/usuario/login")

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain) {
        val req = request as HttpServletRequest
        val res = response as HttpServletResponse

        val path = req.requestURI

        if (rotasPublicas.contains(path)) {
            // Libera as rotas públicas sem verificar o token
            chain.doFilter(request, response)
            return
        }
    }
}


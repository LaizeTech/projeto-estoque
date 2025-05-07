package laize_tech.back.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtil {
    internal var chave = "mykey"
    private val expiracaoMs = 15 * 60 * 1000 // 15 minutos

    fun gerarToken(email: String): String {
        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + expiracaoMs))
            .signWith(SignatureAlgorithm.HS512, chave)
            .compact()
    }



}

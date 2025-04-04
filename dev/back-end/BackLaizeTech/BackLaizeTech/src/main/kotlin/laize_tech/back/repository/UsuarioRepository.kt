package laize_tech.back.repository

import jakarta.transaction.Transactional
import laize_tech.back.entity.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface UsuarioRepository : JpaRepository<Usuario, Int> {

    @Transactional
    @Modifying
    @Query("select e from Usuario e") //JPQL --> NÃO é SQL.
    fun findAllUsuario(): List<Usuario>

}
package laize_tech.back.repository

import jakarta.transaction.Transactional
import laize_tech.back.dto.UsuarioDTO
import laize_tech.back.entity.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface UsuarioRepository : JpaRepository<Usuario, Int> {

    @Transactional
    @Modifying
    @Query("SELECT new laize_tech.back.dto.UsuarioDTO(u.nome, u.email, u.acessoFinanceiro) FROM Usuario u")
    fun findUsuarioDTOs(): List<UsuarioDTO>

//    @Transactional
//    @Modifying
//    @Query("")
//    fun CriarUsuario();
}

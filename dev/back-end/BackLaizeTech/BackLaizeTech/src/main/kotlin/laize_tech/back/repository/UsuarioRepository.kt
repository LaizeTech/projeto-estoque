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

    // Dynamic Finder: usuários com acesso financeiro
    fun findByAcessoFinanceiroTrue(): List<Usuario>

    // Dynamic Finder: nome contém (like)
    fun findByNomeContainingIgnoreCase(nomeFragmento: String): List<Usuario>

    //Contar quantos usuários têm acesso financeiro
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.acessoFinanceiro = true")
    fun countUsuariosComAcessoFinanceiro(): Long

    //Buscar DTO de usuário por e-mail (retorno direto para exibição)
    @Query("SELECT new laize_tech.back.dto.UsuarioDTO(u.nome, u.email, u.acessoFinanceiro) FROM Usuario u WHERE u.email = :email")
    fun findUsuarioDTOByEmail(email: String): UsuarioDTO?
}

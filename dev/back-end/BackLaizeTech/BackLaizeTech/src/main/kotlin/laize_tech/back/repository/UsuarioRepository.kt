package laize_tech.back.repository

import jakarta.transaction.Transactional
import laize_tech.back.dto.BuscarFuncDTO
import laize_tech.back.dto.UsuarioDTO
import laize_tech.back.entity.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.util.Optional

interface UsuarioRepository : JpaRepository<Usuario, Int> {
    fun findByNome(nome: String): List<Usuario>

    fun findByAcessoFinanceiro(AcessoFinanceiro: Boolean): List<Usuario>

    fun findByEmail(email: String): Optional<Usuario>

    @Query(nativeQuery = true, value = """SELECT count(id_usuario) 
            FROM Usuario
            WHERE status_ativo = 1;""")
    fun countByAtivo(ativo: Boolean): Long

    @Query(nativeQuery = true, value = """SELECT id_usuario, nome, status_ativo
            FROM Usuario
            WHERE id_empresa = :idEmpresa AND acesso_financeiro = 0;""")
    fun findByIdEmpresaAndAcessoFinanceiro(idEmpresa: Int): List<BuscarFuncDTO>

}

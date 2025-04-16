package laize_tech.back.repository

import jakarta.transaction.Transactional
import laize_tech.back.entity.CompraProduto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface CompraProdutoRepository : JpaRepository<CompraProduto, Int> {

    @Transactional
    @Modifying
    @Query("select e from CompraProduto e") //JPQL --> NÃO é SQL.
    fun findAllCompraProdutos(): List<CompraProduto>

}
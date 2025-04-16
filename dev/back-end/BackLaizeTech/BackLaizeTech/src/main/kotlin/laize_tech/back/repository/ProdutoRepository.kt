package laize_tech.back.repository

import jakarta.transaction.Transactional
import laize_tech.back.entity.Produto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface ProdutoRepository : JpaRepository<Produto, Int> {

    @Transactional
    @Modifying
    @Query("select e from Produto e") //JPQL --> NÃO é SQL.
    fun findAllPlataformas(): List<Produto>

}
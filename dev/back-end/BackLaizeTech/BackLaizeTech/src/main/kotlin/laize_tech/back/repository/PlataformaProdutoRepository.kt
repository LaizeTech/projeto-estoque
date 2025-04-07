package laize_tech.back.repository

import jakarta.transaction.Transactional
import laize_tech.back.entity.PlataformaProduto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface PlataformaProdutoRepository : JpaRepository<PlataformaProduto, Int> {

    @Transactional
    @Modifying
    @Query("select e from PlataformaProduto e") //JPQL --> NÃO é SQL.
    fun findAllPlataformas(): List<PlataformaProduto>

}
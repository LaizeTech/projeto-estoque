package laize_tech.back.repository

import jakarta.transaction.Transactional
import laize_tech.back.entity.ItensVenda
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface ItensVendaRepository : JpaRepository<ItensVenda, Int> {

    @Transactional
    @Modifying
    @Query("select e from ItensVenda e") //JPQL --> NÃO é SQL.
    fun findAllItensVenda(): List<ItensVenda>

}
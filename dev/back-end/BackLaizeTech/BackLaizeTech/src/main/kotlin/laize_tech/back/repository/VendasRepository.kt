package laize_tech.back.repository

import jakarta.transaction.Transactional
import laize_tech.back.entity.Vendas
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface VendasRepository : JpaRepository<Vendas, Int> {

    @Transactional
    @Modifying
    @Query("select e from Vendas e") //JPQL --> NÃO é SQL.
    fun findAllVendas(): List<Vendas>

}
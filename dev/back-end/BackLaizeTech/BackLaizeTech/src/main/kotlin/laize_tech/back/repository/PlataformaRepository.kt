package laize_tech.back.repository

import jakarta.transaction.Transactional
import laize_tech.back.entity.Plataforma
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface PlataformaRepository : JpaRepository<Plataforma, Int> {

    @Transactional
    @Modifying
    @Query("select e from Plataforma e") //JPQL --> NÃO é SQL.
    fun findAllPlataformas(): List<Plataforma>

}
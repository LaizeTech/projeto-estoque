package laize_tech.back.repository

import jakarta.transaction.Transactional
import laize_tech.back.entity.Produto
import laize_tech.back.entity.ProdutoCaracteristica
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying

interface ProdutoCaracteristicaRepository : JpaRepository<ProdutoCaracteristica, Int> {

    fun findByCaracteristicaId(codigo: Int): List<ProdutoCaracteristica>

}
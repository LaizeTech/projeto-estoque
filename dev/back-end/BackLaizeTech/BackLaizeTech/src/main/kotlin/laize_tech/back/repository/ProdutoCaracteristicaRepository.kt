package laize_tech.back.repository

import laize_tech.back.entity.ProdutoCaracteristica
import org.springframework.data.jpa.repository.JpaRepository

interface ProdutoCaracteristicaRepository : JpaRepository<ProdutoCaracteristica, Int> {

    fun findByCaracteristica_IdCaracteristica(codigo: Int): List<ProdutoCaracteristica>

}
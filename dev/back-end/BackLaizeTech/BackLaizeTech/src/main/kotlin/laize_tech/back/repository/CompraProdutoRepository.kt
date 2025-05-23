package laize_tech.back.repository

import laize_tech.back.entity.CompraProduto
import org.springframework.data.jpa.repository.JpaRepository

interface CompraProdutoRepository : JpaRepository<CompraProduto, Int> {

//    @Transactional
//    @Modifying
//    @Query("select e from CompraProduto e") //JPQL --> NÃO é SQL.
//    fun findAllCompraProdutos(): List<CompraProduto>
//
//    //Buscar compras por intervalo de datas
//    fun findByDtCompraBetween(inicio: LocalDate, fim: LocalDate): List<CompraProduto>
//
//    //Buscar compras de um determinado produto
//    fun findByProdutoIdProduto(idProduto: Int): List<CompraProduto>
//
//    //Somar valor total gasto em compras de um produto
//    @Query("SELECT SUM(c.precoCompra * c.quantidadeProduto) FROM CompraProduto c WHERE c.produto.idProduto = :idProduto")
//    fun somaTotalPorProduto(idProduto: Int): Double?
//
//    //Agrupar total de compras por data (para gráficos)
//    @Query("SELECT c.dtCompra, SUM(c.precoCompra * c.quantidadeProduto) FROM CompraProduto c GROUP BY c.dtCompra ORDER BY c.dtCompra ASC")
//    fun totalPorData(): List<Array<Any>>
}
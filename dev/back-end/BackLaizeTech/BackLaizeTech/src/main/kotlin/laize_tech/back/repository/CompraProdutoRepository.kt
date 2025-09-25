package laize_tech.back.repository

import laize_tech.back.entity.CompraProduto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

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

    @Query(nativeQuery = true, value = """
        SELECT 
            p.nome_produto as nome_produto,
            cp.preco_compra as preco_compra,
            cp.quantidade_produto as quantidade_produto
        FROM Compra_Produto cp
        JOIN Produto p ON cp.id_produto = p.id_produto
        ORDER BY cp.dt_compra DESC
        LIMIT 5
    """)
    fun findUltimasCompra(): List<Map<String, Any>>

    @Query(nativeQuery = true, value = """
SELECT COUNT(id_compra_produto) AS quantidade_compra
FROM Compra_Produto
WHERE dt_compra >= CURDATE() - INTERVAL 2 DAY;
    """)
    fun countComprasUltimos3Dias(): Int

}
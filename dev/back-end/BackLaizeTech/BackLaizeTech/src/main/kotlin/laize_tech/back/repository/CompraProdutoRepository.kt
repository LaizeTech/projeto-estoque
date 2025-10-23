package laize_tech.back.repository

import laize_tech.back.entity.CompraProduto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CompraProdutoRepository : JpaRepository<CompraProduto, Int> {

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

    @Query(nativeQuery = true, value = """
    SELECT co.id_compra_produto, 
        co.fornecedor, 
        co.preco_compra, 
        DATE (co.dt_compra) as dt_compra, 
        co.quantidade_produto,
        ca.nome_categoria,
        p.nome_produto
    FROM
        compra_produto co
    JOIN produto p ON co.id_produto = p.id_produto
    JOIN categoria ca ON p.id_categoria = ca.id_categoria
    ORDER BY co.dt_compra;
    """)
    fun findHistoricoCompra(): List<HistoricoCompraProjection>

}

package laize_tech.back.repository

import laize_tech.back.entity.Produto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query


interface ProdutoRepository : JpaRepository<Produto, Int> {

    @Query(
        value = """
            SELECT 
                MONTHNAME(cp.dt_compra) AS mes,
                SUM(cp.quantidade_produto) AS quantidade_produtos,
                SUM(cp.preco_compra * cp.quantidade_produto) AS valor_investido
            FROM 
                Compra_Produto cp
            WHERE 
                MONTH(cp.dt_compra) = MONTH(CURDATE()) 
                AND YEAR(cp.dt_compra) = YEAR(CURDATE())
            GROUP BY 
                mes
        """, nativeQuery = true
    )
    fun getEntradasMesAtual(): List<Array<Any>>

    @Query(
        value = """
            SELECT 
                DATE_FORMAT(s.dt_venda, '%M') AS mes
            FROM Itens_Saida isv
            JOIN Produto p ON isv.id_produto = p.id_produto
            JOIN Saida s ON isv.id_saida = s.id_saida
            WHERE s.dt_venda >= DATE_SUB(CURDATE(), INTERVAL 4 MONTH)
              AND s.id_tipo_saida = 1
              AND s.id_status_venda = 1
              AND s.id_plataforma = :plataforma -- Adicionado/Corrigido
            GROUP BY YEAR(s.dt_venda), MONTH(s.dt_venda), DATE_FORMAT(s.dt_venda, '%M')
            ORDER BY YEAR(s.dt_venda), MONTH(s.dt_venda)
        """, nativeQuery = true
    )
    fun getMesAtual(plataforma: Long): List<Array<Any>>

    @Query(
        value = """
            SELECT 
                SUM(isv.quantidade) AS quantidade_produtos
            FROM Itens_Saida isv
            JOIN Produto p ON isv.id_produto = p.id_produto
            JOIN Saida s ON isv.id_saida = s.id_saida
            WHERE s.dt_venda >= DATE_SUB(CURDATE(), INTERVAL 4 MONTH)
              AND s.id_tipo_saida = 1
              AND s.id_status_venda = 1
              AND s.id_plataforma = :plataforma -- Adicionado/Corrigido
            GROUP BY YEAR(s.dt_venda), MONTH(s.dt_venda)
            ORDER BY YEAR(s.dt_venda), MONTH(s.dt_venda)
        """, nativeQuery = true
    )
    fun getqtdProdutoVendido(plataforma: Long): List<Array<Any>>

    @Query(
        value = """
            SELECT 
                SUM(isv.quantidade * s.preco_venda) AS valor_total
            FROM Itens_Saida isv
            JOIN Produto p ON isv.id_produto = p.id_produto
            JOIN Saida s ON isv.id_saida = s.id_saida
            WHERE s.dt_venda >= DATE_SUB(CURDATE(), INTERVAL 4 MONTH)
              AND s.id_tipo_saida = 1
              AND s.id_status_venda = 1
              AND s.id_plataforma = :plataforma -- Adicionado/Corrigido
            GROUP BY YEAR(s.dt_venda), MONTH(s.dt_venda)
            ORDER BY YEAR(s.dt_venda), MONTH(s.dt_venda)
        """, nativeQuery = true
    )
    fun getTotalVendido(plataforma: Long): List<Array<Any>>

    @Query(
        value = """
            SELECT
                p.nome_Produto,
                SUM(isv.quantidade) AS totalVendido
            FROM
                Itens_Saida isv
            JOIN
                Produto p ON isv.id_Produto = p.id_Produto
            JOIN
                Saida s ON isv.id_Saida = s.id_Saida
            WHERE
                s.dt_Venda >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)
                AND s.id_tipo_saida = 1
                AND s.id_status_venda = 1
                AND s.id_plataforma = :plataforma -- Corrigido para usar o placeholder
            GROUP BY
                p.nome_Produto
            ORDER BY
                totalVendido DESC
            LIMIT 5
        """, nativeQuery = true
    )
    fun getTop5Produtos(plataforma: Long): List<Array<Any>>

    @Query(
        value = """
            SELECT p.nome_Produto
            FROM Produto p
            WHERE p.id_Produto NOT IN (
                SELECT DISTINCT isv.id_Produto
                FROM Itens_Saida isv
                JOIN Saida s ON isv.id_Saida = s.id_Saida
                WHERE s.dt_Venda >= DATE_SUB(CURDATE(), INTERVAL 60 DAY)
                  AND s.id_tipo_saida = 1
                  AND s.id_status_venda = 1
                  AND s.id_plataforma = :plataforma -- Já estava correto
            )
        """, nativeQuery = true
    )
    fun getProdutosInativos(plataforma: Long): List<String>

    @Query(
        value = """
        SELECT 
            MONTHNAME(s.dt_venda) AS mes,
            SUM(isv.quantidade * s.preco_venda) AS receita_mensal
        FROM Itens_Saida isv
        JOIN Produto p ON isv.id_produto = p.id_produto
        JOIN Saida s ON isv.id_saida = s.id_saida
        WHERE s.dt_venda >= DATE_SUB(CURDATE(), INTERVAL 4 MONTH)
          AND s.id_tipo_saida = 1
          AND s.id_status_venda = 1
          AND s.id_plataforma = :plataforma -- Já estava correto
        GROUP BY YEAR(s.dt_venda), MONTH(s.dt_venda)
        ORDER BY YEAR(s.dt_venda), MONTH(s.dt_venda)
    """,
        nativeQuery = true
    )
    fun getReceitaMensal(plataforma: Long): List<Array<Any>>

}
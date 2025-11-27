package laize_tech.back.repository

import laize_tech.back.dto.PlataformaDetalhe
import laize_tech.back.entity.Produto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ProdutoRepository : JpaRepository<Produto, Int> {

    fun findAllByStatusAtivoTrue(): List<Produto>

    @Query(nativeQuery = true, value = """
        SELECT 
            MONTHNAME(cp.dt_compra) AS mes,
            COUNT(cp.id_produto) AS quantidade_produtos,
            SUM(cp.preco_compra * cp.quantidade_produto) AS valor_investido
        FROM Compra_Produto cp
        WHERE MONTH(cp.dt_compra) = MONTH(CURRENT_DATE()) AND YEAR(cp.dt_compra) = YEAR(CURRENT_DATE())
        GROUP BY mes
    """)
    fun getEntradasMesAtual(): List<Array<Any>>

    @Query(value = """
        SELECT DISTINCT
            pp.id_plataforma AS fkPlataforma,
            p.nome_plataforma AS nomePlataforma
        FROM Plataforma_Produto pp
        JOIN Plataforma p ON pp.id_plataforma = p.id_plataforma
        WHERE pp.id_produto = :idProduto
    """, nativeQuery = true)
    fun findPlataformasByProdutoId(idProduto: Int): List<PlataformaDetalhe>

    // NOVO MÉTODO: Inativação Lógica
    @Modifying
    @Query("UPDATE Produto p SET p.statusAtivo = false WHERE p.idProduto = :idProduto")
    fun inativarProduto(@Param("idProduto") idProduto: Int): Int

    @Query(nativeQuery = true, value = """
        SELECT 
            DATE_FORMAT(s.dt_venda, '%Y-%m') AS mes,
            SUM(s.preco_venda) AS receita
        FROM Saida s
        WHERE s.id_plataforma = :plataformaId
        AND s.dt_venda >= DATE_SUB(NOW(), INTERVAL 6 MONTH)
        GROUP BY mes
        ORDER BY mes ASC
    """)
    fun getReceitaMensal(@Param("plataformaId") plataformaId: Long): List<Array<Any>>

    @Query(nativeQuery = true, value = """
        SELECT 
            p.nome_produto, 
            SUM(i.quantidade) AS total_vendido
        FROM Produto p
        JOIN Itens_Saida i ON p.id_produto = i.id_produto
        JOIN Saida s ON i.id_saida = s.id_saida
        WHERE s.id_plataforma = :plataformaId AND s.id_status_venda = 1 -- FINALIZADA
        GROUP BY p.nome_produto
        ORDER BY total_vendido DESC
        LIMIT 5
    """)
    fun getTop5Produtos(@Param("plataformaId") plataformaId: Long): List<Array<Any>>

    @Query(nativeQuery = true, value = """
        SELECT DISTINCT p.nome_produto
        FROM Produto p
        WHERE p.id_produto NOT IN (
            SELECT i.id_produto
            FROM Itens_Saida i
            JOIN Saida s ON i.id_saida = s.id_saida
            WHERE s.dt_venda >= DATE_SUB(NOW(), INTERVAL 60 DAY)
            AND s.id_plataforma = :plataformaId
        )
    """)
    fun getProdutosInativos(@Param("plataformaId") plataformaId: Long): List<String>

    @Query(nativeQuery = true, value = "SELECT SUM(s.preco_venda) FROM Saida s JOIN Itens_Saida i ON s.id_saida = i.id_saida WHERE s.id_plataforma = :plataformaId AND s.id_status_venda = 1")
    fun getTotalVendido(@Param("plataformaId") plataformaId: Long): Double?

    @Query(nativeQuery = true, value = "SELECT SUM(i.quantidade) FROM Itens_Saida i JOIN Saida s ON i.id_saida = s.id_saida WHERE s.id_plataforma = :plataformaId AND s.id_status_venda = 1")
    fun getqtdProdutoVendido(@Param("plataformaId") plataformaId: Long): Int?

    @Query(nativeQuery = true, value = """
    SELECT 
        pl.nome_plataforma, 
        SUM(s.preco_venda) AS total_vendido
    FROM Plataforma pl
    JOIN Saida s ON pl.id_plataforma = s.id_plataforma
    WHERE s.id_status_venda = 1 -- Apenas vendas FINALIZADAS
    GROUP BY pl.nome_plataforma
    ORDER BY total_vendido DESC
""")
    fun getVendasPorPlataforma(): List<Array<Any>>

    @Query(nativeQuery = true, value = """
    SELECT DISTINCT YEAR(s.dt_venda) as ano
    FROM Saida s
    WHERE s.id_status_venda = 1 -- Apenas vendas FINALIZADAS
    ORDER BY ano DESC
""")
    fun getAnosDisponiveis(): List<Int>

    @Query(nativeQuery = true, value = """
    SELECT SUM(s.preco_venda) 
    FROM Saida s 
    WHERE s.id_status_venda = 1
    AND YEAR(s.dt_venda) = :ano
""")
    fun getTotalVendidoPorAno(@Param("ano") ano: Int): Double?

    @Query(nativeQuery = true, value = """
    SELECT SUM(i.quantidade) 
    FROM Itens_Saida i 
    JOIN Saida s ON i.id_saida = s.id_saida 
    WHERE s.id_status_venda = 1
    AND YEAR(s.dt_venda) = :ano
""")
    fun getQtdProdutoVendidoPorAno(@Param("ano") ano: Int): Int?

    @Query(nativeQuery = true, value = """
    SELECT 
        pl.nome_plataforma, 
        SUM(s.id_saida) AS total_vendido
    FROM Plataforma pl
    JOIN Saida s ON pl.id_plataforma = s.id_plataforma
    WHERE s.id_status_venda = 1 -- Apenas vendas FINALIZADAS
    AND YEAR(s.dt_venda) = :ano
    GROUP BY pl.nome_plataforma
    ORDER BY total_vendido DESC
""")
    fun getVendasPorPlataformaPorAno(@Param("ano") ano: Int): List<Array<Any>>

    @Query(nativeQuery = true, value = """
    SELECT 
        p.nome_produto, 
        SUM(i.quantidade) AS total_vendido
    FROM Produto p
    JOIN Itens_Saida i ON p.id_produto = i.id_produto
    JOIN Saida s ON i.id_saida = s.id_saida
    WHERE s.id_status_venda = 1
    AND YEAR(s.dt_venda) = :ano
    GROUP BY p.nome_produto
    ORDER BY total_vendido DESC
    LIMIT 5
""")
    fun getTop5ProdutosPorAno(@Param("ano") ano: Int): List<Array<Any>>

    fun findAllByStatusAtivoTrueAndCategoria_IdCategoriaIn(categoriaIds: List<Int>): List<Produto>

    @Query(nativeQuery = true, value = """
    SELECT 
        DATE_FORMAT(s.dt_venda, '%Y-%m') AS mes,
        SUM(s.preco_venda) AS receita
    FROM Saida s
    WHERE YEAR(s.dt_venda) = :ano
    GROUP BY mes
    ORDER BY mes ASC
""")
    fun getReceitaMensalPorAno(@Param("ano") ano: Int): List<Array<Any>>
}
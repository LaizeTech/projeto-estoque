package laize_tech.back.repository

import laize_tech.back.entity.Produto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ProdutoRepository : JpaRepository<Produto, Int> {

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

    // Receita Mensal (6 meses) - Específico por plataforma (mantido, mas não usado na tela anual)
    @Query(nativeQuery = true, value = """
        SELECT 
            DATE_FORMAT(s.dt_venda, '%Y-%m') AS mes,
            SUM(s.preco_venda * i.quantidade) AS receita
        FROM Saida s
        JOIN Itens_Saida i ON s.id_saida = i.id_saida
        WHERE s.id_plataforma = :plataformaId
        AND s.dt_venda >= DATE_SUB(NOW(), INTERVAL 6 MONTH)
        GROUP BY mes
        ORDER BY mes ASC
    """)
    fun getReceitaMensal(@Param("plataformaId") plataformaId: Long): List<Array<Any>>

    // Top 5 Produtos - Específico por plataforma (mantido, mas ajustado o id_status_venda)
    @Query(nativeQuery = true, value = """
        SELECT 
            p.nome_produto, 
            SUM(i.quantidade) AS total_vendido
        FROM Produto p
        JOIN Itens_Saida i ON p.id_produto = i.id_produto
        JOIN Saida s ON i.id_saida = s.id_saida
        WHERE s.id_plataforma = :plataformaId AND s.id_status_venda = 1 -- id_status_venda corrigido para FINALIZADA
        GROUP BY p.nome_produto
        ORDER BY total_vendido DESC
        LIMIT 5
    """)
    fun getTop5Produtos(@Param("plataformaId") plataformaId: Long): List<Array<Any>>

    // Produtos Inativos (mantido)
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

    // Total Vendido - Específico por plataforma (mantido, mas ajustado o id_status_venda)
    @Query(nativeQuery = true, value = "SELECT SUM(s.preco_venda * i.quantidade) FROM Saida s JOIN Itens_Saida i ON s.id_saida = i.id_saida WHERE s.id_plataforma = :plataformaId AND s.id_status_venda = 1")
    fun getTotalVendido(@Param("plataformaId") plataformaId: Long): Double?

    // Quantidade Vendida - Específico por plataforma (mantido, mas ajustado o id_status_venda)
    @Query(nativeQuery = true, value = "SELECT SUM(i.quantidade) FROM Itens_Saida i JOIN Saida s ON i.id_saida = s.id_saida WHERE s.id_plataforma = :plataformaId AND s.id_status_venda = 1")
    fun getqtdProdutoVendido(@Param("plataformaId") plataformaId: Long): Int?

    // Vendas Por Plataforma (mantido, mas ajustado o id_status_venda)
    @Query(nativeQuery = true, value = """
    SELECT 
        pl.nome_plataforma, 
        SUM(i.quantidade) AS total_vendido
    FROM Plataforma pl
    JOIN Saida s ON pl.id_plataforma = s.id_plataforma
    JOIN Itens_Saida i ON s.id_saida = i.id_saida
    WHERE s.id_status_venda = 1 -- Apenas vendas FINALIZADAS
    GROUP BY pl.nome_plataforma
    ORDER BY total_vendido DESC
""")
    fun getVendasPorPlataforma(): List<Array<Any>>

    // Anos Disponíveis (mantido, mas ajustado o id_status_venda)
    @Query(nativeQuery = true, value = """
    SELECT DISTINCT YEAR(s.dt_venda) as ano
    FROM Saida s
    WHERE s.id_status_venda = 1 -- Apenas vendas FINALIZADAS
    ORDER BY ano DESC
""")
    fun getAnosDisponiveis(): List<Int>

    // Total Vendido Por Ano - Específico por plataforma (mantido, mas ajustado o id_status_venda)
    @Query(nativeQuery = true, value = """
    SELECT SUM(s.preco_venda * i.quantidade) 
    FROM Saida s 
    JOIN Itens_Saida i ON s.id_saida = i.id_saida 
    WHERE s.id_plataforma = :plataformaId 
    AND s.id_status_venda = 1
    AND YEAR(s.dt_venda) = :ano
""")
    fun getTotalVendidoPorAno(@Param("plataformaId") plataformaId: Long, @Param("ano") ano: Int): Double?

    // Quantidade Vendida Por Ano - Específico por plataforma (mantido, mas ajustado o id_status_venda)
    @Query(nativeQuery = true, value = """
    SELECT SUM(i.quantidade) 
    FROM Itens_Saida i 
    JOIN Saida s ON i.id_saida = s.id_saida 
    WHERE s.id_plataforma = :plataformaId 
    AND s.id_status_venda = 1
    AND YEAR(s.dt_venda) = :ano
""")
    fun getQtdProdutoVendidoPorAno(@Param("plataformaId") plataformaId: Long, @Param("ano") ano: Int): Int?

    // Vendas Por Plataforma Por Ano (mantido, mas ajustado o id_status_venda)
    @Query(nativeQuery = true, value = """
    SELECT 
        pl.nome_plataforma, 
        SUM(i.quantidade) AS total_vendido
    FROM Plataforma pl
    JOIN Saida s ON pl.id_plataforma = s.id_plataforma
    JOIN Itens_Saida i ON s.id_saida = i.id_saida
    WHERE s.id_status_venda = 1 -- Apenas vendas FINALIZADAS
    AND YEAR(s.dt_venda) = :ano
    GROUP BY pl.nome_plataforma
    ORDER BY total_vendido DESC
""")
    fun getVendasPorPlataformaPorAno(@Param("ano") ano: Int): List<Array<Any>>

    // Top 5 Produtos Por Ano - Específico por plataforma (mantido, mas ajustado o id_status_venda)
    @Query(nativeQuery = true, value = """
    SELECT 
        p.nome_produto, 
        SUM(i.quantidade) AS total_vendido
    FROM Produto p
    JOIN Itens_Saida i ON p.id_produto = i.id_produto
    JOIN Saida s ON i.id_saida = s.id_saida
    WHERE s.id_plataforma = :plataformaId 
    AND s.id_status_venda = 1
    AND YEAR(s.dt_venda) = :ano
    GROUP BY p.nome_produto
    ORDER BY total_vendido DESC
    LIMIT 5
""")
    fun getTop5ProdutosPorAno(@Param("plataformaId") plataformaId: Long, @Param("ano") ano: Int): List<Array<Any>>

    // Receita Mensal Por Ano - Específico por plataforma (mantido, mas ajustado o id_status_venda)
    @Query(nativeQuery = true, value = """
    SELECT 
        DATE_FORMAT(s.dt_venda, '%Y-%m') AS mes,
        SUM(s.preco_venda * i.quantidade) AS receita
    FROM Saida s
    JOIN Itens_Saida i ON s.id_saida = i.id_saida
    WHERE s.id_plataforma = :plataformaId
    AND s.id_status_venda = 1
    AND YEAR(s.dt_venda) = :ano
    GROUP BY mes
    ORDER BY mes ASC
""")
    fun getReceitaMensalPorAno(@Param("plataformaId") plataformaId: Long, @Param("ano") ano: Int): List<Array<Any>>

    // =================================================================
    // NOVOS ENDPOINTS GERAIS PARA A TELA DE MÉTRICAS ANUAIS (sem plataformaId)
    // =================================================================

    // 1. Receita Total (Receita Anual) - GERAL
    @Query(nativeQuery = true, value = """
        SELECT SUM(s.preco_venda - s.total_desconto) 
        FROM Saida s 
        WHERE s.id_status_venda = 1 -- FINALIZADA
        AND YEAR(s.dt_venda) = :ano
    """)
    fun getTotalVendidoPorAnoGeral(@Param("ano") ano: Int): Double?

    // 2. Top 5 Produtos - GERAL
    @Query(nativeQuery = true, value = """
        SELECT 
            p.nome_produto, 
            SUM(i.quantidade) AS total_vendido
        FROM Produto p
        JOIN Itens_Saida i ON p.id_produto = i.id_produto
        JOIN Saida s ON i.id_saida = s.id_saida
        WHERE s.id_status_venda = 1 -- FINALIZADA
        AND YEAR(s.dt_venda) = :ano
        GROUP BY p.nome_produto
        ORDER BY total_vendido DESC
        LIMIT 5
    """)
    fun getTop5ProdutosPorAnoGeral(@Param("ano") ano: Int): List<Array<Any>>

    // 3. Receita Mensal - GERAL, com formato de mês abreviado para o front-end
    @Query(nativeQuery = true, value = """
        SELECT 
            CASE MONTH(s.dt_venda)
                WHEN 1 THEN 'Jan' WHEN 2 THEN 'Fev' WHEN 3 THEN 'Mar' WHEN 4 THEN 'Abr'
                WHEN 5 THEN 'Mai' WHEN 6 THEN 'Jun' WHEN 7 THEN 'Jul' WHEN 8 THEN 'Ago'
                WHEN 9 THEN 'Set' WHEN 10 THEN 'Out' WHEN 11 THEN 'Nov' WHEN 12 THEN 'Dez'
            END AS mes_abreviado,
            SUM(s.preco_venda - s.total_desconto) AS receita
        FROM Saida s
        JOIN Itens_Saida i ON s.id_saida = i.id_saida
        WHERE s.id_status_venda = 1 -- FINALIZADA
        AND YEAR(s.dt_venda) = :ano
        GROUP BY MONTH(s.dt_venda), mes_abreviado
        ORDER BY MONTH(s.dt_venda) ASC
    """)
    fun getReceitaMensalPorAnoGeral(@Param("ano") ano: Int): List<Array<Any>>
}
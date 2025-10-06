package laize_tech.back.repository

import laize_tech.back.entity.Saida
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.math.BigDecimal

interface SaidaRepository : JpaRepository<Saida, Int> {
    @Query(
        value = """
            SELECT
    SUM(s.preco_venda) 
FROM
    Saida s 
WHERE
    s.dt_venda >= NOW() - INTERVAL 7 DAY 
    AND s.id_status_venda = (
        SELECT
            sv.id_status_venda 
        FROM
            Status_Venda sv 
        WHERE
            sv.nome_status = 'FINALIZADA'
    );
        """,
        nativeQuery = true
    )
    fun calcularRendaBruta7Dias(): BigDecimal?

//Função com CURDATE talvez não funcione já que provavelmente não tem dados no dia, mês ou ano que ele está buscando
    @Query(nativeQuery = true, value = """
        SELECT 
    COUNT(s.id_saida) AS quantidade_venda, 
    p.nome_plataforma AS plataforma
FROM saida s
JOIN status_venda sv ON s.id_status_venda = sv.id_status_venda
JOIN tipo_saida ts ON s.id_tipo_saida = ts.id_tipo_saida
JOIN plataforma p ON s.id_plataforma = p.id_plataforma
WHERE ts.id_tipo_saida = 1
  AND sv.id_status_venda = 1
  AND MONTH(s.dt_venda) = MONTH(CURDATE())
  AND YEAR(s.dt_venda) = YEAR(CURDATE())
GROUP BY p.nome_plataforma;
    """)
    fun quantidadeVendasPorPlataformaNoMesAtual(): List<Array<Any>>

    @Query(nativeQuery = true, value = """
    SELECT COUNT(id_saida) AS quantidade_saida
    FROM Saida
    WHERE dt_venda >= CURDATE() - INTERVAL 2 DAY;
        """)
    fun quantidadeSaidasUltimos3Dias(): Int


    @Query(nativeQuery = true, value = """
        SELECT
            s.id_saida,
            p.nome_produto,
            i.quantidade,
            pl.nome_plataforma,
            s.dt_venda,
            p.status_ativo,
            s.preco_venda,
            cp.fornecedor
        FROM Saida s
        JOIN Itens_Saida i ON s.id_saida = i.id_saida
        JOIN Produto p ON i.id_produto = p.id_produto
        JOIN Plataforma pl ON s.id_plataforma = pl.id_plataforma
        JOIN Status_Venda sv ON s.id_status_venda = sv.id_status_venda
        LEFT JOIN Compra_Produto cp ON cp.id_produto = p.id_produto
        WHERE sv.nome_status = 'FINALIZADA'
        GROUP BY s.id_saida, p.nome_produto, i.quantidade, pl.nome_plataforma, s.dt_venda, p.status_ativo, s.preco_venda, cp.fornecedor
        ORDER BY s.dt_venda DESC
    """)
    fun findSaidasDetalhes(): List<Array<Any>>
}
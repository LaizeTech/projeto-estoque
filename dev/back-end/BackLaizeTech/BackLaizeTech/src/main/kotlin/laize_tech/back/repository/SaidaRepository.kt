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
            pl.nome_plataforma,
            ts.nome_tipo,
            DATE(s.dt_venda) as dt_venda,
            s.preco_venda,
            s.total_desconto,
            ss.nome_status
        FROM Saida s
        JOIN Plataforma pl ON s.id_plataforma = pl.id_plataforma
        JOIN Tipo_Saida ts ON s.id_tipo_saida = ts.id_tipo_saida
        JOIN Status_Venda ss ON s.id_status_venda = ss.id_status_venda
        ORDER BY s.dt_venda;
    """)
    fun findSaidasDetalhes(): List<Array<Any>>
}

package laize_tech.back.repository

import laize_tech.back.entity.Saida
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.math.BigDecimal

interface SaidaRepository : JpaRepository<Saida, Int> {
    @Query(
        value = """
            SELECT
                SUM(s.preco_venda) AS renda_bruta_7_dias
            FROM Saida s
            JOIN StatusVenda sv ON s.id_status_venda = sv.id_status_venda
            WHERE s.dt_venda >= NOW() - INTERVAL 7 DAY
            AND sv.nome_status = 'FINALIZADA';
        """,
        nativeQuery = true
    )
    fun calcularRendaBruta7Dias(): BigDecimal?
}
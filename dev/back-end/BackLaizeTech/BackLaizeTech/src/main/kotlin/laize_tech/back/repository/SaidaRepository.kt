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
            StatusVenda sv 
        WHERE
            sv.nome_status = 'FINALIZADA'
    );
        """,
        nativeQuery = true
    )
    fun calcularRendaBruta7Dias(): BigDecimal?
}
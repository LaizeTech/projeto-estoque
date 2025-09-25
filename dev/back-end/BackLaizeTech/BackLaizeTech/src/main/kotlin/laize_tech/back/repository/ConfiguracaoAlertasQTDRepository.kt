package laize_tech.back.repository

import laize_tech.back.entity.ConfiguracaoAlertasQTD
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ConfiguracaoAlertasQTDRepository : JpaRepository<ConfiguracaoAlertasQTD, Int>{

    @Query(nativeQuery = true, value = """
        SELECT 
            p.nome_produto,
            p.quantidade_produto,
            CASE
                WHEN p.quantidade_produto < c.quantidade_violeta THEN 'Alerta Violeta'
                WHEN p.quantidade_produto < c.quantidade_vermelha THEN 'Alerta Vermelho'
                WHEN p.quantidade_produto < c.quantidade_amarelo THEN 'Alerta Amarelo'
                ELSE 'OK'
            END AS nivel_alerta
            FROM Produto p
            JOIN Configuracao_AlertasQTD c ON 1 = 1
            WHERE p.status_ativo = 1
            AND (
              p.quantidade_produto < c.quantidade_amarelo
              OR p.quantidade_produto < c.quantidade_vermelha
              OR p.quantidade_produto < c.quantidade_violeta
            );""")
    fun findProdutoAlerta(): List<Map<String, Any>>

}
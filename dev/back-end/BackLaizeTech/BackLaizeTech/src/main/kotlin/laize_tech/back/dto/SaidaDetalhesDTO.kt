package laize_tech.back.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.time.LocalDateTime

data class SaidaDetalhesDTO(
    @JsonProperty("id_saida")
    val idSaida: Int,

    @JsonProperty("nome_plataforma")
    val nomePlataforma: String,

    @JsonProperty("nome_tipo")
    val nomeTipo: String,

    @JsonProperty("data_venda")
    val dataVenda: LocalDateTime?,

    @JsonProperty("preco_venda")
    val precoVenda: BigDecimal?,

    @JsonProperty("total_desconto")
    val totalDesconto: BigDecimal?,

    @JsonProperty("nome_status")
    val nomeStatus: String
)
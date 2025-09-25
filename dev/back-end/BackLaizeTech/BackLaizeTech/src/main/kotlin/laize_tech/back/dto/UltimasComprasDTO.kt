package laize_tech.back.dto

import java.math.BigDecimal
import com.fasterxml.jackson.annotation.JsonProperty

data class UltimasComprasDTO(
    @JsonProperty("nome_produto")
    val nomeProduto: String,
    @JsonProperty("preco_compra")
    val precoCompra: BigDecimal,
    @JsonProperty("quantidade_produto")
    val quantidadeProduto: Int
)

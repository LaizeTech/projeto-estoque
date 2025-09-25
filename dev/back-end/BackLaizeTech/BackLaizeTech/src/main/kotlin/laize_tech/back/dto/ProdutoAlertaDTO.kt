package laize_tech.back.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ProdutoAlertaDTO(
    @JsonProperty("nome_produto")
    val nomeProduto: String,
    @JsonProperty("quantidade_produto")
    val quantidadeProduto: Int,
    @JsonProperty("nivel_alerta")
    val nivelAlerta: String
)


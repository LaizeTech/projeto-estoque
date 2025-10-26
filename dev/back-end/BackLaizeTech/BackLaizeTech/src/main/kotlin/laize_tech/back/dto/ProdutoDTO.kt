package laize_tech.back.dto

import java.math.BigDecimal

data class ProdutoDTO(
    val idCategoria: Int,
    val nomeProduto: String,
    val quantidadeProduto: Int,
    val precoProduto: BigDecimal,
    val statusAtivo: Boolean,
    var caminhoImagem: String?
)
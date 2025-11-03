package laize_tech.back.dto

import java.math.BigDecimal

data class ProdutoDTO(
    val idCategoria: Int,
    val nomeProduto: String,
    val quantidadeProduto: Int,
    val precoProduto: BigDecimal,
    val statusAtivo: Boolean,
    var caminhoImagem: String?,

    // Adicionado para a requisição (entrada)
    val plataformas: List<PlataformaRequestDTO>? = null,

    // Adicionado para a resposta (saída)
    val idProduto: Int? = null,
    val plataformasDetalhe: List<PlataformaDetalheDTO>? = null
)
package laize_tech.back.dto

data class ProdutoDTO(
    val idCategoria: Long,
    val nomeProduto: String,
    val quantidadeProduto: Int,
    val statusAtivo: Boolean
)
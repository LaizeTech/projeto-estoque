package laize_tech.back.dto

data class ProdutoDTO(
    val idCategoria: Int,
    val nomeProduto: String,
    val quantidadeProduto: Int,
    val statusAtivo: Boolean,
    var sku: String?
)
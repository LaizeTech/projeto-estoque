package laize_tech.back.dto

data class ProdutoAtualizadoDTO(
    val idCategoria: Int,
    val nomeProduto: String,
    val quantidadeProduto: Int,
    val statusAtivo: Boolean
)
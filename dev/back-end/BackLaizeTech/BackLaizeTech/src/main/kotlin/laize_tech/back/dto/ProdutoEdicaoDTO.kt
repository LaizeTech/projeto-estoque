package laize_tech.back.dto

data class ProdutoEdicaoDTO(
    val nomeProduto: String,
    val idCategoria: Int,
    val plataformas: List<PlataformaAtualizacaoDTO>
)

data class PlataformaAtualizacaoDTO(
    val idPlataforma: Int,
    val quantidade: Int
)
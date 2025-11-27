package laize_tech.back.dto

import laize_tech.back.entity.Produto

// DTO para o retorno detalhado do produto, incluindo plataformas
data class ProdutoDetalheDTO(
    val idProduto: Int,
    val nomeProduto: String,
    val quantidadeProduto: Int,
    val statusAtivo: Int,
    val idCategoria: Int,
    val nomeCategoria: String?,
    val precoProduto: Double?, // Incluindo o preço, caso o back-end o retorne
    val plataformas: List<PlataformaDetalheDTO>
) {
    companion object {
        fun fromProduto(
            produto: Produto,
            plataformas: List<PlataformaDetalheDTO>,
            preco: Double? = null,
            caminhoImagem: String?
        ): ProdutoDetalheDTO {
            return ProdutoDetalheDTO(
                idProduto = produto.idProduto,
                nomeProduto = produto.nomeProduto ?: "",
                quantidadeProduto = produto.quantidadeProduto,
                statusAtivo = if (produto.statusAtivo) 1 else 0,
                idCategoria = produto.categoria?.idCategoria ?: 0,
                nomeCategoria = produto.categoria?.nomeCategoria,
                precoProduto = preco,
                plataformas = plataformas
            )
        }
    }
}

data class PlataformaDetalheDTO(
    val fkPlataforma: Int,
    val nomePlataforma: String
)
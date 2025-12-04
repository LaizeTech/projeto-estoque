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
    val precoProduto: Double?,
    val plataformas: List<PlataformaDetalheDTO>,
    // 1. CAMPO ADICIONADO AO CONSTRUTOR PRINCIPAL
    val caminhoImagem: String?
) {
    companion object {
        fun fromProduto(
            produto: Produto,
            plataformas: List<PlataformaDetalheDTO>,
            preco: Double? = null
            // O parâmetro 'caminhoImagem' FOI REMOVIDO daqui
        ): ProdutoDetalheDTO {
            return ProdutoDetalheDTO(
                idProduto = produto.idProduto,
                nomeProduto = produto.nomeProduto ?: "",
                quantidadeProduto = produto.quantidadeProduto,
                statusAtivo = if (produto.statusAtivo) 1 else 0,
                idCategoria = produto.categoria?.idCategoria ?: 0,
                nomeCategoria = produto.categoria?.nomeCategoria,
                precoProduto = preco,
                plataformas = plataformas,
                // 2. CAMPO POPULADO DIRETAMENTE DA ENTIDADE 'produto'
                caminhoImagem = produto.caminhoImagem
            )
        }
    }
}

data class PlataformaDetalheDTO(
    val fkPlataforma: Int,
    val nomePlataforma: String
)
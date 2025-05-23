package laize_tech.back.dto

import java.time.LocalDate

data class CompraProdutoDTO(
    val fornecedor: String,
    val precoCompra: Double,
    val dtCompra: LocalDate?,
    val quantidadeProduto: Int,
    val idProduto: Int
)
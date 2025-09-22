package laize_tech.back.dto

import java.math.BigDecimal
import java.time.LocalDateTime

data class CompraProdutoDTO(
    val fornecedor: String,
    val precoCompra: BigDecimal,
    val dtCompra: LocalDateTime?,
    val quantidadeProduto: Int,
    val idProduto: Int
)
package laize_tech.back.dto

data class ProdutoCaracteristicaDTO(
    val idProduto: Int,
    val idTipoCaracteristica: Long,
    val idCaracteristica: Long,
    val quantidadeProduto: Int
)

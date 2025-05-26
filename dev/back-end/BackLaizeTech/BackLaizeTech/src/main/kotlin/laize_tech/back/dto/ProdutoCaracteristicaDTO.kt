package laize_tech.back.dto

data class ProdutoCaracteristicaDTO(
    val idProduto: Long,
    val idTipoCaracteristica: Long,
    val idCaracteristica: Long,
    val quantidadeProduto: Int
)

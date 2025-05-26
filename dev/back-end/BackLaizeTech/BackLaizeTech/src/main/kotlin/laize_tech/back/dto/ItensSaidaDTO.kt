package laize_tech.back.dto

data class ItensSaidaDTO(
    val idSaida: Long,
    val idPlataforma: Long,
    val quantidade: Int,
    val idTipoCaracteristica: Long,
    val idCaracteristica: Long,
    val idProdutoCaracteristica: Long,
    val idProduto: Long
)

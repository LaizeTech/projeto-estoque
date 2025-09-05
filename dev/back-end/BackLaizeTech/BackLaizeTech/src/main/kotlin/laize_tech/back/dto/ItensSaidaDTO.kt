package laize_tech.back.dto

data class ItensSaidaDTO(
    val idSaida: Int,
    val idPlataforma: Int,
    val quantidade: Int,
    val idTipoCaracteristica: Int,
    val idCaracteristica: Int,
    val idProdutoCaracteristica: Int,
    val idProduto: Int
)

package laize_tech.back.dto

data class PlataformaProdutoDTO(
val fkPlataforma: Int,
val fkProdutoCaracteristica: Int,
val fkCaracteristica: Int,
val fkTipoCaracteristica: Int,
val fkProduto: Int,
val quantidadeProdutoPlataforma: Int
)

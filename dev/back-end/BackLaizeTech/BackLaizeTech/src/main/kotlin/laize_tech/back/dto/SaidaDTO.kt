package laize_tech.back.dto

data class SaidaDTO(
    val idTipoSaida: Int,
    val idEmpresa: Int,
    val idPlataforma: Int,
    val dtVenda: String?,
    val precoVenda: Double?,
    val totalTaxa: Double?,
    val totalDesconto: Double?,
    val idStatusVenda: Int
)
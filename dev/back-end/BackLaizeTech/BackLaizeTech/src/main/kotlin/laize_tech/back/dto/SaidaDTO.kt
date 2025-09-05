package laize_tech.back.dto

import java.math.BigDecimal

data class SaidaDTO(
    val idTipoSaida: Int,
    val idEmpresa: Int,
    val idPlataforma: Int,
    val numeroPedido: String?,
    val dtVenda: String?,
    val precoVenda: BigDecimal?,
    val totalDesconto: BigDecimal?,
    val idStatusVenda: Int
)
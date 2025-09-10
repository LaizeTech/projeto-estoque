package laize_tech.back.dto

import laize_tech.back.entity.Empresa

data class LoginResponseDTO(
    val idUsuario: Int,
    val nome: String,
    val email: String,
    val acessoFinanceiro: Boolean,
    val statusAtivo: Boolean,
    val empresa: Empresa
)
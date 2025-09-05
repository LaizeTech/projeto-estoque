package laize_tech.back.dto

import laize_tech.back.entity.Empresa

data class LoginResponseDTO(
    val idUsuario: Long,
    val nome: String,
    val email: String,
    val acessoFinanceiro: Boolean,
    val empresa: Empresa
)
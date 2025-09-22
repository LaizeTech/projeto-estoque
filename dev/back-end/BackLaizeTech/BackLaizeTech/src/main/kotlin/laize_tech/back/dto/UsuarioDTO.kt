package laize_tech.back.dto

data class UsuarioDTO(
    val nome: String?,
    val email: String?,
    val senha: String?,
    val acessoFinanceiro: Boolean?,
    val statusAtivo: Boolean?,
    val idEmpresa: Int?
)
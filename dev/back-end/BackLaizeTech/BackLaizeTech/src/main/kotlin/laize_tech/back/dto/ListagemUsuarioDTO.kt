package laize_tech.back.dto

data class ListagemUsuarioDTO(
    val nome: String?,
    val email: String?,
    val acessoFinanceiro: Boolean?,
    val statusAtivo: Boolean?,
    val idEmpresa: Int?
)
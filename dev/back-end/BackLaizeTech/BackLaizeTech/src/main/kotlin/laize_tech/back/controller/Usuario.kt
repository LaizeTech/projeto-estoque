package laize_tech.back.controller

data class Usuario(
    var nome: String? = null,
    var email: String? = null,
    //true significa que tem acesso ao gerenciamento financeiro
    var acesso_financeiro: Boolean = true,
    var senha: String? = null,
    var status: Boolean = false
){

}

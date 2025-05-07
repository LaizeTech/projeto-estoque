package laize_tech.back.controller

data class Usuario(
    var nome: String,
    var email: String,
    var telefone: String? = null,
    //true significa que tem acesso ao gerenciamento financeiro
    var acesso_financeiro: Boolean = true,
    var senha: String
){

}

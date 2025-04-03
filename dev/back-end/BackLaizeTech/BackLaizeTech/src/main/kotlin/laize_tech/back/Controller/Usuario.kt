package laize_tech.back.Controller

data class Usuario(
    var nome: String,
    var email: String,
    var telefone: String? = null,
    //true significa que tem acesso ao gerenciamento financeiro
    var acesso_financeiro: Boolean = true,
    private var senha: String
){

}

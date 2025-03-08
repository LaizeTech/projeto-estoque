package laize_tech.back

data class Usuario(
    var nome: String? = null,
    var email: String? = null,
    var telefone: String? = null,
    var acesso_financeiro: Boolean
    //true significa que tem acesso ao gerenciamento financeiro
){

    var senha: String = ""
        private set

    fun setSenha(novaSenha: String){
        senha = novaSenha
    }

}

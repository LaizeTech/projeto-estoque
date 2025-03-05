package com.laizetech.laizetech_back

import com.fasterxml.jackson.annotation.JsonIgnore

data class Usuario(
    var nome: String,
    var email: String,
    var telefone: String,
    var acesso_financeiro: Boolean
    //true significa que tem acesso ao gerenciamento financeiro
    ){
    
    var senha: String = ""
        private set

}
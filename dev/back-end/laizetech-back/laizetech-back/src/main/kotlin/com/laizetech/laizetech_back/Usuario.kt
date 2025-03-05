package com.laizetech.laizetech_back

import com.fasterxml.jackson.annotation.JsonIgnore

data class Usuario(
    var nome: String,
    var email: String,

    ){
    
    var senha: String = ""
        private set

}
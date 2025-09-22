package laize_tech.back.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Entity
data class Empresa(
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa")
    var idEmpresa:Int?,
    @field:NotBlank @field:Size(min = 2, max = 50) 
    @Column(name = "nome_empresa")
    var nomeEmpresa: String,
    @field:Size(min = 14, max = 14) 
    var cnpj: String
) {
    constructor() : this(null, "", "")
}

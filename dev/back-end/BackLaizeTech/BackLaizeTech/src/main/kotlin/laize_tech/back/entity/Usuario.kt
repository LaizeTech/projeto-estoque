package laize_tech.back.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Entity
data class Usuario(
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?,
    @field:NotBlank @field:Size(min = 2, max = 50) var nome: String,
    @field:Email var email: String,
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @field:Size(min = 8, max = 128) var senha: String,
    var acessoFinanceiro: Boolean = false,
    @ManyToOne @JoinColumn(name = "fkEmpresa") var empresa: Empresa?
) {
    constructor() : this(null, "", "", "", false, null)
}
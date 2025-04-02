package sptech.projeto05.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Entity
data class Plataforma(
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idPlataforma:Int?,
    @field:NotBlank @field:Size(min = 2, max = 20) var nomePlataforma: String,
    var status: Boolean = false,
    @ManyToOne @JoinColumn(name = "fkEmpresa") var empresa: Empresa?
) {
    constructor() : this(null, "", false, null)
}
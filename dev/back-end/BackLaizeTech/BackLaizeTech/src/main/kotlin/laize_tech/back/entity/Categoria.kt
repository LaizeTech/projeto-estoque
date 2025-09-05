package laize_tech.back.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Entity
data class Categoria(
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    var idCategoria: Int?,
    @field:NotBlank @field:Size(min = 2, max = 120) 
    @Column(name = "nome_categoria")
    var nomeCategoria: String
) {
    constructor() : this(null, "")
}
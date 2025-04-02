package sptech.projeto05.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDate

@Entity
data class Categoria(
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idCategoria: Int?,
    @field:NotBlank @field:Size(min = 2, max = 120) var nomeCategoria: String
) {
    constructor() : this(null, "")
}
package laize_tech.back.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Entity
data class TipoSaida(
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_saida")
    var idTipoSaida: Int? = null,
    @field:NotBlank @field:Size(min = 2, max = 120) var nomeTipo: String,
) {
    constructor() : this(null, "")
}
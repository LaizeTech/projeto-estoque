package laize_tech.back.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class TipoCaracteristica (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)

    var idTipoCaracteristica: Int? = null,
    var nomeTipoCaracteristica: String? = null
) {
    constructor() : this(0, "")
}
package laize_tech.back.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*

@Entity
class TipoCaracteristica (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "id_tipo_caracteristica")
    var idTipoCaracteristica: Int? = null,
    
    @Column(name = "nome_tipo_caracteristica")
    var nomeTipoCaracteristica: String? = null
) {
    constructor() : this(null, null)
}
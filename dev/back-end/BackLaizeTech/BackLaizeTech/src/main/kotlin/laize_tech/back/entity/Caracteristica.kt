package laize_tech.back.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*

@Entity
class Caracteristica (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "id_caracteristica")
    var idCaracteristica: Int? = null,
    var nomeCaracteristica: String = "",


    @ManyToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "id_tipo_caracteristica", nullable = false)
    var tipoCaracteristica: TipoCaracteristica? = null


)   {
    constructor() : this(0, "")
}
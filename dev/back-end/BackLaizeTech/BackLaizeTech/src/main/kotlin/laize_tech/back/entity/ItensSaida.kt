package laize_tech.back.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

@Entity
class ItensSaida(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var id_item: Int? = null,

    @ManyToOne
    @JoinColumn(name = "id_saida")
    @NotNull
    var saida: Saida? = null,

    @ManyToOne
    @JoinColumn(name = "id_plataforma")
    @NotNull
    var plataforma: Plataforma? = null,

    @field:Positive
    @NotNull
    var quantidade: Int? = null,

    @ManyToOne
    @JoinColumn(name = "id_tipo_caracteristica")
    var tipoCaracteristica: TipoCaracteristica? = null,

    @ManyToOne
    @JoinColumn(name = "id_caracteristica")
    var caracteristica: Caracteristica? = null,

    @ManyToOne
    @JoinColumn(name = "id_produto_caracteristica")
    var produtoCaracteristica: ProdutoCaracteristica? = null,

    @ManyToOne
    @JoinColumn(name = "id_produto")
    @NotNull
    var produto: Produto? = null
) {
    constructor() : this(null, null, null, null, null, null, null, null)
}
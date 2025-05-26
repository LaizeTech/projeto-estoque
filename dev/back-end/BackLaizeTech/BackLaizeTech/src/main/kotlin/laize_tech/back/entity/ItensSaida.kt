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
    var idItem: Int? = null,

    @ManyToOne
    @JoinColumn(name = "fkSaida")
    @NotNull
    var saida: Saida? = null,

    @ManyToOne
    @JoinColumn(name = "fkPlataforma")
    @NotNull
    var plataforma: Plataforma? = null,

    @field:Positive
    @NotNull
    var quantidade: Int? = null,

    @ManyToOne
    @JoinColumn(name = "fkTipoCaracteristica")
    var tipoCaracteristica: TipoCaracteristica? = null,

    @ManyToOne
    @JoinColumn(name = "fkCaracteristica")
    var caracteristica: Caracteristica? = null,

    @ManyToOne
    @JoinColumn(name = "fkProdutoCaracteristica")
    var produtoCaracteristica: ProdutoCaracteristica? = null,

    @ManyToOne
    @JoinColumn(name = "fkProduto")
    @NotNull
    var produto: Produto? = null
) {
    constructor() : this(null, null, null, null, null, null, null, null)
}
package laize_tech.back.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*

@Entity
@Table(name = "ProdutoCaracteristica")
data class ProdutoCaracteristica (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProdutoCaracteristica")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var idProdutoCaracteristica: Int? = null,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkCaracteristica")
    var caracteristica: Caracteristica? = null,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkTipoCaracteristica")
    var tipoCaracteristica: TipoCaracteristica? = null,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkProduto")
    var produto: Produto? = null,

    @Column(name = "quantidadeProdutoCaracteristica")
    var quantidadeProdutoCaracteristica: Int? = null
) {
    constructor() : this(null, null, null, null, null)
}

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

    @ManyToOne
    @JoinColumn(name = "id_caracteristica")
    var caracteristica: Caracteristica? = null,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_caracteristica")
    var tipoCaracteristica: TipoCaracteristica? = null,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_produto")
    var produto: Produto? = null,

    @Column(name = "quantidade_produto_caracteristica")
    var quantidadeProdutoCaracteristica: Int? = null
) {
    constructor() : this(null, null, null, null, null)
}

package laize_tech.back.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*

@Entity
@Table(name = "PlataformaProduto")
class PlataformaProduto(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPlataformaProduto")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var idPlataformaProduto: Int? = null,

    @ManyToOne
    @JoinColumn(name = "fkPlataforma")
    var plataforma: Plataforma? = null,

    @ManyToOne
    @JoinColumn(name = "fkProdutoCaracteristica")
    var produtoCaracteristica: ProdutoCaracteristica? = null,

    @ManyToOne
    @JoinColumn(name = "fkCaracteristica")
    var caracteristica: Caracteristica? = null,

    @ManyToOne
    @JoinColumn(name = "fkTipoCaracteristica")
    var tipoCaracteristica: TipoCaracteristica? = null,

    @ManyToOne
    @JoinColumn(name = "fkProduto")
    var produto: Produto? = null,

    var quantidadeProdutoPlataforma: Int? = null
) {
    constructor() : this(
        null, null, null, null, null, null, null
    )
}

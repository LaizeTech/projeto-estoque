package laize_tech.back.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*

@Entity
@Table(name = "PlataformaProduto")
class PlataformaProduto(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plataforma_produto")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var idPlataformaProduto: Int? = null,

    @ManyToOne
    @JoinColumn(name = "id_plataforma")
    var plataforma: Plataforma? = null,

    @ManyToOne
    @JoinColumn(name = "id_produtoCaracteristica")
    var produtoCaracteristica: ProdutoCaracteristica? = null,

    @ManyToOne
    @JoinColumn(name = "id_caracteristica")
    var caracteristica: Caracteristica? = null,

    @ManyToOne
    @JoinColumn(name = "id_tipo_caracteristica")
    var tipoCaracteristica: TipoCaracteristica? = null,

    @ManyToOne
    @JoinColumn(name = "id_produto")
    var produto: Produto? = null,

    var quantidadeProdutoPlataforma: Int? = null
) {
    constructor() : this(
        null, null, null, null, null, null, null
    )
}

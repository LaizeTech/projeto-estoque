package laize_tech.back.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*

@Entity
@Table(name = "ItensSaida")
class ItensSaida(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idItem")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var idItem: Int? = null,

    @ManyToOne
    @JoinColumn(name = "fkSaida")
    var saida: Saida? = null,

    @ManyToOne
    @JoinColumn(name = "fkPlataforma")
    var plataforma: Plataforma? = null,

    var quantidade: Int? = null,

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

    @ManyToOne
    @JoinColumn(name = "fkVenda")
    var venda: Vendas? = null,

    @ManyToOne
    @JoinColumn(name = "fkEmpresa")
    var empresa: Empresa? = null,

    var subTotal: Double? = null
) {
    constructor() : this(null, null, null, null, null, null, null, null, null, null, null)
}
package laize_tech.back.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

@Entity
@Table(name = "Itens_Saida")
class ItensSaida(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "id_item")
    var idItem: Int? = null,

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
    @Column(name = "quantidade")
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
    // Construtor padrão exigido pelo JPA
    constructor() : this(null, null, null, null, null, null, null, null)
}
package sptech.projeto05.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*

@Entity
data class PlataformaProduto(
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idEstoque: Int?,
    var quantidadeProduto: Int,
    @ManyToOne @JoinColumn(name = "fkProduto") var produto: Produto?,
    @ManyToOne @JoinColumn(name = "fkPlataforma") var plataforma: Plataforma?,
    @ManyToOne @JoinColumn(name = "fkEmpresa") var empresa: Empresa?,
) {
    constructor() : this(null, 0, Produto(), Plataforma(), Empresa())
}
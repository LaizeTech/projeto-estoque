package laize_tech.back.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import java.util.*

@Entity
data class Produto(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idProduto: Long,

    @ManyToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "fkCategoria")
    @JsonProperty("categoria")
    var categoria: Categoria? = null,

    @JsonProperty("nomeProduto")
    var nomeProduto: String? = null,

    @JsonProperty("dtRegistro")
    val dtRegistro: Date = Date(),

    @JsonProperty("quantidadeProduto")
    var quantidadeProduto: Int,

    @JsonProperty("statusAtivo")
    var statusAtivo: Boolean
) {

    constructor() : this(0, Categoria(), "", Date(), 0, true)

    override fun toString(): String {
        return "Produto(idProduto=$idProduto, categoria=${categoria?.nomeCategoria}, nomeProduto='$nomeProduto', dtRegistro=$dtRegistro, quantidadeProduto=$quantidadeProduto, statusAtivo=$statusAtivo)"
    }
}
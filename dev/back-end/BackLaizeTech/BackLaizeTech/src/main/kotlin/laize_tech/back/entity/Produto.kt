package laize_tech.back.entity

import jakarta.persistence.*
import java.util.*

@Entity
data class Produto(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val idProduto: Long,

    @ManyToOne
    @JoinColumn(name = "fkCategoria")
    val categoria: Categoria,

    val nomeProduto: String,
    val dtRegistro: Date,
    val quantidadeProduto: Int,
    val statusAtivo: Boolean
) {
    constructor() : this(0, Categoria(), "", Date(), 0, true)

    override fun toString(): String {
        return "Produto(idProduto=$idProduto, categoria=${categoria.nomeCategoria}, nomeProduto='$nomeProduto', dtRegistro=$dtRegistro, quantidadeProduto=$quantidadeProduto, statusAtivo=$statusAtivo)"
    }
}

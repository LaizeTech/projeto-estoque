package laize_tech.back.entity


import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class Produto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val idProduto: Long = 0,             // <-- ÚNICO campo para o ID

    @ManyToOne
    @JoinColumn(name = "fk_categoria")
    var categoria: Categoria?,

    var nomeProduto: String?,            // <-- ÚNICO campo para o nome

    var quantidadeProduto: Int,         // <-- ÚNICO campo para a quantidade

    var sku: String?,                   // <-- ÚNICO campo para o SKU

    var statusAtivo: Boolean = true,

    val dtRegistro: LocalDateTime = LocalDateTime.now(),
    val quantidade: Int
){

    override fun toString(): String {
        return "Produto(idProduto=$idProduto, categoria=${categoria?.nomeCategoria}, nomeProduto='$nomeProduto', dtRegistro=$dtRegistro, quantidadeProduto=$quantidadeProduto, statusAtivo=$statusAtivo)"
    }
}
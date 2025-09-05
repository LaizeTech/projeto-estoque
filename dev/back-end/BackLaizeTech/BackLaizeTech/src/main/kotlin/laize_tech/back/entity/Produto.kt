package laize_tech.back.entity


import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class Produto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produto")
    val idProduto: Int = 0,

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    var categoria: Categoria?,

    @Column(name = "nome_produto")
    var nomeProduto: String?,

    @Column(name = "quantidade_produto")
    var quantidadeProduto: Int,

    @Column(name = "status_ativo")
    var statusAtivo: Boolean = true,

    @Column(name = "caminho_imagem")
    var caminhoImagem: String?,

    val dtRegistro: LocalDateTime = LocalDateTime.now()
){

    override fun toString(): String {
        return "Produto(idProduto=$idProduto, categoria=${categoria?.nomeCategoria}, nomeProduto='$nomeProduto', dtRegistro=$dtRegistro, quantidadeProduto=$quantidadeProduto, statusAtivo=$statusAtivo)"
    }
}
package laize_tech.back.Controller

data class Produto(
    var nome: String? = null,
    var categoria: String? = null,
    var valorCompra: Int? = null,
    var ChaveEstoque: Int? = null,
    // true significa que o produto foi vendido, false que está no estoque ainda
    var status: Boolean? = false
)
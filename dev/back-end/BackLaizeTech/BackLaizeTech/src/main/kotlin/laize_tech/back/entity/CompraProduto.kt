package laize_tech.back.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
data class CompraProduto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "id_compra_produto")
    var idCompraProduto: Int? = null,
    @field:NotBlank 
    var fornecedor: String = "",
    @Column(name = "preco_compra", precision = 10, scale = 2)
    var precoCompra: BigDecimal = BigDecimal.ZERO,
    @Column(name = "dt_compra")
    var dtCompra: LocalDateTime? = LocalDateTime.now(),
    @Column(name = "quantidade_produto")
    var quantidadeProduto: Int = 0,

    @ManyToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "id_produto") 
    var produto: Produto?
) {
    constructor() : this(null,"",BigDecimal.ZERO,null,0,null)
}
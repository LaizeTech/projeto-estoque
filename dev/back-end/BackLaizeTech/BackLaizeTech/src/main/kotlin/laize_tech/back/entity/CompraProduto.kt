package laize_tech.back.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

@Entity
data class CompraProduto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var idCompraProduto: Int? = null,
    @field:NotBlank var fornecedor: String = "",
    var precoCompra: Double = 0.0,
    var dtCompra: LocalDate? = LocalDate.now(),
    var quantidadeProduto: Int = 0,

    @ManyToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "fkProduto") var produto: Produto?
) {
    constructor() : this(null,"",0.0,null,0,null)
}
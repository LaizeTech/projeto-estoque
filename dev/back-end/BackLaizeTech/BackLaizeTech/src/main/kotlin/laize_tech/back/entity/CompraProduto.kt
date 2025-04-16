package laize_tech.back.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDate

@Entity

data class CompraProduto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var idCompraProduto: Int? = null,
    @field:NotBlank @field:Size(min = 2, max = 120) var nomeProduto: String = "",
    var precoCompra: Double = 0.0,
    @field:NotBlank var dtCompraProduto: LocalDate = LocalDate.now(),
    var quantidadeProduto: Int = 0,
    @ManyToOne @JoinColumn(name = "fkProduto") var produto: Produto?
){
    constructor() : this(null, "",0.0, LocalDate.now(),0, null)
}


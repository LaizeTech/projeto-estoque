package laize_tech.back.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import laize_tech.back.entity.Categoria
import java.time.LocalDate

@Entity
data class Produto(
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idProduto: Int?,

    @field:NotBlank
    @field:Size(min = 2, max = 120)
    var nomeProduto: String,

    var precoCompra: Double,

    var dataCadastro: LocalDate = LocalDate.now(),

    @ManyToOne
    @JoinColumn(name = "fkCategoria")
    var categoria: Categoria?
) {
    constructor() : this(null, "", 0.0, LocalDate.now(), null)
}

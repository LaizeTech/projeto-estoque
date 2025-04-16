package laize_tech.back.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

@Entity
data class Vendas(
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idVendas: Int?,
    @field:NotBlank var dtVenda: LocalDate = LocalDate.now(),
    var precoVenda: Double?,
    var totalTaxa: Double?,
    var totalDesconto: Double?,
    var statusVendas: Boolean = false,
    @ManyToOne @JoinColumn(name = "fkEmpresa") var empresa: Empresa?,
    @ManyToOne @JoinColumn(name = "fkPlataforma") var plataforma: Plataforma?
) {
    constructor() : this(null, LocalDate.now(), null, null, null, false, null, null)
}



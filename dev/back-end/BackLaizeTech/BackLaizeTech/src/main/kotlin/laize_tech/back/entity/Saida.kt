package laize_tech.back.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import java.time.LocalDate

@Entity
data class Saida(
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idSaida: Int? = null,
    var dtVenda: LocalDate = LocalDate.now(),
    var precoVenda: Double? = null,
    var totalTaxa: Double? = null,
    var totalDesconto: Double? = null
)
package laize_tech.back.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "Saida")
class Saida(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSaida")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var idSaida: Int? = null,

    @ManyToOne
    @JoinColumn(name = "fkTipoSaida")
    var tipoSaida: TipoSaida? = null,

    @ManyToOne
    @JoinColumn(name = "fkEmpresa")
    var empresa: Empresa? = null,

    @ManyToOne
    @JoinColumn(name = "fkPlataforma")
    var plataforma: Plataforma? = null,

    var dtVenda: LocalDate = LocalDate.now(),

    var precoVenda: Double? = null,

    var totalTaxa: Double? = null,

    var totalDesconto: Double? = null,

    @ManyToOne
    @JoinColumn(name = "fkStatusVenda")
    var statusVenda: StatusVenda? = null

) {
    constructor() : this(null, null, null, null, LocalDate.now(), null, null, null, null)
}

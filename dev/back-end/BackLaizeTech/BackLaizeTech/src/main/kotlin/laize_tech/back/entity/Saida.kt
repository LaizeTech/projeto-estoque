package laize_tech.back.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import java.time.LocalDate

@Entity
data class Saida(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_saida")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var idSaida: Int? = null,

    @ManyToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "id_tipo_saida")
    var tipoSaida: TipoSaida? = null,

    @ManyToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "id_empresa")
    var empresa: Empresa? = null,

    @ManyToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "id_plataforma")
    var plataforma: Plataforma? = null,

    var dtVenda: LocalDate = LocalDate.now(),

    var precoVenda: Double? = null,

    var totalTaxa: Double? = null,

    var totalDesconto: Double? = null,

    @ManyToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "id_status_venda")
    var statusVenda: StatusVenda? = null

) {
    constructor() : this(null, null, null, null, LocalDate.now(), null, null, null, null)
}
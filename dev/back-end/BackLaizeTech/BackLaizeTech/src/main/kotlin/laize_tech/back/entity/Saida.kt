package laize_tech.back.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

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

    @Column(name = "numero_pedido")
    var numeroPedido: String? = null,

    @Column(name = "dt_venda")
    var dtVenda: LocalDateTime = LocalDateTime.now(),

    @Column(name = "preco_venda", precision = 10, scale = 2)
    var precoVenda: BigDecimal? = null,

    @Column(name = "total_desconto", precision = 10, scale = 2)
    var totalDesconto: BigDecimal? = null,

    @ManyToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "id_status_venda")
    var statusVenda: StatusVenda? = null

) {
    constructor() : this(null, null, null, null, null, LocalDateTime.now(), null, null, null)
}
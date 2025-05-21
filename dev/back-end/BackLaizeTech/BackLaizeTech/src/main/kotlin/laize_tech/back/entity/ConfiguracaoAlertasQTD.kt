package laize_tech.back.entity

import jakarta.persistence.*

@Entity
@Table(name = "ConfiguracaoAlertasQTD")
class ConfiguracaoAlertasQTD(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val idConfiguracaoAlertasQTD: Long? = null,

    val quantidadeAmarelo: Int,
    val quantidadeVermelha: Int,
    val quantidadeVioleta: Int

) {
    constructor() : this(0, 0, 0, 0)
}
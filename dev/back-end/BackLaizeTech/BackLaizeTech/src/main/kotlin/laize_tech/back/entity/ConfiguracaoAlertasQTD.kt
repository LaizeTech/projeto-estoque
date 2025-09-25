package laize_tech.back.entity

import jakarta.persistence.*

@Entity
@Table(name = "ConfiguracaoAlertasQTD")
data class ConfiguracaoAlertasQTD(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_configuracao_alertas_qtd")
    val idConfiguracaoAlertasQTD: Int = 0,

    @Column(nullable = false)
    val quantidadeAmarelo: Int = 0,

    @Column(nullable = false)
    val quantidadeVermelha: Int = 0,

    @Column(nullable = false)
    val quantidadeVioleta: Int = 0
)
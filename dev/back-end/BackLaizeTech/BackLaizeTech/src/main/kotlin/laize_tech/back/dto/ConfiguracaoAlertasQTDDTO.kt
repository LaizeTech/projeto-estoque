package laize_tech.back.dto

data class ConfiguracaoAlertasQTDDTO(
    val idConfiguracaoAlertasQTD: Long? = null,
    val quantidadeAmarelo: Int,
    val quantidadeVermelha: Int,
    val quantidadeVioleta: Int
    )

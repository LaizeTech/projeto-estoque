package laize_tech.back.ControllerJpa

import laize_tech.back.dto.ConfiguracaoAlertasQTDDTO
import laize_tech.back.entity.ConfiguracaoAlertasQTD
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/configuracao-alertas-qtd")
class ConfiguracaoAlertaQTDJpaController @Autowired constructor(
    private val repository: laize_tech.back.repository.ConfiguracaoAlertasQTDRepository
) {

    @PostMapping
    fun create(@RequestBody dto: ConfiguracaoAlertasQTDDTO): ConfiguracaoAlertasQTDDTO {
        val entity = ConfiguracaoAlertasQTD(
            quantidadeAmarelo = dto.quantidadeAmarelo,
            quantidadeVermelha = dto.quantidadeVermelha,
            quantidadeVioleta = dto.quantidadeVioleta
        )
        val savedEntity = repository.save(entity)
        return ConfiguracaoAlertasQTDDTO(
            idConfiguracaoAlertasQTD = savedEntity.idConfiguracaoAlertasQTD,
            quantidadeAmarelo = savedEntity.quantidadeAmarelo,
            quantidadeVermelha = savedEntity.quantidadeVermelha,
            quantidadeVioleta = savedEntity.quantidadeVioleta
        )
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ConfiguracaoAlertasQTDDTO? {
        val entity = repository.findById(id).orElse(null) ?: return null
        return ConfiguracaoAlertasQTDDTO(
            idConfiguracaoAlertasQTD = entity.idConfiguracaoAlertasQTD,
            quantidadeAmarelo = entity.quantidadeAmarelo,
            quantidadeVermelha = entity.quantidadeVermelha,
            quantidadeVioleta = entity.quantidadeVioleta
        )
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody dto: ConfiguracaoAlertasQTDDTO
    ): ConfiguracaoAlertasQTDDTO? {
        if (!repository.existsById(id)) return null
        val entity = ConfiguracaoAlertasQTD(
            idConfiguracaoAlertasQTD = id,
            quantidadeAmarelo = dto.quantidadeAmarelo,
            quantidadeVermelha = dto.quantidadeVermelha,
            quantidadeVioleta = dto.quantidadeVioleta
        )
        val updatedEntity = repository.save(entity)
        return ConfiguracaoAlertasQTDDTO(
            idConfiguracaoAlertasQTD = updatedEntity.idConfiguracaoAlertasQTD,
            quantidadeAmarelo = updatedEntity.quantidadeAmarelo,
            quantidadeVermelha = updatedEntity.quantidadeVermelha,
            quantidadeVioleta = updatedEntity.quantidadeVioleta
        )
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        if (repository.existsById(id)) {
            repository.deleteById(id)
        }
    }
}
package laize_tech.back.ControllerJpa

import jakarta.validation.Valid
import laize_tech.back.entity.TipoCaracteristica
import laize_tech.back.repository.TipoCaracteristicaRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tipos-caracteristicas")
class TipoCaracteristicaJpaController (
    val tipoCaracteristicaRepository: TipoCaracteristicaRepository
){
    @PostMapping
    fun create(@RequestBody @Valid tipoCaracteristica: TipoCaracteristica): ResponseEntity<TipoCaracteristica> {
        val savedTipoCaracteristica = tipoCaracteristicaRepository.save(tipoCaracteristica)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTipoCaracteristica)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Int, @RequestBody @Valid tipoCaracteristica: TipoCaracteristica): ResponseEntity<TipoCaracteristica> {
        val existingTipoCaracteristica = tipoCaracteristicaRepository.findById(id)
        return if (existingTipoCaracteristica.isPresent) {
            val updatedTipoCaracteristica = existingTipoCaracteristica.get()
            updatedTipoCaracteristica.nomeTipoCaracteristica = tipoCaracteristica.nomeTipoCaracteristica
            tipoCaracteristicaRepository.save(updatedTipoCaracteristica)
            ResponseEntity.ok(updatedTipoCaracteristica)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping
    fun getAll(): ResponseEntity<List<TipoCaracteristica>> {
        val tiposCaracteristicas = tipoCaracteristicaRepository.findAll()
        return if (tiposCaracteristicas.isEmpty()) {
            ResponseEntity.status(HttpStatus.NO_CONTENT).build()
        } else {
            ResponseEntity.ok(tiposCaracteristicas)
        }
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<Void> {
        return if (tipoCaracteristicaRepository.existsById(id)) {
            tipoCaracteristicaRepository.deleteById(id)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
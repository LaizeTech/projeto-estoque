package laize_tech.back.ControllerJpa

import jakarta.validation.Valid
import laize_tech.back.entity.Caracteristica
import laize_tech.back.repository.CaracteristicaRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/caracteristicas")
class CaracteristicaJpaController(val caracteristicaRepository: CaracteristicaRepository) {

    @PostMapping("/adicionar")
    fun create(@RequestBody @Valid caracteristica: Caracteristica): ResponseEntity<Caracteristica> {
        val savedCaracteristica = caracteristicaRepository.save(caracteristica)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCaracteristica)
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Int,
        @RequestBody caracteristicaDetails: Caracteristica): ResponseEntity<Caracteristica> {
        val existingCaracteristica = caracteristicaRepository.findById(id)
        return if (existingCaracteristica.isPresent) {
            val updatedCaracteristica = existingCaracteristica.get().apply {
                nomeTipoCaracteristica = caracteristicaDetails.nomeTipoCaracteristica
                tipoCaracteristica = caracteristicaDetails.tipoCaracteristica
            }
            caracteristicaRepository.save(updatedCaracteristica)
            ResponseEntity.ok(updatedCaracteristica)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping
    fun getAll(): ResponseEntity<List<Caracteristica>> {
        val caracteristicas = caracteristicaRepository.findAll()
        return if (caracteristicas.isEmpty()) {
            ResponseEntity.status(HttpStatus.NO_CONTENT).build()
        } else {
            ResponseEntity.ok(caracteristicas)
        }
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<Void> {
        return if (caracteristicaRepository.existsById(id)) {
            caracteristicaRepository.deleteById(id)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}


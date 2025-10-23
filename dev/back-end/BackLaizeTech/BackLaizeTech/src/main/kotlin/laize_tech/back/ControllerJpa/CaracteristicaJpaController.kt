package laize_tech.back.ControllerJpa

import jakarta.validation.Valid
import laize_tech.back.dto.CaracteristicaDTO
import laize_tech.back.entity.Caracteristica
import laize_tech.back.entity.TipoCaracteristica
import laize_tech.back.exceptions.IdNaoEncontradoException
import laize_tech.back.repository.CaracteristicaRepository
import laize_tech.back.repository.TipoCaracteristicaRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/caracteristicas")
class CaracteristicaJpaController(val caracteristicaRepository: CaracteristicaRepository,
    val tipoCaracteristicaRepository: TipoCaracteristicaRepository
) {

    @PostMapping
    fun post(@RequestBody @Valid novaCaracteristicaDTO: CaracteristicaDTO): ResponseEntity<Caracteristica> {
        val tipoCaracteristica = novaCaracteristicaDTO.idTipoCaracteristica.let {
            tipoCaracteristicaRepository.findById(it).orElseThrow {
                IdNaoEncontradoException("TipoCaracteristica", it)
            }
        }

        val novaCaracteristica = Caracteristica(
            tipoCaracteristica = tipoCaracteristica,
            nomeCaracteristica = novaCaracteristicaDTO.nomeCaracteristica
        )

        return ResponseEntity.status(201).body(caracteristicaRepository.save(novaCaracteristica))
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Int,
        @RequestBody caracteristicaDetails: Caracteristica): ResponseEntity<Caracteristica> {
        val existingCaracteristica = caracteristicaRepository.findById(id)
        return if (existingCaracteristica.isPresent) {
            val updatedCaracteristica = existingCaracteristica.get().apply {
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


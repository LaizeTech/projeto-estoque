package laize_tech.back.ControllerJpa

import jakarta.validation.Valid
import laize_tech.back.entity.TipoSaida
import laize_tech.back.repository.TipoSaidaRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tipos-saidas")
class TipoSaidaJpaController(val repositorio: TipoSaidaRepository) {

    @GetMapping
    fun get(): ResponseEntity<List<TipoSaida>> {
        val tiposSaida = repositorio.findAll()

        return if (tiposSaida.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(tiposSaida)
        }
    }

    @PostMapping
    fun post(@RequestBody @Valid novoTipoSaida: TipoSaida): ResponseEntity<Any> {
        val tipoSaidaSalvo = repositorio.save(novoTipoSaida)
        return ResponseEntity.status(201).body(tipoSaidaSalvo)
    }

    @PutMapping("/{id}")
    fun put(@PathVariable id: Int, @RequestBody @Valid tipoSaidaAtualizado: TipoSaida): ResponseEntity<Any> {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.status(404).body("")
        }

        tipoSaidaAtualizado.idTipoSaida = id
        val tipoSaidaSalvo = repositorio.save(tipoSaidaAtualizado)
        return ResponseEntity.status(200).body(tipoSaidaSalvo)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<String> {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id)
            return ResponseEntity.status(204).build()
        }

        return ResponseEntity.status(404).body("")
    }
}
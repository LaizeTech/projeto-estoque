package laize_tech.back.ControllerJpa

import jakarta.validation.Valid
import laize_tech.back.entity.Saida
import laize_tech.back.repository.SaidaRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/saidas")
class SaidaJpaController(val repositorio: SaidaRepository) {

    @GetMapping
    fun get(): ResponseEntity<List<Saida>> {
        val saidas = repositorio.findAll()

        return if (saidas.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(saidas)
        }
    }

    @PostMapping("/adicionar")
    fun post(@RequestBody @Valid novaSaida: Saida): ResponseEntity<Any> {
        if (novaSaida.precoVenda == null || novaSaida.totalTaxa == null || novaSaida.totalDesconto == null) {
            return ResponseEntity.status(400).body("")
        }

        val saidaSalva = repositorio.save(novaSaida)
        return ResponseEntity.status(201).body(saidaSalva)
    }

    @PutMapping("/{id}")
    fun put(@PathVariable id: Int, @RequestBody saidaAtualizada: Saida): ResponseEntity<Any> {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.status(404).body("Saída com o ID $id não encontrada.")
        }

        if (saidaAtualizada.precoVenda == null || saidaAtualizada.totalTaxa == null || saidaAtualizada.totalDesconto == null) {
            return ResponseEntity.status(400).body("Os campos precoVenda, totalTaxa e totalDesconto não podem ser nulos!")
        }

        saidaAtualizada.idSaida = id
        val saidaSalva = repositorio.save(saidaAtualizada)
        return ResponseEntity.status(200).body(saidaSalva)
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
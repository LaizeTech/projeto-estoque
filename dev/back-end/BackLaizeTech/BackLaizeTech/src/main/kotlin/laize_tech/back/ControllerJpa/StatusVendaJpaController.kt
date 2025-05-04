package laize_tech.back.ControllerJpa

import jakarta.validation.Valid
import laize_tech.back.entity.StatusVenda
import laize_tech.back.repository.StatusVendaRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/status-venda")
class StatusVendaJpaController(val repositorio: StatusVendaRepository) {

    @GetMapping
    fun get(): ResponseEntity<List<StatusVenda>> {
        val statusVendas = repositorio.findAll()

        return if (statusVendas.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(statusVendas)
        }
    }

    @PostMapping("/adicionar")
    fun post(@RequestBody @Valid novoStatusVenda: StatusVenda): ResponseEntity<Any> {
        val statusVendaSalvo = repositorio.save(novoStatusVenda)
        return ResponseEntity.status(201).body(statusVendaSalvo)
    }

    @PutMapping("/{id}")
    fun put(@PathVariable id: Int, @RequestBody @Valid statusVendaAtualizado: StatusVenda): ResponseEntity<Any> {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.status(404).body("")
        }

        statusVendaAtualizado.idStatusVenda = id
        val statusVendaSalvo = repositorio.save(statusVendaAtualizado)
        return ResponseEntity.status(200).body(statusVendaSalvo)
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
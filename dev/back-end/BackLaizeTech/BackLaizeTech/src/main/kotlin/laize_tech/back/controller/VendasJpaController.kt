package laize_tech.back.controller

import jakarta.validation.Valid
import laize_tech.back.entity.Vendas
import laize_tech.back.repository.VendasRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/vendas")
class VendasJpaController (val repositorio: VendasRepository) {

    @GetMapping
    fun get(): ResponseEntity<List<Vendas>> {
        val vendas = repositorio.findAll()

        return if (vendas.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(vendas)
        }
    }


    @PostMapping("/adicionar")
    fun post(@RequestBody @Valid novaVenda: Vendas):
            ResponseEntity<Vendas> {

        val vendas = repositorio.save(novaVenda)
        return ResponseEntity.status(201).body(vendas)
    }

    @PutMapping("/{id}")
    fun put(@PathVariable id: Int, @RequestBody vendaAtualizada: Vendas): ResponseEntity<Vendas> {

        if (!repositorio.existsById(id)) {
            return ResponseEntity.status(404).build()
        }

        vendaAtualizada.idVendas = id
        val vendas = repositorio.save(vendaAtualizada)
        return ResponseEntity.status(200).body(vendas)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<String> {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id)
            return ResponseEntity.status(204).build()
        }
        val mensagem = "Não foi possível deletar a venda com id $id"
        return ResponseEntity.status(404).body(mensagem)
    }
}
package laize_tech.back.ControllerJpa

import jakarta.validation.Valid
import laize_tech.back.entity.ItensVenda
import laize_tech.back.repository.ItensVendaRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/itens")
class ItensVendaJpaController (val repositorio: ItensVendaRepository) {

    @GetMapping
    fun get(): ResponseEntity<List<ItensVenda>> {
        val itens = repositorio.findAll()

        return if (itens.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(itens)
        }
    }

    @GetMapping("/por-venda/{idVenda}")
    fun getByVenda(@PathVariable idVenda: Int): ResponseEntity<List<ItensVenda>> {
        val itens = repositorio.findByVenda_IdVendas(idVenda)

        return if (itens.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(itens)
        }
    }

    @GetMapping("/por-produto/{idProduto}")
    fun getByProduto(@PathVariable idProduto: Int): ResponseEntity<List<ItensVenda>> {
        val itens = repositorio.findByProduto_IdProduto(idProduto)

        return if (itens.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(itens)
        }
    }

    @GetMapping("/por-empresa/{idEmpresa}")
    fun getByEmpresa(@PathVariable idEmpresa: Int): ResponseEntity<List<ItensVenda>> {
        val itens = repositorio.findByEmpresa_IdEmpresa(idEmpresa)

        return if (itens.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(itens)
        }
    }

    @GetMapping("/por-plataforma/{idPlataforma}")
    fun getByPlataforma(@PathVariable idPlataforma: Int): ResponseEntity<List<ItensVenda>> {
        val itens = repositorio.findByPlataforma_IdPlataforma(idPlataforma)

        return if (itens.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(itens)
        }
    }

    @GetMapping("/subtotal/{idVenda}")
    fun getSubTotalPorVenda(@PathVariable idVenda: Int): ResponseEntity<Double?> {
        val subtotal = repositorio.somaSubTotalPorVenda(idVenda)
        return if (subtotal == null) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(subtotal)
        }
    }

    @PostMapping("/adicionar")
    fun post(@RequestBody @Valid novoItem: ItensVenda): ResponseEntity<ItensVenda> {
        val itens = repositorio.save(novoItem)
        return ResponseEntity.status(201).body(itens)
    }

    @PutMapping("/{id}")
    fun put(@PathVariable id: Int, @RequestBody itemAtualizado: ItensVenda): ResponseEntity<ItensVenda> {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.status(404).build()
        }

        itemAtualizado.idItem = id
        val itens = repositorio.save(itemAtualizado)
        return ResponseEntity.status(200).body(itens)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<String> {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id)
            return ResponseEntity.status(204).build()
        }
        val mensagem = "Não foi possível deletar o item com id $id"
        return ResponseEntity.status(404).body(mensagem)
    }
}
package laize_tech.back.controller

import jakarta.validation.Valid
import laize_tech.back.entity.CompraProduto
import laize_tech.back.repository.CompraProdutoRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/compras")
class CompraProdutoJpaController (val repositorio: CompraProdutoRepository) {

    @GetMapping
    fun get(): ResponseEntity<List<CompraProduto>> {
        val compras = repositorio.findAll()

        return if (compras.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(compras)
        }
    }


    @PostMapping("/adicionar")
    fun post(@RequestBody @Valid novaCompra: CompraProduto):
            ResponseEntity<CompraProduto> {

        val compra = repositorio.save(novaCompra)
        return ResponseEntity.status(201).body(compra)
    }

    @PutMapping("/{id}")
    fun put(@PathVariable id: Int, @RequestBody compraAtualizada: CompraProduto): ResponseEntity<CompraProduto> {

        if (!repositorio.existsById(id)) {
            return ResponseEntity.status(404).build()
        }

        compraAtualizada.idCompraProduto = id
        val compras = repositorio.save(compraAtualizada)
        return ResponseEntity.status(200).body(compras)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<String> {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id)
            return ResponseEntity.status(204).build()
        }
        val mensagem = "Não foi possível deletar a compra com id $id"
        return ResponseEntity.status(404).body(mensagem)
    }
}
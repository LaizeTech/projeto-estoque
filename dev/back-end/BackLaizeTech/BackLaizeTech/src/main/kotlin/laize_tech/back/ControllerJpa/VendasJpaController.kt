package laize_tech.back.ControllerJpa

import jakarta.validation.Valid
import laize_tech.back.entity.Vendas
import laize_tech.back.repository.VendasRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/vendas")
class VendasJpaController(val repositorio: VendasRepository) {

    @GetMapping
    fun get(): ResponseEntity<List<Vendas>> {
        val vendas = repositorio.findAll()

        return if (vendas.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(vendas)
        }
    }

    @GetMapping("/concluidas")
    fun getVendasConcluidas(): ResponseEntity<List<Vendas>> {
        val vendas = repositorio.findByStatusVendasTrue()
        return if (vendas.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(vendas)
        }
    }

    @GetMapping("/por-periodo")
    fun getVendasPorPeriodo(@RequestParam dataInicio: String, @RequestParam dataFim: String): ResponseEntity<List<Vendas>> {
        val startDate = LocalDate.parse(dataInicio)
        val endDate = LocalDate.parse(dataFim)
        val vendas = repositorio.findByDtVendaBetween(startDate, endDate)
        return if (vendas.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(vendas)
        }
    }

    @GetMapping("/total-por-periodo")
    fun getTotalVendasPorPeriodo(@RequestParam dataInicio: String, @RequestParam dataFim: String): ResponseEntity<Double?> {
        val startDate = LocalDate.parse(dataInicio)
        val endDate = LocalDate.parse(dataFim)
        val total = repositorio.somaPrecoVendasPorPeriodo(startDate, endDate)
        return if (total == null) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(total)
        }
    }

    @GetMapping("/por-plataforma")
    fun getVendasPorPlataforma(@RequestParam idPlataforma: Int): ResponseEntity<List<Vendas>> {
        val vendas = repositorio.findByPlataformaIdPlataforma(idPlataforma)
        return if (vendas.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(vendas)
        }
    }

    @GetMapping("/por-empresa")
    fun getVendasPorEmpresa(@RequestParam idEmpresa: Int): ResponseEntity<List<Vendas>> {
        val vendas = repositorio.findByEmpresaIdEmpresa(idEmpresa)
        return if (vendas.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(vendas)
        }
    }

    @PostMapping("/adicionar")
    fun post(@RequestBody @Valid novaVenda: Vendas): ResponseEntity<Vendas> {
        val vendaSalva = repositorio.save(novaVenda)
        return ResponseEntity.status(201).body(vendaSalva)
    }

    @PutMapping("/{id}")
    fun put(@PathVariable id: Int, @RequestBody vendaAtualizada: Vendas): ResponseEntity<Vendas> {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.status(404).build()
        }

        vendaAtualizada.idVendas = id
        val vendaSalva = repositorio.save(vendaAtualizada)
        return ResponseEntity.status(200).body(vendaSalva)
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
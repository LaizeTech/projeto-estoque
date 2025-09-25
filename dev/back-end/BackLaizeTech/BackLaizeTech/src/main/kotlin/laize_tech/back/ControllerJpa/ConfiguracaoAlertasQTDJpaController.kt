package laize_tech.back.controller

import jakarta.validation.Valid
import laize_tech.back.dto.ProdutoAlertaDTO
import laize_tech.back.entity.ConfiguracaoAlertasQTD
import laize_tech.back.repository.ConfiguracaoAlertasQTDRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/alertas")
class ConfiguracaoAlertasQTDJpaController(
    val repositorio: ConfiguracaoAlertasQTDRepository
) {

    @GetMapping
    fun listar(): ResponseEntity<List<ConfiguracaoAlertasQTD>> {
        return ResponseEntity.ok(repositorio.findAll())
    }

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Int): ResponseEntity<ConfiguracaoAlertasQTD> {
        val alerta = repositorio.findById(id)
        return if (alerta.isPresent) ResponseEntity.ok(alerta.get())
        else ResponseEntity.notFound().build()
    }

    @PostMapping
    fun criar(@Valid @RequestBody alerta: ConfiguracaoAlertasQTD): ResponseEntity<ConfiguracaoAlertasQTD> {
        val salvo = repositorio.save(alerta)
        return ResponseEntity.ok(salvo)
    }

    @PutMapping("/{id}")
    fun atualizar(
        @PathVariable id: Int,
        @Valid @RequestBody alertaAtualizado: ConfiguracaoAlertasQTD
    ): ResponseEntity<ConfiguracaoAlertasQTD> {
        return repositorio.findById(id).map { existente ->
            val atualizado = existente.copy(
                quantidadeAmarelo = alertaAtualizado.quantidadeAmarelo,
                quantidadeVermelha = alertaAtualizado.quantidadeVermelha,
                quantidadeVioleta = alertaAtualizado.quantidadeVioleta
            )
            ResponseEntity.ok(repositorio.save(atualizado))
        }.orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Int): ResponseEntity<Void> {
        val existente = repositorio.findById(id).orElse(null)
        return if (existente != null) {
            repositorio.delete(existente)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/produto-alerta")
    fun getProdutoAlerta(): ResponseEntity<List<ProdutoAlertaDTO>> {
        val resultadosMap = repositorio.findProdutoAlerta()

        if (resultadosMap.isEmpty()) {
            return ResponseEntity.noContent().build()
        }

        val alertasDTO = resultadosMap.map { row ->
            ProdutoAlertaDTO(
                nomeProduto = row["nome_produto"] as String,
                quantidadeProduto = row["quantidade_produto"] as Int,
                nivelAlerta = row["nivel_alerta"] as String
            )
        }

        return ResponseEntity.ok(alertasDTO)
    }
}
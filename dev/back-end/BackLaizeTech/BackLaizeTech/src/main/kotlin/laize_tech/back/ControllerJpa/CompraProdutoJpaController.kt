package laize_tech.back.ControllerJpa

import jakarta.validation.Valid
import laize_tech.back.dto.CompraProdutoDTO
import laize_tech.back.entity.CompraProduto
import laize_tech.back.entity.Produto
import laize_tech.back.exceptions.IdNaoEncontradoException
import laize_tech.back.repository.CompraProdutoRepository
import laize_tech.back.repository.ProdutoRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/compras")
class CompraProdutoJpaController(
    val compraProdutoRepository: CompraProdutoRepository,
    val produtoRepository: ProdutoRepository
) {

    @GetMapping
    fun get(): ResponseEntity<List<CompraProduto>> {
        val compras = compraProdutoRepository.findAll()
        return if (compras.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(compras)
        }
    }

    @PostMapping
    fun adicionarCompra(@RequestBody @Valid compraProdutoDTO: CompraProdutoDTO): ResponseEntity<CompraProduto> {
//        val produto: Produto = produtoRepository.findById(compraProdutoDTO.idProduto.toLong().toInt()).orElseThrow {
//            IllegalArgumentException("Produto não encontrado com o ID: ${compraProdutoDTO.idProduto}")
//        }

        val produto = compraProdutoDTO.idProduto.let {
            produtoRepository.findById(it).orElseThrow {
                IdNaoEncontradoException("Produto", it)
            }
        }

        val novaCompra = CompraProduto(
            fornecedor = compraProdutoDTO.fornecedor,
            precoCompra = compraProdutoDTO.precoCompra,
            dtCompra = compraProdutoDTO.dtCompra ?: LocalDate.now(),
            quantidadeProduto = compraProdutoDTO.quantidadeProduto,
            produto = produto
        )

        val compraSalva = compraProdutoRepository.save(novaCompra)
        return ResponseEntity.status(201).body(compraSalva)
    }

    @PutMapping("/{id}")
    fun put(@PathVariable id: Int, @RequestBody compraAtualizada: CompraProduto): ResponseEntity<CompraProduto> {
        if (!compraProdutoRepository.existsById(id)) {
            return ResponseEntity.status(404).build()
        }

        val compraExistente = compraAtualizada.idCompraProduto?.let { idCompraProduto ->
            compraProdutoRepository.findById(idCompraProduto).orElseThrow {
                IdNaoEncontradoException("TipoCaracteristica", idCompraProduto)
            }
        } ?: throw IllegalArgumentException("idCompraProduto não pode ser nulo")

        compraExistente.fornecedor = compraAtualizada.fornecedor
        compraExistente.precoCompra = compraAtualizada.precoCompra
        compraExistente.dtCompra = compraAtualizada.dtCompra
        compraExistente.quantidadeProduto = compraAtualizada.quantidadeProduto

        val compraSalva = compraProdutoRepository.save(compraExistente)
        return ResponseEntity.status(200).body(compraSalva)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<String> {
        if (compraProdutoRepository.existsById(id)) {
            compraProdutoRepository.deleteById(id)
            return ResponseEntity.status(204).build()
        }
        val mensagem = "Não foi possível deletar a compra com id $id"
        return ResponseEntity.status(404).body(mensagem)
    }
}
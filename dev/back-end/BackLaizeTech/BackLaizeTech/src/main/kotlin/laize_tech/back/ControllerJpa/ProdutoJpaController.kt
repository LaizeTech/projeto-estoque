package laize_tech.back.ControllerJpa

import jakarta.validation.Valid
import laize_tech.back.dto.ProdutoDTO
import laize_tech.back.entity.Categoria
import laize_tech.back.entity.Produto
import laize_tech.back.repository.CategoriaRepository
import laize_tech.back.repository.ProdutoRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/produtos")
class ProdutoJpaController (
    val produtoRepository: ProdutoRepository,
    val categoriaRepository: CategoriaRepository
) {

    @GetMapping
    fun get(): ResponseEntity<List<Produto>> {
        val produtos = produtoRepository.findAll()
        return if (produtos.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(produtos)
        }
    }

    @PostMapping("/adicionar")
    fun post(@RequestBody @Valid novoProdutoDTO: ProdutoDTO): ResponseEntity<Produto> {
        val categoria: Categoria = categoriaRepository.findById(novoProdutoDTO.idCategoria.toInt()).orElseThrow {
            IllegalArgumentException("Categoria não encontrada com o ID: ${novoProdutoDTO.idCategoria}")
        }

        val novoProduto = Produto(
            idProduto = 0,
            categoria = categoria,
            nomeProduto = novoProdutoDTO.nomeProduto,
            quantidadeProduto = novoProdutoDTO.quantidadeProduto,
            statusAtivo = novoProdutoDTO.statusAtivo
        )

        val produtoSalvo = produtoRepository.save(novoProduto)
        return ResponseEntity.status(201).body(produtoSalvo)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<String> {
        val idInt = id.toInt()
        if (produtoRepository.existsById(idInt)) {
            produtoRepository.deleteById(idInt)
            return ResponseEntity.status(204).build()
        }
        val mensagem = "Não foi possível deletar o produto com id $id"
        return ResponseEntity.status(404).body(mensagem)
    }

    @PutMapping("/{id}")
    fun put(@PathVariable id: Int, @RequestBody produtoAtualizadoDTO: ProdutoDTO): ResponseEntity<Any> {
        val produtoExistente = produtoRepository.findById(id.toInt()).orElse(null)
            ?: return ResponseEntity.status(404).body("Produto não encontrado")

        if (produtoAtualizadoDTO.nomeProduto.isBlank()) {
            return ResponseEntity.status(400).body("Nome do produto não pode estar em branco")
        }

        val categoria: Categoria = categoriaRepository.findById(produtoAtualizadoDTO.idCategoria.toInt()).orElseThrow {
            IllegalArgumentException("Categoria não encontrada com o ID: ${produtoAtualizadoDTO.idCategoria}")
        }

        produtoExistente.categoria = categoria
        produtoExistente.nomeProduto = produtoAtualizadoDTO.nomeProduto
        produtoExistente.quantidadeProduto = produtoAtualizadoDTO.quantidadeProduto
        produtoExistente.statusAtivo = produtoAtualizadoDTO.statusAtivo

        val produtoSalvo = produtoRepository.save(produtoExistente)
        return ResponseEntity.status(200).body(produtoSalvo)
    }
}
package laize_tech.back.ControllerJpa

import jakarta.validation.Valid
import laize_tech.back.dto.*
import laize_tech.back.entity.Categoria
import laize_tech.back.entity.Produto
import laize_tech.back.exceptions.IdNaoEncontradoException
import laize_tech.back.repository.CategoriaRepository
import laize_tech.back.repository.ProdutoRepository
import laize_tech.back.service.FileUploadService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/produtos")
class ProdutoJpaController(
    val produtoRepository: ProdutoRepository,
    val categoriaRepository: CategoriaRepository,
    private val uploadService: FileUploadService,
) {

    @GetMapping("/entradas/mes-atual")
    fun getEntradasMesAtual(): ResponseEntity<List<Array<Any>>> {
        val entradas = produtoRepository.getEntradasMesAtual()
        return if (entradas.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(entradas)
        }
    }

    @GetMapping("/vendas/meses")
    fun getMesAtual(@RequestParam plataforma: Long): ResponseEntity<List<Array<Any>>> {
        val meses = produtoRepository.getMesAtual(plataforma)
        return if (meses.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(meses)
        }
    }

    @GetMapping("/vendas/quantidade")
    fun getQtdProdutoVendido(@RequestParam plataforma: Long): ResponseEntity<List<Array<Any>>> {
        val quantidade = produtoRepository.getqtdProdutoVendido(plataforma)
        return if (quantidade.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(quantidade)
        }
    }

    @GetMapping("/vendas/total")
    fun getTotalVendido(@RequestParam plataforma: Long): ResponseEntity<List<Array<Any>>> {
        val totalVendido = produtoRepository.getTotalVendido(plataforma)
        return if (totalVendido.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(totalVendido)
        }
    }

    @GetMapping("/top5")
    fun getTop5Produtos(@RequestParam plataforma: Long): ResponseEntity<List<Array<Any>>> {
        val top5 = produtoRepository.getTop5Produtos(plataforma)
        return if (top5.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(top5)
        }
    }

    @GetMapping("/inativos")
    fun getProdutosInativos(@RequestParam plataforma: Long): ResponseEntity<List<String>> {
        val inativos = produtoRepository.getProdutosInativos(plataforma)
        return if (inativos.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(inativos)
        }
    }

    @GetMapping("/receita/mensal")
    fun getReceitaMensal(@RequestParam plataforma: Long): ResponseEntity<List<Array<Any>>> {
        val receita = produtoRepository.getReceitaMensal(plataforma)
        return if (receita.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(receita)
        }
    }

    @GetMapping
    fun get(): ResponseEntity<List<Produto>> {
        val produtos = produtoRepository.findAll()
        return if (produtos.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(produtos)
        }
    }

    @PostMapping
    fun post(@RequestBody @Valid novoProdutoDTO: ProdutoDTO): ResponseEntity<Produto> {

        val categoria = categoriaRepository.findById(novoProdutoDTO.idCategoria.toInt())
            .orElseThrow { IdNaoEncontradoException("Categoria", novoProdutoDTO.idCategoria.toInt()) }

        val novoProduto = Produto(
            idProduto = 0,
            categoria = categoria,
            nomeProduto = novoProdutoDTO.nomeProduto,

            quantidadeProduto = novoProdutoDTO.quantidadeProduto,

//            sku = novoProdutoDTO.sku,
            statusAtivo = novoProdutoDTO.statusAtivo,
            dtRegistro = TODO(),
//            quantidade = novoProdutoDTO.quantidadeProduto,
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
        val produtoExistente = produtoRepository.findById(id).orElseThrow {
            IdNaoEncontradoException("Produto", id)
        }

        if (produtoAtualizadoDTO.nomeProduto.isBlank()) {
            return ResponseEntity.status(400).body("Nome do produto não pode estar em branco")
        }

        val categoria: Categoria = categoriaRepository.findById(produtoAtualizadoDTO.idCategoria.toInt()).orElseThrow {
            IdNaoEncontradoException("Categoria", produtoAtualizadoDTO.idCategoria.toInt())
        }

        produtoExistente.categoria = categoria
        produtoExistente.nomeProduto = produtoAtualizadoDTO.nomeProduto
        produtoExistente.quantidadeProduto = produtoAtualizadoDTO.quantidadeProduto
        produtoExistente.statusAtivo = produtoAtualizadoDTO.statusAtivo

        val produtoSalvo = produtoRepository.save(produtoExistente)
        return ResponseEntity.status(200).body(produtoSalvo)
    }

    @PostMapping("/upload/csv")
    fun uploadCsv(@RequestParam("file") file: MultipartFile): ResponseEntity<List<Produto>> {
        if (file.isEmpty) {
            return ResponseEntity.badRequest().build()
        }

        if (!file.contentType?.equals("text/csv")!!) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build()
        }

        val produtosSalvos = uploadService.uploadFileAndProcess(file)
        return ResponseEntity.status(HttpStatus.CREATED).body(produtosSalvos)
    }
}
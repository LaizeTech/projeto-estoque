package laize_tech.back.ControllerJpa

import jakarta.validation.Valid
import laize_tech.back.dto.*
import laize_tech.back.entity.Categoria
import laize_tech.back.entity       .Produto
import laize_tech.back.exceptions.IdNaoEncontradoException
import laize_tech.back.repository.CategoriaRepository
// CORREÇÃO: O import do repositório precisa do caminho completo.
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

    // CORREÇÃO: Este endpoint chamava "getMesAtual", que não existia.
    // Alterei para chamar "getReceitaMensal", que parece ser a intenção original.
    @GetMapping("/vendas/meses")
    fun getVendasMeses(@RequestParam plataforma: Long): ResponseEntity<List<Array<Any>>> {
        val meses = produtoRepository.getReceitaMensal(plataforma)
        return if (meses.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(meses)
        }
    }

    // CORREÇÃO: Lógica ajustada para o novo tipo de retorno (Int?).
    @GetMapping("/vendas/quantidade")
    fun getQtdProdutoVendido(@RequestParam plataforma: Long): ResponseEntity<Int> {
        val quantidade = produtoRepository.getqtdProdutoVendido(plataforma)
        return if (quantidade != null) {
            ResponseEntity.status(200).body(quantidade)
        } else {
            ResponseEntity.status(204).build()
        }
    }

    @GetMapping("/vendas/por-plataforma")
    fun getVendasPorPlataforma(): ResponseEntity<List<Array<Any>>> {
        val vendas = produtoRepository.getVendasPorPlataforma()
        return if (vendas.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(vendas)
        }
    }

    // CORREÇÃO: Lógica ajustada para o novo tipo de retorno (Double?).
    @GetMapping("/vendas/total")
    fun getTotalVendido(@RequestParam plataforma: Long): ResponseEntity<Double> {
        val totalVendido = produtoRepository.getTotalVendido(plataforma)
        return if (totalVendido != null) {
            ResponseEntity.status(200).body(totalVendido)
        } else {
            ResponseEntity.status(204).build()
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
            statusAtivo = novoProdutoDTO.statusAtivo,
            // CORREÇÃO: Removido o "dtRegistro = TODO()".
            // Sua entidade Produto já atribui a data atual por padrão.
        )
        val produtoSalvo = produtoRepository.save(novoProduto)
        return ResponseEntity.status(201).body(produtoSalvo)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> { // Retornar Void é mais comum para 204
        val idInt = id.toInt()
        if (produtoRepository.existsById(idInt)) {
            produtoRepository.deleteById(idInt)
            return ResponseEntity.status(204).build()
        }
        // Se não existe, lançar a exceção é uma boa prática
        throw IdNaoEncontradoException("Produto", idInt)
    }

    @PutMapping("/{id}")
    fun put(@PathVariable id: Int, @RequestBody produtoAtualizadoDTO: ProdutoDTO): ResponseEntity<Produto> {
        val produtoExistente = produtoRepository.findById(id).orElseThrow {
            IdNaoEncontradoException("Produto", id)
        }

        if (produtoAtualizadoDTO.nomeProduto.isBlank()) {
            // Lançar uma exceção é melhor do que retornar ResponseEntity<Any>
            throw IllegalArgumentException("Nome do produto não pode estar em branco")
        }

        val categoria: Categoria = categoriaRepository.findById(produtoAtualizadoDTO.idCategoria.toInt()).orElseThrow {
            IdNaoEncontradoException("Categoria", produtoAtualizadoDTO.idCategoria.toInt())
        }

        produtoExistente.apply {
            this.categoria = categoria
            this.nomeProduto = produtoAtualizadoDTO.nomeProduto
            this.quantidadeProduto = produtoAtualizadoDTO.quantidadeProduto
            this.statusAtivo = produtoAtualizadoDTO.statusAtivo
        }

        val produtoSalvo = produtoRepository.save(produtoExistente)
        return ResponseEntity.status(200).body(produtoSalvo)
    }

    @PostMapping("/upload/csv")
    fun uploadCsv(@RequestParam("file") file: MultipartFile): ResponseEntity<List<Produto>> {
        if (file.isEmpty) {
            return ResponseEntity.badRequest().build()
        }

        if (file.contentType == null || !file.contentType!!.equals("text/csv", ignoreCase = true)) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build()
        }

        val produtosSalvos = uploadService.uploadFileAndProcess(file)
        return ResponseEntity.status(HttpStatus.CREATED).body(produtosSalvos)
    }
}
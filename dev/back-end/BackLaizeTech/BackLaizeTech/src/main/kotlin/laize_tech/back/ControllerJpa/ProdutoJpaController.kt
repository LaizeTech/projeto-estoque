package laize_tech.back.ControllerJpa

import jakarta.validation.Valid
import laize_tech.back.dto.*
import laize_tech.back.entity.Categoria
import laize_tech.back.entity       .Produto
import laize_tech.back.exceptions.IdNaoEncontradoException
import laize_tech.back.repository.CategoriaRepository
import laize_tech.back.repository.ProdutoRepository
import laize_tech.back.service.FileUploadService
import laize_tech.back.service.ProdutoService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/produtos")
@CrossOrigin("http://localhost:3000")
class ProdutoJpaController(
    val produtoRepository: ProdutoRepository,
    val categoriaRepository: CategoriaRepository,
    private val uploadService: FileUploadService,
    private val produtoService: ProdutoService,
) {

    @PostMapping(value = ["/cadastro"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun cadastrarProduto(
        @RequestPart("produto") produtoDTO: ProdutoDTO,
        @RequestPart("imagem", required = false) imagem: MultipartFile?
    ): ResponseEntity<ProdutoDTO> {
        val novoProduto = produtoService.cadastrarProduto(produtoDTO, imagem)
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto)
    }

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
    fun getVendasMeses(@RequestParam plataforma: Long): ResponseEntity<List<Array<Any>>> {
        val meses = produtoRepository.getReceitaMensal(plataforma)
        return if (meses.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(meses)
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
        )
        val produtoSalvo = produtoRepository.save(novoProduto)
        return ResponseEntity.status(201).body(produtoSalvo)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> { 
        val idInt = id.toInt()
        if (produtoRepository.existsById(idInt)) {
            produtoRepository.deleteById(idInt)
            return ResponseEntity.status(204).build()
        }
        throw IdNaoEncontradoException("Produto", idInt)
    }

    @PutMapping("/{id}")
    fun put(@PathVariable id: Int, @RequestBody produtoAtualizadoDTO: ProdutoDTO): ResponseEntity<Produto> {
        val produtoExistente = produtoRepository.findById(id).orElseThrow {
            IdNaoEncontradoException("Produto", id)
        }

        if (produtoAtualizadoDTO.nomeProduto.isBlank()) {
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

    @GetMapping("/anos-disponiveis")
    fun getAnosDisponiveis(): ResponseEntity<List<Int>> {
        val anos = produtoRepository.getAnosDisponiveis()
        return if (anos.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(anos)
        }
    }

    @GetMapping("/vendas/total")
    fun getTotalVendido(
        @RequestParam plataforma: Long,
        @RequestParam(required = false) ano: Int?
    ): ResponseEntity<Double> {
        val totalVendido = if (ano != null) {
            produtoRepository.getTotalVendidoPorAno(plataforma, ano)
        } else {
            produtoRepository.getTotalVendido(plataforma)
        }

        return if (totalVendido != null) {
            ResponseEntity.status(200).body(totalVendido)
        } else {
            ResponseEntity.status(204).build()
        }
    }

    @GetMapping("/vendas/quantidade")
    fun getQtdProdutoVendido(
        @RequestParam plataforma: Long,
        @RequestParam(required = false) ano: Int?
    ): ResponseEntity<Int> {
        val quantidade = if (ano != null) {
            produtoRepository.getQtdProdutoVendidoPorAno(plataforma, ano)
        } else {
            produtoRepository.getqtdProdutoVendido(plataforma)
        }

        return if (quantidade != null) {
            ResponseEntity.status(200).body(quantidade)
        } else {
            ResponseEntity.status(204).build()
        }
    }

    @GetMapping("/vendas/por-plataforma")
    fun getVendasPorPlataforma(@RequestParam(required = false) ano: Int?): ResponseEntity<List<Array<Any>>> {
        val vendas = if (ano != null) {
            produtoRepository.getVendasPorPlataformaPorAno(ano)
        } else {
            produtoRepository.getVendasPorPlataforma()
        }

        return if (vendas.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(vendas)
        }
    }

    @GetMapping("/top5")
    fun getTop5Produtos(
        @RequestParam plataforma: Long,
        @RequestParam(required = false) ano: Int?
    ): ResponseEntity<List<Array<Any>>> {
        val top5 = if (ano != null) {
            produtoRepository.getTop5ProdutosPorAno(plataforma, ano)
        } else {
            produtoRepository.getTop5Produtos(plataforma)
        }

        return if (top5.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(top5)
        }
    }

    @GetMapping("/receita/mensal")
    fun getReceitaMensal(
        @RequestParam plataforma: Long,
        @RequestParam(required = false) ano: Int?
    ): ResponseEntity<List<Array<Any>>> {
        val receita = if (ano != null) {
            produtoRepository.getReceitaMensalPorAno(plataforma, ano)
        } else {
            produtoRepository.getReceitaMensal(plataforma)
        }

        return if (receita.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(receita)
        }
    }
}

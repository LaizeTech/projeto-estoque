package laize_tech.back.service

import laize_tech.back.dto.ProdutoDTO
import laize_tech.back.dto.AdicionarQtdRequestDTO
import laize_tech.back.dto.ProdutoEdicaoDTO
import laize_tech.back.dto.AdicionarPlataformaDTO
import laize_tech.back.entity.Produto
import laize_tech.back.entity.PlataformaProduto
import laize_tech.back.repository.CategoriaRepository
import laize_tech.back.repository.ProdutoRepository
import laize_tech.back.repository.PlataformaProdutoRepository
import laize_tech.back.repository.PlataformaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class ProdutoService(
    private val produtoRepository: ProdutoRepository,
    private val categoriaRepository: CategoriaRepository,
    private val imageStorageService: ImageStorageService,
    private val plataformaProdutoRepository: PlataformaProdutoRepository, // INJEÇÃO CORRIGIDA
    private val plataformaRepository: PlataformaRepository // INJEÇÃO NECESSÁRIA
) {

    @Transactional
    fun cadastrarProduto(produtoDTO: ProdutoDTO, imagem: MultipartFile?): ProdutoDTO {
        val caminhoImagem = imagem?.let { imageStorageService.salvarImagem(it) }

        val categoria = categoriaRepository.findById(produtoDTO.idCategoria)
            .orElseThrow { IllegalArgumentException("Categoria não encontrada para o ID: ${produtoDTO.idCategoria}") }

        val produto = Produto(
            categoria = categoria,
            nomeProduto = produtoDTO.nomeProduto,
            quantidadeProduto = produtoDTO.quantidadeProduto,
            statusAtivo = produtoDTO.statusAtivo,
            caminhoImagem = caminhoImagem ?: "",
            dtRegistro = LocalDateTime.now()
        )

        val salvo = produtoRepository.save(produto)

        return ProdutoDTO(
            idCategoria = salvo.categoria?.idCategoria ?: 0,
            nomeProduto = salvo.nomeProduto ?: "",
            quantidadeProduto = salvo.quantidadeProduto,
            precoProduto = produtoDTO.precoProduto,
            statusAtivo = salvo.statusAtivo,
            caminhoImagem = salvo.caminhoImagem
        )
    }

    @Transactional
    fun atualizarProduto(id: Int, produtoDTO: ProdutoEdicaoDTO, imagem: MultipartFile?): ProdutoDTO {
        val produto = produtoRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Produto não encontrado para o ID: $id") }

        val categoria = categoriaRepository.findById(produtoDTO.idCategoria)
            .orElseThrow { IllegalArgumentException("Categoria não encontrada para o ID: ${produtoDTO.idCategoria}") }

        produto.nomeProduto = produtoDTO.nomeProduto
        produto.categoria = categoria

        if (imagem != null && !imagem.isEmpty) {
            val caminhoImagem = imageStorageService.salvarImagem(imagem)
            produto.caminhoImagem = caminhoImagem
        }

        val produtoSalvo = produtoRepository.save(produto)

        return ProdutoDTO(
            idCategoria = produtoSalvo.categoria?.idCategoria ?: 0,
            nomeProduto = produtoSalvo.nomeProduto ?: "",
            quantidadeProduto = produtoSalvo.quantidadeProduto,
            precoProduto = BigDecimal.valueOf(0.0),
            statusAtivo = produtoSalvo.statusAtivo,
            caminhoImagem = produtoSalvo.caminhoImagem
        )
    }

    @Transactional
    fun adicionarQuantidadePorPlataforma(dto: AdicionarQtdRequestDTO): Produto {
        val produto = produtoRepository.findById(dto.fkProduto)
            .orElseThrow { IllegalArgumentException("Produto não encontrado para o ID: ${dto.fkProduto}") }

        var totalAdicionado = 0

        dto.plataformas.forEach { plataformaQtd ->
            val plataformaProduto = plataformaProdutoRepository
                .findByProdutoIdProdutoAndPlataformaIdPlataforma(dto.fkProduto, plataformaQtd.fkPlataforma)
                ?: throw IllegalArgumentException("Relação Produto-Plataforma não encontrada")

            plataformaProduto.quantidadeProdutoPlataforma = (plataformaProduto.quantidadeProdutoPlataforma ?: 0) + plataformaQtd.quantidadeAdicional
            plataformaProdutoRepository.save(plataformaProduto)

            totalAdicionado += plataformaQtd.quantidadeAdicional
        }

        produto.quantidadeProduto += totalAdicionado

        return produtoRepository.save(produto)
    }

    @Transactional
    fun adicionarNovaPlataforma(idProduto: Int, dto: AdicionarPlataformaDTO) {
        val produto = produtoRepository.findById(idProduto)
            .orElseThrow { IllegalArgumentException("Produto não encontrado para o ID: $idProduto") }

        val plataforma = plataformaRepository.findById(dto.fkPlataforma)
            .orElseThrow { IllegalArgumentException("Plataforma não encontrada para o ID: ${dto.fkPlataforma}") }

        val relacaoExistente = plataformaProdutoRepository.findByProdutoIdProdutoAndPlataformaIdPlataforma(idProduto, dto.fkPlataforma)
        if (relacaoExistente != null) {
            throw IllegalArgumentException("O produto já está associado a esta plataforma.")
        }

        // CORREÇÃO: Instanciando PlataformaProduto corretamente
        val novaRelacao = PlataformaProduto(
            plataforma = plataforma,
            produto = produto,
            quantidadeProdutoPlataforma = dto.quantidadeInicial
        )

        plataformaProdutoRepository.save(novaRelacao)

        produto.quantidadeProduto += dto.quantidadeInicial
        produtoRepository.save(produto)
    }

    @Transactional
    fun removerPlataforma(idProduto: Int, idPlataforma: Int) {
        val relacao = plataformaProdutoRepository.findByProdutoIdProdutoAndPlataformaIdPlataforma(idProduto, idPlataforma)
            ?: throw IllegalArgumentException("Relação Produto-Plataforma não encontrada.")

        val produto = produtoRepository.findById(idProduto)
            .orElseThrow { IllegalArgumentException("Produto não encontrado para o ID: $idProduto") }

        produto.quantidadeProduto -= relacao.quantidadeProdutoPlataforma ?: 0

        plataformaProdutoRepository.delete(relacao)

        produtoRepository.save(produto)
    }

    @Transactional
    fun inativarProduto(id: Int): Boolean {
        val produto = produtoRepository.findById(id).orElse(null) ?: return false

        produto.statusAtivo = false
        produtoRepository.save(produto)
        return true
    }
}
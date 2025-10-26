package laize_tech.back.service

import laize_tech.back.dto.ProdutoDTO
import laize_tech.back.entity.Produto
import laize_tech.back.repository.CategoriaRepository
import laize_tech.back.repository.ProdutoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

@Service
class ProdutoService(
    private val produtoRepository: ProdutoRepository,
    private val categoriaRepository: CategoriaRepository,
    private val imageStorageService: ImageStorageService
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
            precoProduto = produtoDTO.precoProduto, // ✅ adicionado aqui
            statusAtivo = salvo.statusAtivo,
            caminhoImagem = salvo.caminhoImagem
        )
    }
}
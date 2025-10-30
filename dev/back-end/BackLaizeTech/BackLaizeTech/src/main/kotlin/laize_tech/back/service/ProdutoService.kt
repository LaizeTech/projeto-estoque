package laize_tech.back.service

import laize_tech.back.dto.ProdutoDTO
import laize_tech.back.dto.AdicionarQtdRequestDTO
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
        // ... (código existente)
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
    fun adicionarQuantidadePorPlataforma(dto: AdicionarQtdRequestDTO): Produto {
        // ... (código existente para adicionarQuantidadePorPlataforma)
        val produto = produtoRepository.findById(dto.fkProduto)
            .orElseThrow { IllegalArgumentException("Produto não encontrado para o ID: ${dto.fkProduto}") }

        var totalAdicionado = 0

        dto.plataformas.forEach { plataformaQtd ->
            // Lógica para atualizar Plataforma_Produto
            // NOTE: Você deve implementar a busca e atualização na tabela Plataforma_Produto aqui.
            // Exemplo:
            // val plataformaProduto = plataformaProdutoRepository.findByFkProdutoAndFkPlataforma(dto.fkProduto, plataformaQtd.fkPlataforma)
            // plataformaProduto.quantidadeProdutoPlataforma += plataformaQtd.quantidadeAdicional
            // plataformaProdutoRepository.save(plataformaProduto)

            totalAdicionado += plataformaQtd.quantidadeAdicional
        }

        // Atualiza a quantidade total do produto principal
        produto.quantidadeProduto += totalAdicionado

        return produtoRepository.save(produto)
    }

    @Transactional
    fun inativarProduto(id: Int): Boolean {
        val produto = produtoRepository.findById(id).orElse(null) ?: return false

        produto.statusAtivo = false // Define o status como inativo
        produtoRepository.save(produto) // Salva a alteração no banco de dados
        return true
    }
}
package laize_tech.back.service

import laize_tech.back.entity.Produto
import laize_tech.back.repository.ProdutoRepository
import com.opencsv.CSVReaderBuilder
import laize_tech.back.entity.Categoria
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.InputStreamReader
import java.time.Instant

@Service
@ConditionalOnProperty(name = ["aws.s3.enabled"], havingValue = "false", matchIfMissing = true)
class MockUploadService(
    private val produtoRepository: ProdutoRepository
) : FileUploadService {

    override fun uploadFileAndProcess(file: MultipartFile): List<Produto> {
        println("🔧 Modo de desenvolvimento: Processando arquivo sem S3 - ${file.originalFilename}")
        
        // Apenas processar os dados do CSV sem enviar para S3
        val produtos = parseCsvAndCreateProducts(file)

        // Salvar os produtos no banco de dados
        return produtoRepository.saveAll(produtos)
    }

    private fun parseCsvAndCreateProducts(file: MultipartFile): List<Produto> {
        val produtos = mutableListOf<Produto>()

        InputStreamReader(file.inputStream).use { reader ->
            CSVReaderBuilder(reader).withSkipLines(1).build().use { csvReader ->
                var line: Array<String>?
                while (csvReader.readNext().also { line = it } != null) {
                    line?.let {
                        val produto = Produto(
                            categoria = Categoria(),
                            nomeProduto = it[0] ?: "valor_padrao",
                            quantidadeProduto = it[2].toIntOrNull() ?: 0,
                            caminhoImagem = it[1] // Campo para caminho da imagem
                        )
                        produtos.add(produto)
                    }
                }
            }
        }
        return produtos
    }

    // Métodos CRUD básicos
    override fun findAll(): List<Produto> = produtoRepository.findAll()
    override fun findById(id: Int): Produto? = produtoRepository.findById(id).orElse(null)
}

package laize_tech.back.service


import laize_tech.back.entity.Produto
import laize_tech.back.repository.ProdutoRepository
import com.opencsv.CSVReaderBuilder
import laize_tech.back.entity.Categoria
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.io.InputStreamReader
import java.time.Instant

@Service
class UploadService(
    private val s3Client: S3Client,
    private val produtoRepository: ProdutoRepository,
    @Value("\${aws.s3.bucket-name}") private val bucketName: String
) {

    fun uploadFileAndProcess(file: MultipartFile): List<Produto> {
        // 1. Fazer o upload do arquivo original para o S3
        val fileKey = "uploads/${Instant.now().epochSecond}_${file.originalFilename}"
        uploadToS3(fileKey, file)

        // 2. Ler o arquivo e processar os dados (exemplo para CSV)
        val produtos = parseCsvAndCreateProducts(file)

        // 3. Salvar os produtos no banco de dados (parte do CRUD)
        return produtoRepository.saveAll(produtos)
    }

    private fun uploadToS3(key: String, file: MultipartFile) {
        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build()

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.inputStream, file.size))
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
                            nomeProduto = "valor_padrao",
                            quantidadeProduto = 0,
                            quantidade = it[2].toIntOrNull() ?: 0, // Defina um valor padrão ou obtenha do CSV
                           // nome = it[0], // Defina um valor padrão ou obtenha do CSV
                            sku = it[1],
                            // id = 0 // Defina um valor padrão ou obtenha do CSV
                        )
                        produtos.add(produto)
                    }
                }
            }
        }
        return produtos
    }

    // Métodos CRUD básicos
    fun findAll(): List<Produto> = produtoRepository.findAll()
    fun findById(id: Long): Produto? = produtoRepository.findById(id).orElse(null)
}
package laize_tech.back.service

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.net.URL
import java.nio.file.Paths

@Service
class StorageService {
    private val s3 = S3Client.builder()
        .region(Region.US_EAST_1) // região do bucket S3
        .credentialsProvider(DefaultCredentialsProvider.create())
        .build()

    private val bucketName = "raw"

    fun uploadFile(file: MultipartFile): String {
        val key = "uploads/${System.currentTimeMillis()}_${file.originalFilename}"
        val tempFile = Paths.get(System.getProperty("java.io.tmpdir"), file.originalFilename).toFile()
        file.transferTo(tempFile)
        val request = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build()
        s3.putObject(request, tempFile.toPath())
        return "https://$bucketName.s3.amazonaws.com/$key"
    }
}
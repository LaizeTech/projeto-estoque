package laize_tech.back.service

import laize_tech.back.entity.Produto
import org.springframework.web.multipart.MultipartFile

interface FileUploadService {
    fun uploadFileAndProcess(file: MultipartFile): List<Produto>
    fun findAll(): List<Produto>
    fun findById(id: Int): Produto?
}

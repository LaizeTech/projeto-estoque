package laize_tech.back.ControllerJpa

import laize_tech.back.entity.Arquivo
import laize_tech.back.service.ArquivoService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/arquivos")
class ArquivoController(
    private val arquivoService: ArquivoService
) {

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    fun uploadArquivo(@RequestParam("file") file: MultipartFile): Arquivo {
        return arquivoService.salvarArquivo(file)
    }

    @GetMapping
    fun listarArquivos(): List<Arquivo> = arquivoService.listarArquivos()

    @GetMapping("/{id}")
    fun buscarArquivo(@PathVariable id: Long): Arquivo = arquivoService.buscarArquivo(id)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletarArquivo(@PathVariable id: Long) = arquivoService.deletarArquivo(id)
}

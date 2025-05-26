package laize_tech.back.ControllerJpa

import jakarta.validation.Valid
import laize_tech.back.dto.PlataformaDTO
import laize_tech.back.dto.ProdutoDTO
import laize_tech.back.entity.Categoria
import laize_tech.back.entity.Plataforma
import laize_tech.back.repository.EmpresaRepository
import laize_tech.back.repository.PlataformaRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/plataforma")
class PlataformaJpaController(private val plataformaRepository: PlataformaRepository,
                              private val empresaRepository: EmpresaRepository
) {

    @GetMapping
    fun get(): ResponseEntity<List<Plataforma>> {
        val produtos = plataformaRepository.findAll()
        return if (produtos.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(produtos)
        }
    }

    @PostMapping("/adicionar")
    fun post(@RequestBody @Valid novaPlataformaDTO: PlataformaDTO): ResponseEntity<Plataforma> {
        val empresa = novaPlataformaDTO.idEmpresa?.let { idEmpresa ->
            empresaRepository.findById(idEmpresa).orElseThrow {
                IllegalArgumentException("Empresa não encontrada com o ID: $idEmpresa")
            }
        }

        val novaPlataforma = Plataforma(
            nomePlataforma = novaPlataformaDTO.nomePlataforma,
            status = novaPlataformaDTO.status,
            empresa = empresa
        )

        val plataformaSalva = plataformaRepository.save(novaPlataforma)
        return ResponseEntity.status(201).body(plataformaSalva)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<Void> {
        return if (plataformaRepository.existsById(id)) {
            plataformaRepository.deleteById(id)
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(404).build()
        }
    }

//    @PutMapping("/{id}")
//    fun put(@PathVariable id: Int, @RequestBody @Valid plataformaDTO: PlataformaDTO): ResponseEntity<Any> {
//        val plataformaExistente = plataformaRepository.findById(id).orElse(null)
//            ?: return ResponseEntity.status(404).body("Plataforma não encontrada")
//
//        if (plataformaDTO.nomePlataforma.isNullOrBlank()) {
//            return ResponseEntity.status(400).body("Nome da plataforma não pode estar em branco")
//        }
//
//        val empresa = plataformaDTO.idEmpresa?.let { idEmpresa ->
//            empresaRepository.findById(idEmpresa).orElseThrow {
//                IllegalArgumentException("Empresa não encontrada com o ID: $idEmpresa")
//            }
//        }
//
//        plataformaExistente.let {
//            it.nomePlataforma = plataformaDTO.nomePlataforma
//            it.status = plataformaDTO.status
//            it.empresa = empresa
//        }
//
//        val plataformaSalva = plataformaRepository.save(plataformaExistente)
//        return ResponseEntity.status(200).body(plataformaSalva)
//    }

    @PutMapping("/{id}")
    fun put(@PathVariable id: Int, @RequestBody @Valid plataformaDTO: PlataformaDTO): ResponseEntity<Any> {
        val plataformaExistente = plataformaRepository.findById(id).orElse(null)
            ?: return ResponseEntity.status(404).body("Plataforma não encontrada")

        if (plataformaDTO.nomePlataforma?.isBlank() == true) {
            return ResponseEntity.status(400).body("Nome da plataforma não pode estar em branco")
        }

        val empresa = plataformaDTO.idEmpresa?.let { idEmpresa ->
            empresaRepository.findById(idEmpresa).orElseThrow {
                IllegalArgumentException("Empresa não encontrada com o ID: $idEmpresa")
            }
        }

        plataformaExistente.nomePlataforma = plataformaDTO.nomePlataforma
        plataformaExistente.status = plataformaDTO.status
        plataformaExistente.empresa = empresa

        val plataformaSalva = plataformaRepository.save(plataformaExistente)
        return ResponseEntity.status(200).body(plataformaSalva)
    }

}
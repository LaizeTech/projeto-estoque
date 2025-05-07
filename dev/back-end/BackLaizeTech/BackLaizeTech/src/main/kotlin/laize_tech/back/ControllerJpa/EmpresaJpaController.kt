package laize_tech.back.ControllerJpa

import laize_tech.back.repository.EmpresaRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import laize_tech.back.entity.Empresa

@RestController
@RequestMapping("/empresas")
class EmpresaJpaController(val repositorio: EmpresaRepository) {

    @GetMapping
    fun get(): ResponseEntity<List<Empresa>> {
        val empresas = repositorio.findAll()
        return if (empresas.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(empresas)
        }
    }

    @GetMapping("/por-cnpj")
    fun getByCnpj(@RequestParam cnpj: String): ResponseEntity<Empresa> {
        return try {
            val empresa = repositorio.findByCnpj(cnpj)
            ResponseEntity.status(200).body(empresa)
        } catch (e: Exception) {
            ResponseEntity.status(404).build()
        }
    }

    @GetMapping("/por-nome")
    fun getByNome(@RequestParam nome: String): ResponseEntity<List<Empresa>> {
        val empresas = repositorio.findByNomeEmpresaContainingIgnoreCase(nome)
        return if (empresas.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(empresas)
        }
    }
}
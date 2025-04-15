package laize_tech.back.ControllerJpa

import laize_tech.back.repository.EmpresaRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import laize_tech.back.entity.Empresa
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/empresas")
class EmpresaJpaController(val repositorio: EmpresaRepository) {

    @GetMapping
    fun get():ResponseEntity<List<Empresa>> {
        val empresas = repositorio.findAll()

        return if (empresas.isEmpty()){
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(empresas)
        }
    }
}
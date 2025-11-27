package laize_tech.back.ControllerJpa

import laize_tech.back.dto.EmpresaDTO
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

    @PostMapping
    fun create(@RequestBody empresa: EmpresaDTO): ResponseEntity<Any> {
        if (empresa.nomeEmpresa.isNullOrBlank() || empresa.cnpj.isNullOrBlank()) {
            return ResponseEntity.badRequest().body("Os campos nome e CNPJ não podem estar vazios ou nulos!")
        }
        if (repositorio.existsByCnpj(empresa.cnpj)) {
            return ResponseEntity.status(409).body("Já existe uma empresa cadastrada com esse CNPJ!")
        }

        return try {
            val novaEmpresa = Empresa(
                idEmpresa = null,
                nomeEmpresa = empresa.nomeEmpresa,
                cnpj = empresa.cnpj
            )
            val empresaSalva = repositorio.save(novaEmpresa)
            val resposta = EmpresaDTO(empresaSalva.nomeEmpresa, empresaSalva.cnpj)
            ResponseEntity.status(201).body(resposta)
        } catch (e: Exception) {
            ResponseEntity.status(500).body("Erro ao salvar empresa")
        }
    }


    @PutMapping("/{id}")
    fun update(@PathVariable id: Int, @RequestBody empresaAtualizada: Empresa): ResponseEntity<Empresa> {
        val existente = repositorio.findById(id)
        return if (existente.isEmpty) {
            val empresa = existente.get().copy(
                nomeEmpresa = empresaAtualizada.nomeEmpresa,
                cnpj = empresaAtualizada.cnpj
            )
            ResponseEntity.ok(repositorio.save(empresa))
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<Void> {
        return if (repositorio.existsById(id)) {
            repositorio.deleteById(id)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}

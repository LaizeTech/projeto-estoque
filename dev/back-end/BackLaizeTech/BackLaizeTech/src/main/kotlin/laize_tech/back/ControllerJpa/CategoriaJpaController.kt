package laize_tech.back.ControllerJpa

import jakarta.validation.Valid
import laize_tech.back.entity.Categoria
import laize_tech.back.repository.CategoriaRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/categorias")
class  CategoriaJpaController (val repositorio: CategoriaRepository) {

    @GetMapping
    fun get():ResponseEntity<List<Categoria>> {
        val categorias = repositorio.findAll()

        return if (categorias.isEmpty()){
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(categorias)
        }
    }

    @PostMapping("/adicionar")
    fun post(@RequestBody @Valid novaCategoria: Categoria): ResponseEntity<Any> {

        if (novaCategoria.nomeCategoria.isBlank()) {
            return ResponseEntity.status(400).body("")
        }

        if (repositorio.findAll().any { it.nomeCategoria == novaCategoria.nomeCategoria }) {
            return ResponseEntity.status(409).body("")
        }

        val categoriaSalva = repositorio.save(novaCategoria)
        return ResponseEntity.status(201).body(categoriaSalva)
    }

    @PutMapping("/{id}")
    fun put(@PathVariable id: Int, @RequestBody categoriaAtualizada: Categoria): ResponseEntity<Any> {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.status(404).body("")
        }

        if (categoriaAtualizada.nomeCategoria.isBlank()) {
            return ResponseEntity.status(400).body("")
        }

        if (repositorio.findAll().any { it.nomeCategoria == categoriaAtualizada.nomeCategoria && it.idCategoria != id }) {
            return ResponseEntity.status(409).body("")
        }

        categoriaAtualizada.idCategoria = id
        val categoriaSalva = repositorio.save(categoriaAtualizada)
        return ResponseEntity.status(200).body(categoriaSalva)
    }

    @DeleteMapping ("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<String> {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id)
            return ResponseEntity.status(204).build()
        }
        val mensagem = "Não foi possível deletar a categoria com id $id"
        return ResponseEntity.status(404).body("")
    }
}
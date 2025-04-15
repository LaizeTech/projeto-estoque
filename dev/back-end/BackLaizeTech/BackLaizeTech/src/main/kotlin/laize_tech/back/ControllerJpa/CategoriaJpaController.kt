package laize_tech.back.ControllerJpa

import jakarta.validation.Valid
import laize_tech.back.entity.Categoria
import laize_tech.back.repository.CategoriaRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/categorias")
class CategoriaJpaController (val repositorio: CategoriaRepository) {

    @GetMapping
    fun get():ResponseEntity<List<Categoria>> {
        val categorias = repositorio.findAll()

        return if (categorias.isEmpty()){
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(categorias)
        }
    }

    @PostMapping ("/adicionar")
    fun post(@RequestBody @Valid novaCategoria: Categoria):
            ResponseEntity<Categoria> {

        val categoria = repositorio.save(novaCategoria)
        return ResponseEntity.status(201).body(categoria)
    }

    @PutMapping("/{id}")
    fun put (@PathVariable id:Int, @RequestBody categoriaAtualizada: Categoria): ResponseEntity<Categoria>{

        if (!repositorio.existsById(id)){
            return ResponseEntity.status(404).build()
        }

        categoriaAtualizada.idCategoria = id
        val categoria = repositorio.save(categoriaAtualizada)
        return ResponseEntity.status(200).body(categoria)
    }

    @DeleteMapping ("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<String> {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id)
            return ResponseEntity.status(204).build()
        }
        val mensagem = "Não foi possível deletar a categoria com id $id"
        return ResponseEntity.status(404).body(mensagem)
    }
}
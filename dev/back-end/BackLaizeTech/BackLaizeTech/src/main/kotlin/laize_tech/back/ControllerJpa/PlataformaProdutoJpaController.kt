package laize_tech.back.ControllerJpa

import laize_tech.back.dto.PlataformaProdutoDTO
import laize_tech.back.entity.PlataformaProduto
import laize_tech.back.repository.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/plataforma-produto")
class PlataformaProdutoJpaController(
    private val plataformaProdutoRepository: PlataformaProdutoRepository,
    private val plataformaRepository: PlataformaRepository,
    private val produtoRepository: ProdutoRepository,
    private val caracteristicaRepository: CaracteristicaRepository,
    private val tipoCaracteristicaRepository: TipoCaracteristicaRepository,
    private val produtoCaracteristicaRepository: ProdutoCaracteristicaRepository
) {

    @GetMapping
    fun getAll(): ResponseEntity<List<PlataformaProduto>> {
        val lista = plataformaProdutoRepository.findAll()

        return if (lista.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(lista)
        }
    }

    @PostMapping
    fun create(@RequestBody dto: PlataformaProdutoDTO): ResponseEntity<PlataformaProduto> {
        val plataforma = plataformaRepository.findById(dto.fkPlataforma)
        val produto = produtoRepository.findById(dto.fkProduto)
        val caracteristica = caracteristicaRepository.findById(dto.fkCaracteristica)
        val tipoCaracteristica = tipoCaracteristicaRepository.findById(dto.fkTipoCaracteristica.toLong())
        val produtoCaracteristica = produtoCaracteristicaRepository.findById(dto.fkProdutoCaracteristica)

        if (plataforma.isEmpty || produto.isEmpty || caracteristica.isEmpty ||
            tipoCaracteristica.isEmpty || produtoCaracteristica.isEmpty
        ) {
            return ResponseEntity.status(404).build()
        }

        val nova = PlataformaProduto(
            plataforma = plataforma.get(),
            produto = produto.get(),
            caracteristica = caracteristica.get(),
            tipoCaracteristica = tipoCaracteristica.get(),
            produtoCaracteristica = produtoCaracteristica.get(),
            quantidadeProdutoPlataforma = dto.quantidadeProdutoPlataforma
        )
        return ResponseEntity.status(201).body(plataformaProdutoRepository.save(nova))
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Int, @RequestBody dto: PlataformaProdutoDTO): ResponseEntity<PlataformaProduto> {
        val existente = plataformaProdutoRepository.findById(id)
        if (existente.isEmpty) return ResponseEntity.status(404).build()

        val plataforma = plataformaRepository.findById(dto.fkPlataforma)
        val produto = produtoRepository.findById(dto.fkProduto)
        val caracteristica = caracteristicaRepository.findById(dto.fkCaracteristica)
        val tipoCaracteristica = tipoCaracteristicaRepository.findById(dto.fkTipoCaracteristica.toLong())
        val produtoCaracteristica = produtoCaracteristicaRepository.findById(dto.fkProdutoCaracteristica)

        if (plataforma.isEmpty || produto.isEmpty || caracteristica.isEmpty ||
            tipoCaracteristica.isEmpty || produtoCaracteristica.isEmpty
        ) {
            return ResponseEntity.status(400).build()
        }

        val atualizado = existente.get().apply {
            this.plataforma = plataforma.get()
            this.produto = produto.get()
            this.caracteristica = caracteristica.get()
            this.tipoCaracteristica = tipoCaracteristica.get()
            this.produtoCaracteristica = produtoCaracteristica.get()
            this.quantidadeProdutoPlataforma = dto.quantidadeProdutoPlataforma
        }
        return ResponseEntity.ok(plataformaProdutoRepository.save(atualizado))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<Void> {
        if (!plataformaProdutoRepository.existsById(id)) {
            return ResponseEntity.status(404).build()
        }
        plataformaProdutoRepository.deleteById(id)
        return ResponseEntity.status(204).build()
    }

    @PatchMapping("/{id}/quantidade")
    fun updateQuantidade(@PathVariable id: Int, @RequestParam quantidade: Int): ResponseEntity<PlataformaProduto> {
        val existente = plataformaProdutoRepository.findById(id)
        if (existente.isEmpty) return ResponseEntity.status(404).build()

        val atualizado = existente.get().apply {
            this.quantidadeProdutoPlataforma = quantidade
        }

        return ResponseEntity.ok(plataformaProdutoRepository.save(atualizado))
    }

}




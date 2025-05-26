package laize_tech.back.ControllerJpa


import laize_tech.back.dto.ProdutoCaracteristicaDTO
import laize_tech.back.entity.ProdutoCaracteristica
import laize_tech.back.repository.CaracteristicaRepository
import laize_tech.back.repository.ProdutoCaracteristicaRepository
import laize_tech.back.repository.ProdutoRepository
import laize_tech.back.repository.TipoCaracteristicaRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/produto-caracteristica")
class ProdutoCaracteristicaJpaController(
    private val repositorio: ProdutoCaracteristicaRepository,
    private val tipoCaracteristicaRepository: TipoCaracteristicaRepository,
    private val caracteristicaRepository: CaracteristicaRepository,
    private val produtoRepository: ProdutoRepository,
) {

    @GetMapping
    fun get(): ResponseEntity<List<ProdutoCaracteristica>> {
        val produtos = repositorio.findAll()

        return if (produtos.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(produtos)
        }
    }

    @PostMapping
    fun post(@RequestBody novoProduto: ProdutoCaracteristicaDTO): ResponseEntity<ProdutoCaracteristica> {
        // Valide e carregue entidades relacionadas
        val tipoCaracteristica = novoProduto.idTipoCaracteristica.let {
            tipoCaracteristicaRepository.findById(it).orElseThrow {
                IllegalArgumentException("TipoCaracteristica com ID $it não encontrado")
            }
        }
        val caracteristica = novoProduto.idCaracteristica.let {
            caracteristicaRepository.findById(it.toInt()).orElseThrow {
                IllegalArgumentException("Caracteristica com ID $it não encontrada")
            }
        }

        val produto = novoProduto.idProduto.let {
            produtoRepository.findById(it.toInt()).orElseThrow {
                IllegalArgumentException("Produto com ID $it não encontrada")
            }
        }

        val produtoCaracteristica = ProdutoCaracteristica(
            tipoCaracteristica = tipoCaracteristica,
            caracteristica = caracteristica,
            produto = produto
        )

        produtoCaracteristica.quantidadeProdutoCaracteristica = novoProduto.quantidadeProduto

        return ResponseEntity.status(201).body(repositorio.save(produtoCaracteristica))
    }

    @PutMapping("/{codigo}")
    fun put(@PathVariable codigo: Int, @RequestBody novoProduto: ProdutoCaracteristicaDTO): ResponseEntity<ProdutoCaracteristica> {
        val existente = repositorio.findById(codigo).orElse(null)
            ?: return ResponseEntity.status(404).build()

        val tipoCaracteristica = novoProduto.idTipoCaracteristica.let {
            tipoCaracteristicaRepository.findById(it).orElseThrow {
                IllegalArgumentException("TipoCaracteristica com ID $it não encontrado")
            }
        }
        val caracteristica = novoProduto.idCaracteristica.let {
            caracteristicaRepository.findById(it.toInt()).orElseThrow {
                IllegalArgumentException("Caracteristica com ID $it não encontrada")
            }
        }
        val produto = novoProduto.idProduto.let {
            produtoRepository.findById(it.toInt()).orElseThrow {
                IllegalArgumentException("Produto com ID $it não encontrada")
            }
        }

        existente.tipoCaracteristica = tipoCaracteristica
        existente.caracteristica = caracteristica
        existente.produto = produto
        existente.quantidadeProdutoCaracteristica = novoProduto.quantidadeProduto

        return ResponseEntity.status(200).body(repositorio.save(existente))
    }

    @DeleteMapping("/{codigo}")
    fun delete(@PathVariable codigo: Int): ResponseEntity<Void> {
        if (!repositorio.existsById(codigo)) {
            return ResponseEntity.status(404).build()
        }

        repositorio.deleteById(codigo)
        return ResponseEntity.status(204).build()
    }

    @PatchMapping("/{codigo}")
    fun patchQuantidade(@PathVariable codigo: Int, @RequestParam quantidade: Int): ResponseEntity<ProdutoCaracteristica> {
        val produto = repositorio.findById(codigo).orElse(null)
            ?: return ResponseEntity.status(404).build()

        produto.quantidadeProdutoCaracteristica = quantidade
        val produtoAtualizado = repositorio.save(produto)

        return ResponseEntity.status(200).body(produtoAtualizado)
    }

}
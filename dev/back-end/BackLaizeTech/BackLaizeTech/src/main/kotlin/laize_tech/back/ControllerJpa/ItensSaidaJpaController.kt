package laize_tech.back.ControllerJpa

import jakarta.validation.Valid
import laize_tech.back.entity.ItensSaida
import laize_tech.back.repository.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/itens")
class ItensSaidaJpaController(
    val repositorio: ItensSaidaRepository,
    val tipoCaracteristicaRepository: TipoCaracteristicaRepository,
    val caracteristicaRepository: CaracteristicaRepository,
    val plataformaRepository: PlataformaRepository,
    val produtoRepository: ProdutoRepository,
    val saidaRepository: SaidaRepository,
    val produtoCaracteristicaRepository: ProdutoCaracteristicaRepository
) {

    @GetMapping
    fun get(): ResponseEntity<List<ItensSaida>> {
        val itens = repositorio.findAll()

        return if (itens.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(itens)
        }
    }

    @PostMapping("/adicionar")
    fun post(@RequestBody @Valid novoItem: ItensSaida): ResponseEntity<ItensSaida> {
        // Valide e carregue entidades relacionadas
        val tipoCaracteristica = novoItem.tipoCaracteristica?.idTipoCaracteristica?.let {
            tipoCaracteristicaRepository.findById(it.toLong()).orElseThrow {
                IllegalArgumentException("TipoCaracteristica com ID $it não encontrado")
            }
        }
        val caracteristica = novoItem.caracteristica?.idCaracteristica?.let {
            caracteristicaRepository.findById(it.toLong().toInt()).orElseThrow {
                IllegalArgumentException("Caracteristica com ID $it não encontrada")
            }
        }

        val plataforma = novoItem.plataforma?.idPlataforma?.let {
            plataformaRepository.findById(it.toLong().toInt()).orElseThrow {
                IllegalArgumentException("Plataforma com ID $it não encontrada")
            }
        }

        val produto = novoItem.produto?.idProduto?.let {
            produtoRepository.findById(it.toLong().toInt()).orElseThrow {
                IllegalArgumentException("Produto com ID $it não encontrada")
            }
        }

        val saida = novoItem.saida?.idSaida?.let {
            saidaRepository.findById(it.toLong().toInt()).orElseThrow {
                IllegalArgumentException("Saida com ID $it não encontrada")
            }
        }

        val produtoCaracteristica = novoItem.produtoCaracteristica?.idProdutoCaracteristica?.let {
            produtoCaracteristicaRepository.findById(it.toLong().toInt()).orElseThrow {
                IllegalArgumentException("ProdutoCaracteristica com ID $it não encontrada")
            }
        }

        novoItem.tipoCaracteristica = tipoCaracteristica
        novoItem.caracteristica = caracteristica
        novoItem.plataforma = plataforma
        novoItem.produto = produto
        novoItem.saida = saida
        novoItem.produtoCaracteristica = produtoCaracteristica

        // Salve o item
        val itens = repositorio.save(novoItem)
        return ResponseEntity.status(201).body(itens)
    }

    @PutMapping("/{id}")
    fun put(@PathVariable id: Int, @RequestBody itemAtualizado: ItensSaida): ResponseEntity<ItensSaida> {
        val itemExistente = repositorio.findById(id).orElse(null)
            ?: return ResponseEntity.status(404).build()

        // Atualize os campos do itemExistente com os valores do itemAtualizado
        itemAtualizado.saida?.idSaida?.let {
            itemExistente.saida = saidaRepository.findById(it.toInt()).orElseThrow {
                IllegalArgumentException("Saida com ID $it não encontrada")
            }
        }
        itemAtualizado.plataforma?.idPlataforma?.let {
            itemExistente.plataforma = plataformaRepository.findById(it.toInt()).orElseThrow {
                IllegalArgumentException("Plataforma com ID $it não encontrada")
            }
        }
        itemAtualizado.tipoCaracteristica?.idTipoCaracteristica?.let {
            itemExistente.tipoCaracteristica = tipoCaracteristicaRepository.findById(it.toLong()).orElseThrow {
                IllegalArgumentException("TipoCaracteristica com ID $it não encontrada")
            }
        }
        itemAtualizado.caracteristica?.idCaracteristica?.let {
            itemExistente.caracteristica = caracteristicaRepository.findById(it.toInt()).orElseThrow {
                IllegalArgumentException("Caracteristica com ID $it não encontrada")
            }
        }
        itemAtualizado.produtoCaracteristica?.idProdutoCaracteristica?.let {
            itemExistente.produtoCaracteristica = produtoCaracteristicaRepository.findById(it.toInt()).orElseThrow {
                IllegalArgumentException("ProdutoCaracteristica com ID $it não encontrada")
            }
        }
        itemAtualizado.produto?.idProduto?.let {
            itemExistente.produto = produtoRepository.findById(it.toInt()).orElseThrow {
                IllegalArgumentException("Produto com ID $it não encontrada")
            }
        }

        itemExistente.quantidade = itemAtualizado.quantidade ?: itemExistente.quantidade
        itemExistente.subTotal = itemAtualizado.subTotal ?: itemExistente.subTotal

        val itens = repositorio.save(itemExistente)
        return ResponseEntity.status(200).body(itens)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<String> {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id)
            return ResponseEntity.status(204).build()
        }
        val mensagem = "Não foi possível deletar o item com id $id"
        return ResponseEntity.status(404).body(mensagem)
    }

}



//    @GetMapping("/por-saida/{idSaida}")
//    fun getBySaida(@PathVariable idSaida: Int): ResponseEntity<List<ItensSaida>> {
//        val itens = repositorio.findBySaida_IdSaida(idSaida)
//
//        return if (itens.isEmpty()) {
//            ResponseEntity.status(204).build()
//        } else {
//            ResponseEntity.status(200).body(itens)
//        }
//    }

//    @GetMapping("/por-produto/{idProduto}")
//    fun getByProduto(@PathVariable idProduto: Int): ResponseEntity<List<ItensSaida>> {
//        val itens = repositorio.findByProduto_IdProduto(idProduto)
//
//        return if (itens.isEmpty()) {
//            ResponseEntity.status(204).build()
//        } else {
//            ResponseEntity.status(200).body(itens)
//        }
//    }
//
//    @GetMapping("/por-empresa/{idEmpresa}")
//    fun getByEmpresa(@PathVariable idEmpresa: Int): ResponseEntity<List<ItensSaida>> {
//        val itens = repositorio.findByEmpresa_IdEmpresa(idEmpresa)
//
//        return if (itens.isEmpty()) {
//            ResponseEntity.status(204).build()
//        } else {
//            ResponseEntity.status(200).body(itens)
//        }
//    }
//
//    @GetMapping("/por-plataforma/{idPlataforma}")
//    fun getByPlataforma(@PathVariable idPlataforma: Int): ResponseEntity<List<ItensSaida>> {
//        val itens = repositorio.findByPlataforma_IdPlataforma(idPlataforma)
//
//        return if (itens.isEmpty()) {
//            ResponseEntity.status(204).build()
//        } else {
//            ResponseEntity.status(200).body(itens)
//        }
//    }
//
//    @GetMapping("/subtotal/{idSaida}")
//    fun getSubTotalPorSaida(@PathVariable idSaida: Int): ResponseEntity<Double?> {
//        val subtotal = repositorio.somaSubTotalPorSaida(idSaida)
//        return if (subtotal == null) {
//            ResponseEntity.status(204).build()
//        } else {
//            ResponseEntity.status(200).body(subtotal)
//        }
//    }

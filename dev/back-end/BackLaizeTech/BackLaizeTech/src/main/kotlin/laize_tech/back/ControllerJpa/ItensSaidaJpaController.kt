package laize_tech.back.ControllerJpa

import jakarta.validation.Valid
import laize_tech.back.dto.ItensSaidaDTO
import laize_tech.back.entity.ItensSaida
import laize_tech.back.exceptions.IdNaoEncontradoException
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

    @PostMapping
    fun post(@RequestBody @Valid novoItem: ItensSaidaDTO): ResponseEntity<ItensSaida> {
        // Valide e carregue entidades relacionadas
        val tipoCaracteristica = novoItem.idTipoCaracteristica.let {
            tipoCaracteristicaRepository.findById(it).orElseThrow {
                IdNaoEncontradoException("TipoCaracteristica", it)
            }
        }
        val caracteristica = novoItem.idCaracteristica.let {
            caracteristicaRepository.findById(it.toInt()).orElseThrow {
                IdNaoEncontradoException("Caracteristica", it)
            }
        }

        val plataforma = novoItem.idPlataforma.let {
            plataformaRepository.findById(it.toInt()).orElseThrow {
                IdNaoEncontradoException("Plataforma", it)
            }
        }

        val produto = novoItem.idProduto.let {
            produtoRepository.findById(it.toInt().toLong()).orElseThrow {
                IdNaoEncontradoException("Produto", it)
            }
        }

        val saida = novoItem.idSaida.let {
            saidaRepository.findById(it.toInt()).orElseThrow {
                IdNaoEncontradoException("Saida", it)
            }
        }

        val produtoCaracteristica = novoItem.idProdutoCaracteristica.let {
            produtoCaracteristicaRepository.findById(it.toInt()).orElseThrow {
                IdNaoEncontradoException("ProdutoCaracteristica", it)
            }
        }

        val item = ItensSaida(
            saida = saida,
            plataforma = plataforma,
            quantidade = novoItem.quantidade,
            tipoCaracteristica = tipoCaracteristica,
            caracteristica = caracteristica,
            produtoCaracteristica = produtoCaracteristica,
            produto = produto
        )

        val itens = repositorio.save(item)
        return ResponseEntity.status(201).body(itens)
    }

    @PutMapping("/{id}")
    fun put(@PathVariable id: Int, @RequestBody itemAtualizado: ItensSaidaDTO): ResponseEntity<ItensSaida> {
        val itemExistente = repositorio.findById(id).orElse(null)
            ?: return ResponseEntity.status(404).build()

        itemAtualizado.idSaida.let {
            itemExistente.saida = saidaRepository.findById(it.toInt()).orElseThrow {
                IdNaoEncontradoException("Saida", it)
            }
        }
        itemAtualizado.idPlataforma.let {
            itemExistente.plataforma = plataformaRepository.findById(it.toInt()).orElseThrow {
                IdNaoEncontradoException("Plataforma", it)
            }
        }
        itemAtualizado.idTipoCaracteristica.let {
            itemExistente.tipoCaracteristica = tipoCaracteristicaRepository.findById(it).orElseThrow {
                IdNaoEncontradoException("TipoCaracteristica", it)
            }
        }
        itemAtualizado.idCaracteristica.let {
            itemExistente.caracteristica = caracteristicaRepository.findById(it.toInt()).orElseThrow {
                IdNaoEncontradoException("Caracteristica", it)
            }
        }
        itemAtualizado.idProdutoCaracteristica.let {
            itemExistente.produtoCaracteristica = produtoCaracteristicaRepository.findById(it.toInt()).orElseThrow {
                IdNaoEncontradoException("ProdutoCaracteristica", it)            }
        }
        itemAtualizado.idProduto.let {
            itemExistente.produto = produtoRepository.findById(it.toInt().toLong()).orElseThrow {
                IdNaoEncontradoException("Produto", it)
            }
        }

        itemExistente.quantidade = itemAtualizado.quantidade

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

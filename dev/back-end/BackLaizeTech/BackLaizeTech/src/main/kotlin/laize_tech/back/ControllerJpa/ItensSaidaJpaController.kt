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
            produtoRepository.findById(it).orElseThrow {
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
            itemExistente.produto = produtoRepository.findById(it).orElseThrow {
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

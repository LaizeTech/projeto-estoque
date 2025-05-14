package laize_tech.back.ControllerJpa

import laize_tech.back.controller.Produto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/estoque")
class PlataformaEstoqueController {
    private val listaEstoque: MutableMap<String, MutableList<Produto>> = mutableMapOf(
        "Shopee" to mutableListOf(
            Produto("Batom", "Boca", 5, 1, false),
        ),
        "Nuvem_shop" to mutableListOf(
            Produto("Batom", "Boca", 5, 2, false),

        ),
        "Venda_direta" to mutableListOf(
            Produto("Batom", "Boca", 5, 3, false)
        ),
        "Loja_fisica" to mutableListOf(
            Produto("Batom", "Boca", 5, 4, false)
        )
    )
    @GetMapping("/lista-estoque")
    fun getProdutos(): ResponseEntity<Map<String, List<Produto>>>? {
        return if (listaEstoque.isEmpty()) ResponseEntity.status(404).build() else ResponseEntity.ok(listaEstoque)
    }
    @GetMapping("/{plataforma}/{produto}")
    fun getPlataformaProduto(@PathVariable plataforma:String, @PathVariable produto:String): ResponseEntity<Produto?> {
        return if (plataforma.isEmpty() or produto.isEmpty()) ResponseEntity.status(404).build() else ResponseEntity.ok(listaEstoque[plataforma]?.find { it.nome == produto })
    }
    @PostMapping("/{chave}")
    fun addProduto(@RequestBody produto: Produto, @PathVariable chave:Int): ResponseEntity<Produto> {
        var ax:String? = null
        when(chave) {
            1 -> ax = "Shoppe"
            2 -> ax = "Nuvem_shop"
            3 -> ax = "Venda_direta"
            4 -> ax = "Loja_fisica"
            else -> ResponseEntity.status(404).body(null)
        }
        listaEstoque[ax]?.add(produto)
        return ResponseEntity.status(200).build()
    }
    @PutMapping("/Atualizar-produto/{plataforma}/{produtoAntigo}")
    fun updateProduto(@PathVariable plataforma:String, @PathVariable produtoAntigo: String, @RequestBody produtoNovo: Produto): ResponseEntity<String>{
        if (plataforma.isEmpty()) return ResponseEntity.status(404).body("Plataforma invalida!")
        if (produtoAntigo.isEmpty()) return ResponseEntity.status(404).body("Produto invalido")
        listaEstoque[plataforma]?.let{
            var value = it.find { it.nome == produtoAntigo }
            it[it.indexOf(value)] = produtoNovo
        }
        return ResponseEntity.status(200).body("Produto atualizado com sucesso")
    }
    @DeleteMapping("/deletar-produto/{plataforma}/{produto}")
    fun deleteProduto(@PathVariable plataforma: String, @PathVariable produto: String): ResponseEntity<String>{
        if (plataforma.isEmpty()) return ResponseEntity.status(404).body("Plataforma invalida!")
        if (produto.isEmpty()) return ResponseEntity.status(404).body("Produto invalido")
        listaEstoque[plataforma]?.let { listaEstoque[plataforma]?.removeAt(it.indexOfFirst { it.nome == produto }) }
        return ResponseEntity.status(200).body("Produto removido com sucesso!")
    }
}

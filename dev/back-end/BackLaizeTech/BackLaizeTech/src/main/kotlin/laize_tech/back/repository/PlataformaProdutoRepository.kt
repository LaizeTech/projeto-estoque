package laize_tech.back.repository

import jakarta.transaction.Transactional
import laize_tech.back.entity.PlataformaProduto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface PlataformaProdutoRepository : JpaRepository<PlataformaProduto, Int> {

    @Transactional
    @Modifying
    @Query("select e from PlataformaProduto e")
    fun findAllPlataformas(): List<PlataformaProduto>

    fun findByProdutoNomeProduto(nome: String): List<PlataformaProduto>

    fun findByPlataformaNomePlataforma(nome: String): List<PlataformaProduto>

    fun findByEmpresaNomeEmpresa(nomeEmpresa: String): List<PlataformaProduto>

    fun findByPlataformaNomePlataformaAndProdutoNomeProduto(plataforma: String, produto: String): PlataformaProduto?

    fun findByEmpresaIdEmpresa(id: Int): List<PlataformaProduto>
}
package laize_tech.back.repository

import jakarta.transaction.Transactional
import laize_tech.back.entity.PlataformaProduto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface PlataformaProdutoRepository : JpaRepository<PlataformaProduto, Int> {

    @Transactional
    @Modifying
    @Query("select e from PlataformaProduto e") //JPQL --> NÃO é SQL.
    fun findAllPlataformas(): List<PlataformaProduto>

    // Buscar todos os registros de estoque por nome do produto
    fun findByProdutoNome(nome: String): List<PlataformaProduto>

    // Buscar todos os registros de estoque por nome da plataforma
    fun findByPlataformaNome(nome: String): List<PlataformaProduto>

    // Buscar todos os registros de estoque por nome da empresa
    fun findByEmpresaNomeEmpresa(nomeEmpresa: String): List<PlataformaProduto>

    // Buscar um estoque específico por nome da plataforma e nome do produto
    fun findByPlataformaNomeAndProdutoNome(plataforma: String, produto: String): PlataformaProduto?

    // Buscar todos os estoques por ID da empresa
    fun findByEmpresaIdEmpresa(id: Int): List<PlataformaProduto>
}
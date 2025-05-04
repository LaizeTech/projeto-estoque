package laize_tech.back.repository

import jakarta.transaction.Transactional
import laize_tech.back.entity.Produto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate

interface ProdutoRepository : JpaRepository<Produto, Int> {

    @Transactional
    @Modifying
    @Query("select e from Produto e") //JPQL --> NÃO é SQL.
    fun findAllPlataformas(): List<Produto>

    //Buscar todos os produtos de uma determinada categoria
    @Query("SELECT p FROM Produto p WHERE p.categoria.nomeCategoria = :categoriaNome")
    fun findByCategoriaNome(categoriaNome: String): List<Produto>

    //Buscar produtos cadastrados após uma certa data
    @Query("SELECT p FROM Produto p WHERE p.dataCadastro > :data")
    fun findProdutosCadastradosDepoisDe(data: LocalDate): List<Produto>

    //Buscar produtos com valor de compra menor que X
    @Query("SELECT p FROM Produto p WHERE p.precoCompra < :precoMax")
    fun findByPrecoCompraMenorQue(precoMax: Double): List<Produto>

    //Contar produtos de uma categoria específica
    @Query("SELECT COUNT(p) FROM Produto p WHERE p.categoria.nomeCategoria = :categoriaNome")
    fun countByCategoria(categoriaNome: String): Long
}
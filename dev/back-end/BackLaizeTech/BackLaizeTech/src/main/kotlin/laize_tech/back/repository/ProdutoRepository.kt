package laize_tech.back.repository

import jakarta.transaction.Transactional
import laize_tech.back.entity.Produto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate

interface ProdutoRepository : JpaRepository<Produto, Int> {

//    @Transactional
//    @Modifying
//    @Query("select e from Produto e") //JPQL --> NÃO é SQL.
//    fun findAllPlataformas(): List<Produto>
//
//    // Dynamic Finder: produtos por nome da categoria
//    fun findByCategoria_NomeCategoria(categoriaNome: String): List<Produto>
//
//    // Dynamic Finder: produtos com preço de compra menor que
//    fun findByPrecoCompraLessThan(precoMax: Double): List<Produto>
//
//    //Contar produtos de uma categoria específica
//    @Query("SELECT COUNT(p) FROM Produto p WHERE p.categoria.nomeCategoria = :categoriaNome")
//    fun countByCategoria(categoriaNome: String): Long
}
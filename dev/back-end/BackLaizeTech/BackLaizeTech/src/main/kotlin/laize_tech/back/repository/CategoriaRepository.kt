package laize_tech.back.repository

import jakarta.transaction.Transactional
import laize_tech.back.entity.Categoria
import laize_tech.back.entity.Empresa
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface CategoriaRepository : JpaRepository<Categoria, Int> {

    @Transactional
    @Modifying
    @Query("SELECT c FROM Categoria c")
    fun findAllCategoriasCustom(): List<Categoria>

    //Buscar categorias pelo nome (exato)
    fun findByNomeCategoria(nome: String): List<Categoria>

    //Buscar categorias com nome contendo texto (como LIKE)
    fun findByNomeCategoriaContainingIgnoreCase(fragmento: String): List<Categoria>

    //Contar categorias com determinado nome
    fun countByNomeCategoria(nome: String): Long
}
package laize_tech.back.repository

import jakarta.transaction.Transactional
import laize_tech.back.entity.Categoria
import laize_tech.back.entity.Empresa
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface CategoriaRepository : JpaRepository<Categoria, Int> {
}
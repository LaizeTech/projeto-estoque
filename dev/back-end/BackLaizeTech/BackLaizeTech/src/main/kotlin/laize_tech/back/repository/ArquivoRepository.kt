package laize_tech.back.repository

import laize_tech.back.entity.Arquivo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ArquivoRepository : JpaRepository<Arquivo, Int>

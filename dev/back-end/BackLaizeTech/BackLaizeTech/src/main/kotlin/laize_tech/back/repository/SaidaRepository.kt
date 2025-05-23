package laize_tech.back.repository

import laize_tech.back.entity.Saida
import org.springframework.data.jpa.repository.JpaRepository

interface SaidaRepository : JpaRepository<Saida, Int> {

}
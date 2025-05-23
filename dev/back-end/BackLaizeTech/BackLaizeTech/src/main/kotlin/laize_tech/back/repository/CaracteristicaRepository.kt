package laize_tech.back.repository

import laize_tech.back.entity.Caracteristica
import org.springframework.data.jpa.repository.JpaRepository

interface CaracteristicaRepository : JpaRepository<Caracteristica, Int> {
}
package laize_tech.back.repository

import laize_tech.back.entity.ItensSaida
import org.springframework.data.jpa.repository.JpaRepository

interface ItensSaidaRepository : JpaRepository<ItensSaida, Int> {

}

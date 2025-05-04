package laize_tech.back.repository

import laize_tech.back.entity.TipoSaida
import org.springframework.data.jpa.repository.JpaRepository

interface TipoSaidaRepository : JpaRepository<TipoSaida, Int>
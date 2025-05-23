package laize_tech.back.repository

import laize_tech.back.entity.TipoCaracteristica
import org.springframework.data.jpa.repository.JpaRepository

interface TipoCaracteristicaRepository  : JpaRepository<TipoCaracteristica, Long> {
}
package laize_tech.back.repository

import laize_tech.back.entity.ConfiguracaoAlertasQTD
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ConfiguracaoAlertasQTDRepository : JpaRepository<ConfiguracaoAlertasQTD, Int>
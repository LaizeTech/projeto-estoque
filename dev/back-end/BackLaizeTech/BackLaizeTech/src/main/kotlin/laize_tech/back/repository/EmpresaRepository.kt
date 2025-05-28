package laize_tech.back.repository

import jakarta.transaction.Transactional
import laize_tech.back.entity.Empresa
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface EmpresaRepository : JpaRepository<Empresa, Int> {

    @Transactional
    @Modifying
    @Query("SELECT e FROM Empresa e")
    fun findAllEmpresas(): List<Empresa>

    //Buscar por CNPJ (para evitar duplicidades)
    fun findByCnpj(cnpj: String): Empresa

    //Buscar por nome com like (filtro no front)
    fun findByNomeEmpresaContainingIgnoreCase(nome: String): List<Empresa>
}

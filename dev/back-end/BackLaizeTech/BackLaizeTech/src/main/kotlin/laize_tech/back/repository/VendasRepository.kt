package laize_tech.back.repository

import jakarta.transaction.Transactional
import laize_tech.back.entity.Vendas
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate

interface VendasRepository : JpaRepository<Vendas, Int> {

    @Transactional
    @Modifying
    @Query("select e from Vendas e")
    fun findAllVendas(): List<Vendas>

    // Dynamic Finder: vendas concluídas
    fun findByStatusVendasTrue(): List<Vendas>

    // Dynamic Finder: intervalo de datas
    fun findByDtVendaBetween(dataInicio: LocalDate, dataFim: LocalDate): List<Vendas>

    //Somar valor total das vendas em um período
    @Query("SELECT SUM(v.precoVenda) FROM Vendas v WHERE v.dtVenda BETWEEN :dataInicio AND :dataFim")
    fun somaPrecoVendasPorPeriodo(dataInicio: LocalDate, dataFim: LocalDate): Double?

    // Dynamic Finder: por plataforma
    fun findByPlataformaIdPlataforma(idPlataforma: Int): List<Vendas>

    // Dynamic Finder: por empresa
    fun findByEmpresaIdEmpresa(idEmpresa: Int): List<Vendas>
}
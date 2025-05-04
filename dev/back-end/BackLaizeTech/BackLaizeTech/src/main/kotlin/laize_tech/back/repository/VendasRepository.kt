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

    //Buscar todas as vendas concluídas
    @Query("SELECT v FROM Vendas v WHERE v.statusVendas = true")
    fun findVendasConcluidas(): List<Vendas>

    //Buscar vendas por intervalo de datas
    @Query("SELECT v FROM Vendas v WHERE v.dtVenda BETWEEN :dataInicio AND :dataFim")
    fun findVendasPorPeriodo(dataInicio: LocalDate, dataFim: LocalDate): List<Vendas>

    //Somar valor total das vendas em um período
    @Query("SELECT SUM(v.precoVenda) FROM Vendas v WHERE v.dtVenda BETWEEN :dataInicio AND :dataFim")
    fun somaPrecoVendasPorPeriodo(dataInicio: LocalDate, dataFim: LocalDate): Double?

    //Buscar vendas por plataforma específica
    @Query("SELECT v FROM Vendas v WHERE v.plataforma.idPlataforma = :idPlataforma")
    fun findByPlataforma(idPlataforma: Int): List<Vendas>

    //Buscar vendas por empresa específica
    @Query("SELECT v FROM Vendas v WHERE v.empresa.idEmpresa = :idEmpresa")
    fun findByEmpresa(idEmpresa: Int): List<Vendas>
}
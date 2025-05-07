package laize_tech.back.repository

import jakarta.transaction.Transactional
import laize_tech.back.entity.ItensVenda
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface ItensVendaRepository : JpaRepository<ItensVenda, Int> {

    @Transactional
    @Modifying
    @Query("select e from ItensVenda e")
    fun findAllItensVenda(): List<ItensVenda>

    // Buscar itens por ID da venda
    fun findByVenda_IdVendas(idVenda: Int): List<ItensVenda>

    // Buscar itens por ID do produto
    fun findByProduto_IdProduto(idProduto: Int): List<ItensVenda>

    // Buscar todos os itens de uma empresa
    fun findByEmpresa_IdEmpresa(idEmpresa: Int): List<ItensVenda>

    // Buscar itens por plataforma
    fun findByPlataforma_IdPlataforma(idPlataforma: Int): List<ItensVenda>

    //Somar o subtotal de todos os itens de uma venda
    @Query("SELECT SUM(i.subTotal) FROM ItensVenda i WHERE i.venda.idVendas = :idVenda")
    fun somaSubTotalPorVenda(idVenda: Int): Double?
}
package laize_tech.back.repository

import jakarta.transaction.Transactional
import laize_tech.back.entity.ItensSaida
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface ItensVendaRepository : JpaRepository<ItensSaida, Int> {

    @Transactional
    @Modifying
    @Query("select e from ItensSaida e")
    fun findAllItensVenda(): List<ItensSaida>

    // Buscar itens por ID da venda
    fun findByVenda_IdVendas(idVenda: Int): List<ItensSaida>

    // Buscar itens por ID do produto
    fun findByProduto_IdProduto(idProduto: Int): List<ItensSaida>

    // Buscar todos os itens de uma empresa
    fun findByEmpresa_IdEmpresa(idEmpresa: Int): List<ItensSaida>

    // Buscar itens por plataforma
    fun findByPlataforma_IdPlataforma(idPlataforma: Int): List<ItensSaida>

    //Somar o subtotal de todos os itens de uma venda
    @Query("SELECT SUM(i.subTotal) FROM ItensSaida i WHERE i.venda.idVendas = :idVenda")
    fun somaSubTotalPorVenda(idVenda: Int): Double?
}
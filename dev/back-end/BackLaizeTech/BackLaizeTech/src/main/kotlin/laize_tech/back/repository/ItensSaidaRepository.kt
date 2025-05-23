package laize_tech.back.repository

import laize_tech.back.entity.ItensSaida
import org.springframework.data.jpa.repository.JpaRepository

interface ItensSaidaRepository : JpaRepository<ItensSaida, Int> {

//    @Transactional
//    @Modifying
//    @Query("select e from ItensSaida e")
//    fun findAllItensSaida(): List<ItensSaida>
//
//    // Buscar itens por ID da saida
//    fun findBySaida_IdSaida(idSaida: Int): List<ItensSaida>
//
//    // Buscar itens por ID do produto
//    fun findByProduto_IdProduto(idProduto: Int): List<ItensSaida>
//
//    // Buscar todos os itens de uma empresa
//    fun findByEmpresa_IdEmpresa(idEmpresa: Int): List<ItensSaida>
//
//    // Buscar itens por plataforma
//    fun findByPlataforma_IdPlataforma(idPlataforma: Int): List<ItensSaida>
//
//    //Somar o subtotal de todos os itens de uma saida
//    @Query("SELECT SUM(i.subTotal) FROM ItensSaida i WHERE i.saida.idSaida = :idSaida")
//    fun somaSubTotalPorSaida(idSaida: Int): Double?
}
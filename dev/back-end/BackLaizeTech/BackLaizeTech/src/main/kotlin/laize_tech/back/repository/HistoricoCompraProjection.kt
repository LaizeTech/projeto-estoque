package laize_tech.back.repository

import java.math.BigDecimal
import java.time.LocalDate

interface HistoricoCompraProjection {
    fun getIdCompraProduto(): Int
    fun getFornecedor(): String
    fun getPrecoCompra(): BigDecimal
    fun getDtCompra(): LocalDate
    fun getQuantidadeProduto(): Int
    fun getNomeCategoria(): String
    fun getNomeProduto(): String
}
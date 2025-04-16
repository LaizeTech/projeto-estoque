package laize_tech.back.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import java.time.LocalDate

@Entity
data class ItensVenda(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var idItem: Int?,
    var quantidade: Int?,
    var precoUnitario: Double?,
    var subTotal: Double?,
    @ManyToOne @JoinColumn(name = "fkEmpresa") var empresa: Empresa?,
    @ManyToOne @JoinColumn(name = "fkPlataforma") var plataforma: Plataforma?,
    @ManyToOne @JoinColumn(name = "fkProduto") var produto: Produto?,
    @ManyToOne @JoinColumn(name = "fkVenda") var venda: Vendas?
){
    constructor() : this(null, null, null, null, null, null, null, null)

}


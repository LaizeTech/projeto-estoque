package laize_tech.back.entity

import jakarta.persistence.*

@Entity
data class Usuario(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idUsuario: Long,

    val nome: String,
    val email: String,
    val senha: String,
    val acessoFinanceiro: Boolean,

    @ManyToOne
    @JoinColumn(name = "fkEmpresa")
    val empresa: Empresa
) {
    constructor() : this(0, "", "", "", false, Empresa())
}
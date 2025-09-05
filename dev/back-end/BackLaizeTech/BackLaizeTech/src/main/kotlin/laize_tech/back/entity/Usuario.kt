package laize_tech.back.entity

import jakarta.persistence.*

@Entity
data class Usuario(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idUsuario: Int,

    var nome: String,
    var email: String,
    var senha: String,
    var acessoFinanceiro: Boolean,

    @ManyToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "id_empresa")
    var empresa: Empresa
) {
    constructor() : this(0, "", "", "", false, Empresa())
}
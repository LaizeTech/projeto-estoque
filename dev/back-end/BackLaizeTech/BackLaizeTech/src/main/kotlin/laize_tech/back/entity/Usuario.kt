package laize_tech.back.entity

import jakarta.persistence.*

@Entity
data class Usuario(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    var idUsuario: Int,

    var nome: String,
    var email: String,
    var senha: String,
    @Column(name = "acesso_financeiro")
    var acessoFinanceiro: Boolean,
    @Column(name = "status_ativo")
    var statusAtivo: Boolean,

    @ManyToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "id_empresa")
    var empresa: Empresa
) {
    constructor() : this(0, "", "", "", false, true, Empresa())
}
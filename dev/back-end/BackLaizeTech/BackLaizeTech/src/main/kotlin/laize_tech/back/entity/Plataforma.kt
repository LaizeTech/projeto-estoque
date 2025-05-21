package laize_tech.back.entity

import jakarta.persistence.*

@Entity
@Table(name = "Plataforma")
class Plataforma(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPlataforma")
    var idPlataforma: Long? = null,

    @ManyToOne
    @JoinColumn(name = "fkEmpresa")
    var empresa: Empresa? = null,

    var nomePlataforma: String? = null,

    var status: Boolean? = null

) {
    constructor() : this(null, null, null, null)
}

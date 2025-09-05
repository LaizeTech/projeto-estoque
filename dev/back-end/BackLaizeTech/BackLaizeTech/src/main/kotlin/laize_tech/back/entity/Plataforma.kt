package laize_tech.back.entity

import jakarta.persistence.*

@Entity
@Table(name = "Plataforma")
class Plataforma(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plataforma")
    var idPlataforma: Int? = null,

    @Column(name = "nome_plataforma")
    var nomePlataforma: String? = null,

    var status: Boolean? = null,

    @ManyToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "id_empresa")
    var empresa: Empresa? = null

) {
    constructor() : this(null, null, null, null)
}

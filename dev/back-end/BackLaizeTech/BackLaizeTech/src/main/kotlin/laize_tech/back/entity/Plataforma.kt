package laize_tech.back.entity

import jakarta.persistence.*

@Entity
@Table(name = "Plataforma")
class Plataforma(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPlataforma")
    var idPlataforma: Int? = null,

    var nomePlataforma: String? = null,

    var status: Boolean? = null,

    @ManyToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "id_empresa")
    var empresa: Empresa? = null

) {
    constructor() : this(null, null, null, null)
}

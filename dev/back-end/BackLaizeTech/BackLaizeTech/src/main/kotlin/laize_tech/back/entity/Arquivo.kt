package laize_tech.back.entity

import jakarta.persistence.*

@Entity
data class Arquivo(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val nomeOriginal: String,

    val tipo: String,

    val url: String
)
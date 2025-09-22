package laize_tech.back.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull

enum class StatusVendaEnum {
    PENDENTE, FINALIZADA, CANCELADA
}

@Entity
data class StatusVenda(

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_status_venda")
    var idStatusVenda: Int? = null,

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "nome_status")
    var nomeStatus: StatusVendaEnum = StatusVendaEnum.PENDENTE
) {
    constructor() : this(null, StatusVendaEnum.PENDENTE)
}

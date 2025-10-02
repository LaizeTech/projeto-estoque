package laize_tech.back.dto

import laize_tech.back.entity.Empresa

data class BuscarFuncDTO(
    val idUsuario: Int,
    val nome: String,
    val statusAtivo: Boolean
)

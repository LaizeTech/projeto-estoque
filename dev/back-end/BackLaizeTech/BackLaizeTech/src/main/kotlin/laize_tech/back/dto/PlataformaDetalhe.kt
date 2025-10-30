package laize_tech.back.dto

// Interface para o DTO que será retornado pelo Repository
interface PlataformaDetalhe {
    fun getFkPlataforma(): Int
    fun getNomePlataforma(): String
}
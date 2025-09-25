package laize_tech.back.repository

interface ProdutoAlertaProjection {
    fun getNomeProduto(): String
    fun getQuantidadeProduto(): Int
    fun getNivelAlerta(): String
}
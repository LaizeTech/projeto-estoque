package laize_tech.back.dto

// DTO principal para a requisição de adicionar quantidade
data class AdicionarQtdRequestDTO(
    val fkProduto: Int, // ID do produto principal
    val plataformas: List<PlataformaQtdAdicionalDTO>
)

// DTO para a quantidade adicional em uma plataforma específica
data class PlataformaQtdAdicionalDTO(
    val fkPlataforma: Int, // ID da plataforma
    val quantidadeAdicional: Int // Quantidade a ser somada
)
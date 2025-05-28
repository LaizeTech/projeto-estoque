package laize_tech.back.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.io.Serializable

@ResponseStatus(HttpStatus.NOT_FOUND)
class IdNaoEncontradoException(
    val recurso: String ,
    val id: Serializable
): RuntimeException (){

    override val message: String?
        get() = "Não foi possível encontrar $recurso com o ID $id"

}
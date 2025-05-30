package laize_tech.back.ControllerJpa

import jakarta.validation.Valid
import laize_tech.back.dto.SaidaDTO
import laize_tech.back.entity.Saida
import laize_tech.back.exceptions.IdNaoEncontradoException
import laize_tech.back.repository.SaidaRepository
import laize_tech.back.repository.TipoSaidaRepository
import laize_tech.back.repository.EmpresaRepository
import laize_tech.back.repository.PlataformaRepository
import laize_tech.back.repository.StatusVendaRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/saidas")
class SaidaJpaController(
    val repositorio: SaidaRepository,
    val tipoSaidaRepository: TipoSaidaRepository,
    val empresaRepository: EmpresaRepository,
    val plataformaRepository: PlataformaRepository,
    val statusVendaRepository: StatusVendaRepository
) {

    @GetMapping
    fun get(): ResponseEntity<List<Saida>> {
        val saidas = repositorio.findAll()

        return if (saidas.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(saidas)
        }
    }

    @PostMapping
    fun post(@RequestBody @Valid novaSaidaDTO: SaidaDTO): ResponseEntity<Saida> {
        val tipoSaida = tipoSaidaRepository.findById(novaSaidaDTO.idTipoSaida).orElseThrow {
            IdNaoEncontradoException("TipoSaida", novaSaidaDTO.idTipoSaida)
        }

        val empresa = empresaRepository.findById(novaSaidaDTO.idEmpresa).orElseThrow {
            IdNaoEncontradoException("Empresa", novaSaidaDTO.idEmpresa)
        }

        val plataforma = plataformaRepository.findById(novaSaidaDTO.idPlataforma).orElseThrow {
            IdNaoEncontradoException("Plataforma", novaSaidaDTO.idPlataforma)
        }

        val statusVenda = statusVendaRepository.findById(novaSaidaDTO.idStatusVenda).orElseThrow {
            IdNaoEncontradoException("StatusVenda", novaSaidaDTO.idStatusVenda)
        }

        val dtVenda: LocalDate = novaSaidaDTO.dtVenda?.let { LocalDate.parse(it) } ?: LocalDate.now()

        val novaSaida = Saida(
            tipoSaida = tipoSaida,
            empresa = empresa,
            plataforma = plataforma,
            dtVenda = dtVenda,
            precoVenda = novaSaidaDTO.precoVenda,
            totalTaxa = novaSaidaDTO.totalTaxa,
            totalDesconto = novaSaidaDTO.totalDesconto,
            statusVenda = statusVenda
        )

        val saidaSalva = repositorio.save(novaSaida)
        return ResponseEntity.status(201).body(saidaSalva)
    }

    @PutMapping("/{id}")
    fun put(@PathVariable id: Int, @RequestBody saidaAtualizadaDTO: SaidaDTO): ResponseEntity<Any> {
        val saidaExistente = repositorio.findById(id).orElseThrow {
            IdNaoEncontradoException("Saída", id)
        }

        val tipoSaida = tipoSaidaRepository.findById(saidaAtualizadaDTO.idTipoSaida).orElseThrow {
            IdNaoEncontradoException("TipoSaida", saidaAtualizadaDTO.idTipoSaida)
        }

        val empresa = empresaRepository.findById(saidaAtualizadaDTO.idEmpresa).orElseThrow {
            IdNaoEncontradoException("Empresa", saidaAtualizadaDTO.idEmpresa)
        }

        val plataforma = plataformaRepository.findById(saidaAtualizadaDTO.idPlataforma).orElseThrow {
            IdNaoEncontradoException("Plataforma", saidaAtualizadaDTO.idPlataforma)
        }

        val statusVenda = statusVendaRepository.findById(saidaAtualizadaDTO.idStatusVenda).orElseThrow {
            IdNaoEncontradoException("StatusVenda", saidaAtualizadaDTO.idStatusVenda)
        }

        saidaExistente.tipoSaida = tipoSaida
        saidaExistente.empresa = empresa
        saidaExistente.plataforma = plataforma
        saidaExistente.dtVenda = saidaAtualizadaDTO.dtVenda?.let { LocalDate.parse(it) } ?: saidaExistente.dtVenda
        saidaExistente.precoVenda = saidaAtualizadaDTO.precoVenda ?: saidaExistente.precoVenda
        saidaExistente.totalTaxa = saidaAtualizadaDTO.totalTaxa ?: saidaExistente.totalTaxa
        saidaExistente.totalDesconto = saidaAtualizadaDTO.totalDesconto ?: saidaExistente.totalDesconto
        saidaExistente.statusVenda = statusVenda

        val saidaSalva = repositorio.save(saidaExistente)
        return ResponseEntity.status(200).body(saidaSalva)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<String> {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id)
            return ResponseEntity.status(204).build()
        }

        return ResponseEntity.status(404).body("")
    }
}
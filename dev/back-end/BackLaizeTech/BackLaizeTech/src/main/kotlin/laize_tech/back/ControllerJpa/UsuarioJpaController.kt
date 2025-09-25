package laize_tech.back.ControllerJpa

import jakarta.validation.Valid
import laize_tech.back.dto.ListagemUsuarioDTO
import laize_tech.back.dto.LoginDTO
import laize_tech.back.dto.LoginResponseDTO
import laize_tech.back.dto.UsuarioDTO
import laize_tech.back.entity.Usuario
import laize_tech.back.exceptions.IdNaoEncontradoException
import laize_tech.back.repository.EmpresaRepository
import laize_tech.back.repository.UsuarioRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/usuarios")
class UsuarioJpaController(
    val repositorio: UsuarioRepository,
    val empresaRepository: EmpresaRepository
) {

    @PostMapping
    fun post(@RequestBody @Valid novoUsuarioDTO: UsuarioDTO): ResponseEntity<Any> {
        if (novoUsuarioDTO.nome.isNullOrBlank() || novoUsuarioDTO.senha.isNullOrBlank() || novoUsuarioDTO.email.isNullOrBlank() || novoUsuarioDTO.idEmpresa == null) {
            return ResponseEntity.status(400).body("Os campos nome, senha e email não podem estar vazios ou nulos!")
        }

        if (repositorio.findAll().any { it.email == novoUsuarioDTO.email }) {
            return ResponseEntity.status(409).body("Já existe um usuário cadastrado com esse e-mail!")
        }

        val empresa = empresaRepository.findById(novoUsuarioDTO.idEmpresa).orElseThrow {
            IdNaoEncontradoException("Empresa", novoUsuarioDTO.idEmpresa)
        }

        val novoUsuario = Usuario(
            idUsuario = 0,
            nome = novoUsuarioDTO.nome,
            email = novoUsuarioDTO.email,
            senha = novoUsuarioDTO.senha,
            acessoFinanceiro = novoUsuarioDTO.acessoFinanceiro ?: false,
            statusAtivo = novoUsuarioDTO.statusAtivo ?: true,
            empresa = empresa
        )
        return try {
            val usuarioSalvo = repositorio.save(novoUsuario)
            ResponseEntity.status(201).body(usuarioSalvo)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar usuário")
        }
    }

    @PatchMapping("/{id}/alterar-senha")
    fun alterarSenha(
        @PathVariable id: Int,
        @RequestBody payload: Map<String, String>
    ): ResponseEntity<String> {
        val novaSenha = payload["novaSenha"]
        val confirmacaoSenha = payload["confirmacaoSenha"]

        if (novaSenha.isNullOrBlank() || confirmacaoSenha.isNullOrBlank()) {
            return ResponseEntity.status(400).body("Os campos 'novaSenha' e 'confirmacaoSenha' são obrigatórios.")
        }

        if (novaSenha != confirmacaoSenha) {
            return ResponseEntity.status(400).body("A senha e a confirmação de senha não coincidem.")
        }

        val usuario = repositorio.findById(id).orElse(null)
            ?: return ResponseEntity.status(404).body("Usuário com o ID $id não encontrado.")

        usuario.senha = novaSenha

        return try {
            repositorio.save(usuario)
            ResponseEntity.status(200).body("Senha alterada com sucesso.")
        } catch (e: Exception) {
            ResponseEntity.status(500).body("Erro ao alterar a senha.")
        }
    }

    @PatchMapping("/{id}/alterar-email")
    fun alterarEmail(
        @PathVariable id: Int,
        @RequestBody payload: Map<String, String>
    ): ResponseEntity<String> {
        val novoEmail = payload["novoEmail"]

        if (novoEmail.isNullOrBlank()) {
            return ResponseEntity.status(400).body("O campo 'novoEmail' é obrigatório.")
        }

        if (repositorio.findAll().any { it.email == novoEmail }) {
            return ResponseEntity.status(409).body("Já existe um usuário cadastrado com o e-mail '$novoEmail'.")
        }

        val usuario = repositorio.findById(id).orElse(null)
            ?: return ResponseEntity.status(404).body("Usuário com o ID $id não encontrado.")

        usuario.email = novoEmail

        return try {
            repositorio.save(usuario)
            ResponseEntity.status(200).body("E-mail alterado com sucesso.")
        } catch (e: Exception) {
            ResponseEntity.status(500).body("Erro ao alterar o e-mail.")
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody loginDTO: LoginDTO): ResponseEntity<Any> {
        val email = loginDTO.email
        val senha = loginDTO.senha

        if (email.isNullOrBlank() || senha.isNullOrBlank()) {
            return ResponseEntity.status(400).body("E-mail e senha são obrigatórios.")
        }

        val usuarioOpt = repositorio.findByEmail(email)

        if (usuarioOpt.isEmpty) {
            return ResponseEntity.status(401).body("E-mail ou senha inválidos.")
        }

        val usuario = usuarioOpt.get()

        if (usuario.senha != senha) {
            return ResponseEntity.status(401).body("E-mail ou senha inválidos.")
        }

        val usuarioLogado = LoginResponseDTO(
            idUsuario = usuario.idUsuario,
            nome = usuario.nome,
            email = usuario.email,
            acessoFinanceiro = usuario.acessoFinanceiro,
            statusAtivo = usuario.statusAtivo,
            empresa = usuario.empresa
        )

        return ResponseEntity.ok(usuarioLogado)
    }

    @PutMapping("/{id}")
    fun put(@PathVariable id: Int, @RequestBody @Valid usuarioDTO: UsuarioDTO): ResponseEntity<Any> {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.status(404).body("Usuário com o ID $id não encontrado.")
        }

        if (usuarioDTO.nome.isNullOrBlank() || usuarioDTO.senha.isNullOrBlank() || usuarioDTO.email.isNullOrBlank() || usuarioDTO.idEmpresa == null) {
            return ResponseEntity.status(400).body("Os campos nome, senha e email não podem estar vazios ou nulos!")
        }

        val usuarioExistente = repositorio.findById(id.toInt()).orElse(null)

        if (repositorio.findAll().any { it.email == usuarioDTO.email && it.idUsuario != id }) {
            return ResponseEntity.status(409).body("Já existe um usuário cadastrado com o e-mail '${usuarioDTO.email}'.")
        }

        val empresa = empresaRepository.findById(usuarioDTO.idEmpresa).orElseThrow {
            IdNaoEncontradoException("Empresa", usuarioDTO.idEmpresa)
        }

        usuarioExistente.nome = usuarioDTO.nome
        usuarioExistente.email = usuarioDTO.email
        usuarioExistente.senha = usuarioDTO.senha
        usuarioExistente.acessoFinanceiro = usuarioDTO.acessoFinanceiro ?: false
        usuarioExistente.statusAtivo = usuarioDTO.statusAtivo ?: usuarioExistente.statusAtivo
        usuarioExistente.empresa = empresa

        return try {
            val usuarioSalvo = repositorio.save(usuarioExistente)
            return ResponseEntity.status(200).body(usuarioSalvo)
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar usuário")
        }
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<String> {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id)
            return ResponseEntity.status(204).build()
        }
        return ResponseEntity.status(404).body("Usuário com o ID $id não encontrado.")
    }

    @GetMapping
    fun get(): ResponseEntity<List<ListagemUsuarioDTO>> {
        val usuarios = repositorio.findAll()
        val listagemUsuarios = mutableListOf<ListagemUsuarioDTO>()
        for (usuario in usuarios) {
            val listagemUsuario = ListagemUsuarioDTO(
                nome = usuario.nome,
                email = usuario.email,
                acessoFinanceiro = usuario.acessoFinanceiro,
                statusAtivo = usuario.statusAtivo,
                idEmpresa = usuario.empresa.idEmpresa
            )
            listagemUsuarios.add(listagemUsuario)
        }
        return if (listagemUsuarios.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(listagemUsuarios)
        }
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: Int): ResponseEntity<Any> {
        val usuario = repositorio.findById(id)
        return if (usuario.isPresent) {
            val usuarioEncontrado = usuario.get()
            val listagemUsuario = ListagemUsuarioDTO(
                nome = usuarioEncontrado.nome,
                email = usuarioEncontrado.email,
                acessoFinanceiro = usuarioEncontrado.acessoFinanceiro,
                statusAtivo = usuarioEncontrado.statusAtivo,
                idEmpresa = usuarioEncontrado.empresa.idEmpresa
            )
            ResponseEntity.status(200).body(listagemUsuario)
        } else {
            ResponseEntity.status(404).body("Usuário com o ID $id não encontrado.")
        }
    }

    @GetMapping("/nome/{nome}")
    fun getByNome(@PathVariable nome: String): ResponseEntity<List<ListagemUsuarioDTO>> {
        val usuarios = repositorio.findByNome(nome)
        val listagemUsuarios = usuarios.map { usuario ->
            ListagemUsuarioDTO(
                nome = usuario.nome,
                email = usuario.email,
                acessoFinanceiro = usuario.acessoFinanceiro,
                statusAtivo = usuario.statusAtivo,
                idEmpresa = usuario.empresa.idEmpresa
            )
        }
        return if (listagemUsuarios.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(listagemUsuarios)
        }
    }

    @GetMapping("/acessoFinanceiro/{acessoFinanceiro}")
    fun getByAcessoFinanceiro(@PathVariable acessoFinanceiro: Boolean): ResponseEntity<List<ListagemUsuarioDTO>> {
        val usuarios = repositorio.findByAcessoFinanceiro(acessoFinanceiro)
        val listagemUsuarios = usuarios.map { usuario ->
            ListagemUsuarioDTO(
                nome = usuario.nome,
                email = usuario.email,
                acessoFinanceiro = usuario.acessoFinanceiro,
                statusAtivo = usuario.statusAtivo,
                idEmpresa = usuario.empresa.idEmpresa
            )
        }
        return if (listagemUsuarios.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(listagemUsuarios)
        }
    }

    @GetMapping("/contar-ativos")
    fun contarUsuariosAtivos(): ResponseEntity<Map<String, Long>> {
        val count = repositorio.countByAtivo(true)
        val response = mapOf("usuarios_ativos" to count)
        return ResponseEntity.status(200).body(response)
    }

}
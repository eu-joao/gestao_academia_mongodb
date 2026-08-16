package br.edu.ifpb.academia.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.academia.dto.request.AlunoRequest;
import br.edu.ifpb.academia.dto.response.Responses.AlunoResponse;
import br.edu.ifpb.academia.service.AlunoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/alunos")
@RequiredArgsConstructor
public class AlunoController {
	private final AlunoService service;


	@Operation(summary = "Lista alunos")
	@GetMapping
	public List<AlunoResponse> listar() {
		return service.listar();
	}

	@Operation(summary = "Busca aluno por ID")
	@GetMapping("/{id}")
	public AlunoResponse buscar(@PathVariable Long id) {
		return service.buscar(id);
	}

	@Operation(summary = "Cria aluno e treino ativo automaticamente")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public AlunoResponse criar(@Valid @RequestBody AlunoRequest request) {
		return service.criar(request);
	}

	@Operation(summary = "Atualiza aluno e recria treino ativo quando o perfil muda")
	@PutMapping("/{id}")
	public AlunoResponse atualizar(@PathVariable Long id, @Valid @RequestBody AlunoRequest request) {
		return service.atualizar(id, request);
	}

	@Operation(summary = "Remove aluno")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void excluir(@PathVariable Long id) {
		service.excluir(id);
	}
}

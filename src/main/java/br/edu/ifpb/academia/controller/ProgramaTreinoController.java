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

import br.edu.ifpb.academia.dto.request.ProgramaTreinoRequest;
import br.edu.ifpb.academia.dto.response.Responses.ProgramaTreinoResponse;
import br.edu.ifpb.academia.service.ProgramaTreinoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/programas-treino")
@RequiredArgsConstructor
public class ProgramaTreinoController {
	private final ProgramaTreinoService service;


	@Operation(summary = "Lista programas de treino")
	@GetMapping
	public List<ProgramaTreinoResponse> listar() {
		return service.listar();
	}

	@Operation(summary = "Busca programa de treino por ID")
	@GetMapping("/{id}")
	public ProgramaTreinoResponse buscar(@PathVariable Long id) {
		return service.buscar(id);
	}

	@Operation(summary = "Cria programa de treino")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public ProgramaTreinoResponse criar(@Valid @RequestBody ProgramaTreinoRequest request) {
		return service.criar(request);
	}

	@Operation(summary = "Atualiza programa de treino")
	@PutMapping("/{id}")
	public ProgramaTreinoResponse atualizar(@PathVariable Long id, @Valid @RequestBody ProgramaTreinoRequest request) {
		return service.atualizar(id, request);
	}

	@Operation(summary = "Remove programa de treino")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void excluir(@PathVariable Long id) {
		service.excluir(id);
	}
}

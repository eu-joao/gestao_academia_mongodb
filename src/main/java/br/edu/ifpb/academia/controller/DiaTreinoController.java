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

import br.edu.ifpb.academia.dto.request.DiaTreinoRequest;
import br.edu.ifpb.academia.dto.response.Responses.DiaTreinoResponse;
import br.edu.ifpb.academia.service.DiaTreinoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/dias-treino")
@RequiredArgsConstructor
public class DiaTreinoController {
	private final DiaTreinoService service;


	@Operation(summary = "Lista dias de treino")
	@GetMapping
	public List<DiaTreinoResponse> listar() {
		return service.listar();
	}

	@Operation(summary = "Busca dia de treino por ID")
	@GetMapping("/{id}")
	public DiaTreinoResponse buscar(@PathVariable Long id) {
		return service.buscar(id);
	}

	@Operation(summary = "Cria dia de treino")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public DiaTreinoResponse criar(@Valid @RequestBody DiaTreinoRequest request) {
		return service.criar(request);
	}

	@Operation(summary = "Atualiza dia de treino")
	@PutMapping("/{id}")
	public DiaTreinoResponse atualizar(@PathVariable Long id, @Valid @RequestBody DiaTreinoRequest request) {
		return service.atualizar(id, request);
	}

	@Operation(summary = "Remove dia de treino")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void excluir(@PathVariable Long id) {
		service.excluir(id);
	}
}

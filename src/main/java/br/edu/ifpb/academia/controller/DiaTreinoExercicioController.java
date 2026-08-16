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

import br.edu.ifpb.academia.dto.request.DiaTreinoExercicioRequest;
import br.edu.ifpb.academia.dto.response.Responses.DiaTreinoExercicioResponse;
import br.edu.ifpb.academia.service.DiaTreinoExercicioService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/dias-treino-exercicios")
@RequiredArgsConstructor
public class DiaTreinoExercicioController {
	private final DiaTreinoExercicioService service;


	@Operation(summary = "Lista exercicios dos dias de treino")
	@GetMapping
	public List<DiaTreinoExercicioResponse> listar() {
		return service.listar();
	}

	@Operation(summary = "Busca exercicio de dia por ID")
	@GetMapping("/{id}")
	public DiaTreinoExercicioResponse buscar(@PathVariable Long id) {
		return service.buscar(id);
	}

	@Operation(summary = "Adiciona exercicio a um dia de treino")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public DiaTreinoExercicioResponse criar(@Valid @RequestBody DiaTreinoExercicioRequest request) {
		return service.criar(request);
	}

	@Operation(summary = "Atualiza exercicio de um dia de treino")
	@PutMapping("/{id}")
	public DiaTreinoExercicioResponse atualizar(@PathVariable Long id,
			@Valid @RequestBody DiaTreinoExercicioRequest request) {
		return service.atualizar(id, request);
	}

	@Operation(summary = "Remove exercicio de um dia de treino")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void excluir(@PathVariable Long id) {
		service.excluir(id);
	}
}

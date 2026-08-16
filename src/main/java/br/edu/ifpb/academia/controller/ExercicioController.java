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

import br.edu.ifpb.academia.dto.request.ExercicioRequest;
import br.edu.ifpb.academia.dto.response.Responses.ExercicioResponse;
import br.edu.ifpb.academia.service.ExercicioService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/exercicios")
@RequiredArgsConstructor
public class ExercicioController {
	private final ExercicioService service;


	@Operation(summary = "Lista exercicios")
	@GetMapping
	public List<ExercicioResponse> listar() {
		return service.listar();
	}

	@Operation(summary = "Busca exercicio por ID")
	@GetMapping("/{id}")
	public ExercicioResponse buscar(@PathVariable Long id) {
		return service.buscar(id);
	}

	@Operation(summary = "Cria exercicio")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public ExercicioResponse criar(@Valid @RequestBody ExercicioRequest request) {
		return service.criar(request);
	}

	@Operation(summary = "Atualiza exercicio")
	@PutMapping("/{id}")
	public ExercicioResponse atualizar(@PathVariable Long id, @Valid @RequestBody ExercicioRequest request) {
		return service.atualizar(id, request);
	}

	@Operation(summary = "Remove exercicio")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void excluir(@PathVariable Long id) {
		service.excluir(id);
	}
}
